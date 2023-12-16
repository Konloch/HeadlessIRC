package com.konloch.ircbot.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Room
{
	private final Server server;
	private final String name;
	private boolean active = true;
	private boolean joined;
	private long lastJoinAttempt = System.currentTimeMillis();
	private final Queue<String> messageQueue = new LinkedList<>();
	private final List<User> users = new CopyOnWriteArrayList<>();
	
	public Room(Server server, String name)
	{
		this.server = server;
		this.name = name;
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
}
