package com.konloch.ircbot.server;

import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.listener.*;
import com.konloch.ircbot.message.integer.IntegerMessage;
import com.konloch.ircbot.message.text.TextMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static com.konloch.util.FastStringUtils.split;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Server implements Runnable
{
	private static final CharsetEncoder ENCODER = StandardCharsets.UTF_8.newEncoder();
	private static final CharsetDecoder DECODER = StandardCharsets.UTF_8.newDecoder();
	private static final Pattern IS_NUMBER = Pattern.compile("\\d+");
	private static final Pattern LINE_SPLITTER = Pattern.compile("\\r?\\n");
	
	private final IRCBot bot;
	private final String serverAddress;
	private final int port;
	private boolean active = true;
	private boolean encounteredError;
	
	private Selector selector;
	private SocketChannel socketChannel;
	
	private final Map<String,Channel> channels = new HashMap<>();
	private final Map<String,User> users = new HashMap<>();
	private final Object USER_LOCK = new Object();
	
	public Server(IRCBot bot, String serverAddress, int port)
	{
		this.bot = bot;
		this.serverAddress = serverAddress;
		this.port = port;
	}
	
	public Channel join(String channelName)
	{
		Channel channel = new Channel(this, channelName);
		channels.put(channelName, channel);
		return channel;
	}
	
	public void process()
	{
		try
		{
			internalProcess();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			encounteredError = true; //trip error flag
		}
	}
	
	private void internalProcess() throws IOException
	{
		//wait till connected
		if(socketChannel == null || socketChannel.isConnectionPending() || !socketChannel.isConnected())
			return;
		
		//remove any non-active channels
		channels.values().removeIf(channel -> !channel.isActive());
		
		//process all channels
		for(Channel channel : channels.values())
		{
			channel.process();
		}
		
		synchronized (USER_LOCK)
		{
			//process all private messages
			for (User user : users.values())
			{
				if (user.getMessageQueue().isEmpty())
					continue;
				
				for (int i = 0; i < 5; i++)
				{
					if (user.getMessageQueue().isEmpty())
						break;
					
					String message = user.getMessageQueue().poll();
					send("PRIVMSG " + user.getNickname() + " :" + message);
				}
			}
		}
		
	}
	
	@Override
	public void run()
	{
		while(active)
		{
			try
			{
				encounteredError = false; //reset error flag
				selector = Selector.open();
				
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);
				socketChannel.connect(new InetSocketAddress(serverAddress, port));
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
				
				while (!encounteredError)
				{
					selector.select();
					
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> keyIterator = keys.iterator();
					
					while (keyIterator.hasNext())
					{
						SelectionKey key = keyIterator.next();
						keyIterator.remove();
						
						if (key.isConnectable())
							connect();
						else if (key.isReadable())
							read();
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				close();
				
				//call on listener event
				bot.getListeners().callOnConnectionLost(this);
			}
		}
	}
	
	private void connect() throws IOException
	{
		if (socketChannel.isConnectionPending())
			socketChannel.finishConnect();
		
		socketChannel.register(selector, SelectionKey.OP_READ);
		
		//send NICK and USER commands to identify with the IRC server
		send("NICK " + bot.getNickname());
		send("USER " + bot.getNickname() + " 8 * :" + bot.getClient());
		
		//call on listener event
		bot.getListeners().callOnConnectionEstablished(this);
	}
	
	private void read() throws IOException
	{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		
		//decode into buffer
		buffer.flip();
		CharBuffer charBuffer = DECODER.decode(buffer);
		String[] stringBuffer = LINE_SPLITTER.split(charBuffer.toString());
		
		//parse each message
		for(String message : stringBuffer)
		{
			try
			{
				String messageLower = message.toLowerCase();
				
				if(messageLower.isEmpty())
					continue;
				
				if(bot.isDebug())
					System.out.println("IN:  " + message);
				
				//respond to PING with PONG
				if (messageLower.startsWith("ping"))
				{
					send("PONG " + message.substring(5));
				}
				
				//decode messages
				else if (message.startsWith(":"))
				{
					String[] splitPartMessage = split(message, " ", 5);
					
					if (splitPartMessage.length >= 3)
					{
						String command = splitPartMessage[1];
						String commandLowerCase = command.toLowerCase();
						
						//handle integer based commands
						if (splitPartMessage.length >= 4 && IS_NUMBER.matcher(command).matches())
						{
							int opcode = Integer.parseInt(splitPartMessage[1]);
							
							if(bot.isDebug())
								System.out.println("OP:  " + opcode);
							
							//look up the integer message
							IntegerMessage intMsg = IntegerMessage.opcode(opcode);
							
							//process the message
							if(intMsg != null)
								intMsg.getEvent().handle(this, splitPartMessage);
						}
						
						//handle text based commands
						else
						{
							//only a maximum of 4 parameters
							splitPartMessage = split(message, " ", 4);
							
							//look up the integer message
							TextMessage textMsg = TextMessage.opcode(commandLowerCase);
							
							//process the message
							if(textMsg != null)
								textMsg.getEvent().handle(this, splitPartMessage);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			buffer.clear();
		}
	}
	
	public void send(String message) throws IOException
	{
		if(bot.isDebug())
			System.out.println("OUT: " + message);
		
		String formattedMessage = message + "\r\n";
		ByteBuffer buffer = ENCODER.encode(CharBuffer.wrap(formattedMessage));
		socketChannel.write(buffer);
	}
	
	private void close()
	{
		try
		{
			if (socketChannel != null)
				socketChannel.close();
			if (selector != null)
				selector.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Channel getChannel(String channelName)
	{
		if(channelName.startsWith(":"))
			channelName = channelName.substring(1);
		
		Channel channel = channels.get(channelName);
		
		if(channel != null)
			return channel;
		
		channel = channels.get("#" + channelName);
		
		return channel;
	}
	
	public User getUser(String nickname)
	{
		User user = users.get(nickname);
		
		if(user == null)
		{
			user = new User(this, nickname);
			
			synchronized (USER_LOCK)
			{
				users.put(nickname, user);
			}
		}
		
		return user;
	}
	
	public void onJoin(IRCJoin join)
	{
		//filter listener events to only call for this server
		bot.getListeners().onJoin(event ->
		{
			if(event.getServer() != this)
				return;
			
			join.join(event);
		});
	}
	
	public void onConnectionEstablished(IRCConnectionEstablished established)
	{
		//filter listener events to only call for this server
		bot.getListeners().onConnectionEstablished(event ->
		{
			if(event.getServer() != this)
				return;
			
			established.established(event);
		});
	}
	
	public void onConnectionLost(IRCConnectionLost lost)
	{
		//filter listener events to only call for this server
		bot.getListeners().onConnectionLost(event ->
		{
			if(event.getServer() != this)
				return;
			
			lost.lost(event);
		});
	}
	
	public void onLeave(IRCLeave leave)
	{
		//filter listener events to only call for this server
		bot.getListeners().onLeave(event ->
		{
			if(event.getServer() != this)
				return;
			
			leave.leave(event);
		});
	}
	
	public void onChannelMessage(IRCChannelMessage message)
	{
		//filter listener events to only call for this server
		bot.getListeners().onChannelMessage(event ->
		{
			if(event.getServer() != this)
				return;
			
			message.message(event);
		});
	}
	
	public void onPrivateMessage(IRCPrivateMessage message)
	{
		//filter listener events to only call for this server
		bot.getListeners().onPrivateMessage(event ->
		{
			if(event.getServer() != this)
				return;
			
			message.message(event);
		});
	}
	
	public IRCBot getBot()
	{
		return bot;
	}
	
	public String getServerAddress()
	{
		return serverAddress;
	}
	
	public Collection<Channel> getChannels()
	{
		return channels.values();
	}
	
	public Collection<User> getUsers()
	{
		return users.values();
	}
	
	@Override
	public String toString()
	{
		return getServerAddress();
	}
}