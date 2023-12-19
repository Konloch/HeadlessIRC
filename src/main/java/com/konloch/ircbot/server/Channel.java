package com.konloch.ircbot.server;

import com.konloch.ircbot.listener.IRCJoin;
import com.konloch.ircbot.listener.IRCLeave;
import com.konloch.ircbot.listener.IRCChannelMessage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Channel
{
	private final Server server;
	private final String name;
	private boolean active = true;
	private boolean joined;
	private long lastJoinAttempt = System.currentTimeMillis();
	private final Queue<String> messageQueue = new LinkedList<>();
	private final List<User> users = new CopyOnWriteArrayList<>();
	
	public Channel(Server server, String name)
	{
		this.server = server;
		this.name = name;
	}
	
	public void process() throws IOException
	{
		//rejoin channel after 5 seconds from initial attempt
		if (!isJoined() && System.currentTimeMillis() - getLastJoinAttempt() >= 5000)
		{
			setLastJoinAttempt(System.currentTimeMillis());
			
			//join a channel
			server.send("JOIN " + getName());
		}
		
		//send any queued messages
		if (isJoined())
		{
			//send channel messages
			for (int i = 0; i < 5; i++)
			{
				if (getMessageQueue().isEmpty())
					break;
				
				String message = getMessageQueue().poll();
				server.send("PRIVMSG " + getNameIRCProtocol() + " :" + message);
			}
		}
	}
	
	public User add(String nickname)
	{
		//drop permissions
		if(nickname.startsWith("@") || nickname.startsWith("+"))
			nickname = nickname.substring(1);
		
		User user = get(nickname);
		if(user != null)
			return user;
		
		user = server.getUser(nickname);
		users.add(user);
		return user;
	}
	
	public User get(String nickname)
	{
		User user = null;
		for(User u : users)
		{
			if(u.getNickname().equals(nickname))
			{
				user = u;
				break;
			}
		}
		
		return user;
	}
	
	public User remove(String nickname)
	{
		User user = null;
		int index = 0;
		for(User u : users)
		{
			if(u.getNickname().equals(nickname))
			{
				user = u;
				break;
			}
			
			index++;
		}
	
		if(user != null)
			users.remove(index);
	
		return user;
	}
	
	public void send(String message)
	{
		messageQueue.add(message);
	}
	
	public void onJoin(IRCJoin join)
	{
		//filter listener events to only call for this server
		server.getBot().getListeners().onJoin(event ->
		{
			if(event.getChannel() != this)
				return;
			
			join.join(event);
		});
	}
	
	public void onLeave(IRCLeave leave)
	{
		//filter listener events to only call for this server
		server.getBot().getListeners().onLeave(event ->
		{
			if(event.getChannel() != this)
				return;
			
			leave.leave(event);
		});
	}
	
	public void onMessage(IRCChannelMessage message)
	{
		//filter listener events to only call for this server
		server.getBot().getListeners().onChannelMessage(event ->
		{
			if(event.getChannel() != this)
				return;
			
			message.message(event);
		});
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public List<User> getUsers()
	{
		return users;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNameIRCProtocol()
	{
		if(name.startsWith("#"))
			return name;
		
		return "#" + name;
	}
	
	public boolean isJoined()
	{
		return joined;
	}
	
	public long getLastJoinAttempt()
	{
		return lastJoinAttempt;
	}
	
	public Queue<String> getMessageQueue()
	{
		return messageQueue;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public void setJoined(boolean joined)
	{
		this.joined = joined;
	}
	
	public void setLastJoinAttempt(long lastJoinAttempt)
	{
		this.lastJoinAttempt = lastJoinAttempt;
	}
	
	@Override
	public String toString()
	{
		return server + "/" + getName();
	}
}
