package com.konloch.ircbot.server;

import com.konloch.ircbot.IRCBot;
import com.konloch.ircbot.event.IRCJoin;
import com.konloch.ircbot.event.IRCLeave;
import com.konloch.ircbot.event.IRCPrivateMessage;
import com.konloch.ircbot.event.IRCRoomMessage;
import com.konloch.util.FastStringUtils;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
	private final String server;
	private final int port;
	private boolean active = true;
	
	private Selector selector;
	private SocketChannel socketChannel;
	
	private final List<Room> rooms = new ArrayList<>();
	
	public Server(IRCBot bot, String server, int port)
	{
		this.bot = bot;
		this.server = server;
		this.port = port;
	}
	
	public Room join(String channel)
	{
		Room room = new Room(this, channel);
		rooms.add(room);
		return room;
	}
	
	public void process()
	{
		//wait till connected
		if(socketChannel == null || socketChannel.isConnectionPending() || !socketChannel.isConnected())
			return;
		
		//remove any non-active rooms
		rooms.removeIf(room -> !room.isActive());
		
		//process all rooms
		for(Room room : rooms)
		{
			room.process();
		}
	}
	
	@Override
	public void run()
	{
		while(active)
		{
			try
			{
				selector = Selector.open();
				
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);
				socketChannel.connect(new InetSocketAddress(server, port));
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
				
				while (active)
				{
					try
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
					catch (Exception e)
					{
						e.printStackTrace();
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
							
							switch(opcode)
							{
								//case 333: //RPL_TOPICWHOTIME
								case 353: //RPL_NAMREPLY
								{
									String[] channelThenUsers = split(splitPartMessage[4], " ", 2);
									
									if(channelThenUsers.length <= 0)
										continue;
									
									String channelName = channelThenUsers[0];
									String[] users = split(channelThenUsers[1].substring(1), " ");
									
									Room room = get(channelName);
									
									if(room != null)
									{
										room.setJoined(true);
										
										switch(opcode)
										{
											case 333: //RPL_TOPICWHOTIME
												break;
											case 353: //RPL_NAMREPLY
												for(String nickname : users)
												{
													User user = room.add(nickname);
													bot.getGlobalEvents().callOnJoin(room, user);
												}
												break;
										}
									}
								}
								break;
							}
						}
						
						//handle text based commands
						else
						{
							//only a maximum of 4 parameters
							splitPartMessage = split(message, " ", 4);
							
							switch (commandLowerCase)
							{
								//decode messages
								case "privmsg":
								{
									String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
									String location = splitPartMessage[2];
									String msg = splitPartMessage[3].substring(1);
									
									if(location.startsWith("#"))
									{
										Room room = get(location);
										
										if (room != null)
										{
											room.setJoined(true);
											User user = room.get(nickname);
											bot.getGlobalEvents().callRoomMessage(room, user, msg);
										}
									}
									else
									{
										//TODO handle pm
									}
								}
								break;
								
								//decode join
								case "join":
								{
									String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
									String roomName = splitPartMessage[2];
									
									Room room = get(roomName);
									
									if (room != null)
									{
										room.setJoined(true);
										User user = room.add(nickname);
										bot.getGlobalEvents().callOnJoin(room, user);
									}
								}
								break;
								
								//decode quit
								case "quit":
								{
									String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
									
									for(Room room : rooms)
									{
										User user = room.remove(nickname);
										if(user != null)
										{
											bot.getGlobalEvents().callOnLeave(room, user);
											break;
										}
									}
								}
								break;
							}
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
	
	public Room get(String room)
	{
		if(room.startsWith(":"))
			room = room.substring(1);
		
		for(Room r : rooms)
			if(r.getName().equalsIgnoreCase(room) ||
					r.getName().equalsIgnoreCase("#" + room))
				return r;
		
		return null;
	}
	
	public void onJoin(IRCJoin join)
	{
		//filter the global event to only call for this server
		bot.getGlobalEvents().onJoin((room, user) ->
		{
			if(room.getServer() != this)
				return;
			
			join.join(room, user);
		});
	}
	
	public void onLeave(IRCLeave leave)
	{
		//filter the global event to only call for this server
		bot.getGlobalEvents().onLeave((room, user) ->
		{
			if(room.getServer() != this)
				return;
			
			leave.leave(room, user);
		});
	}
	
	public void onRoomMessage(IRCRoomMessage roomMessage)
	{
		//filter the global event to only call for this server
		bot.getGlobalEvents().onRoomMessage((room, user, msg) ->
		{
			if(room.getServer() != this)
				return;
			
			roomMessage.message(room, user, msg);
		});
	}
	
	public void onPrivateMessage(IRCPrivateMessage privateMessage)
	{
		//filter the global event to only call for this server
		bot.getGlobalEvents().onPrivateMessage((user, msg) ->
		{
			if(user.getServer() != this)
				return;
			
			privateMessage.message(user, msg);
		});
	}
	
	public List<Room> getRooms()
	{
		return rooms;
	}
	
	public IRCBot getBot()
	{
		return bot;
	}
}