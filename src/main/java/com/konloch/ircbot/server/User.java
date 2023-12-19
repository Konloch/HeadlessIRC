package com.konloch.ircbot.server;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class User
{
	private final Server server;
	private final String nickname;
	private final Queue<String> messageQueue = new LinkedList<>();
	
	protected User(Server server, String nickname)
	{
		this.server = server;
		this.nickname = nickname;
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public String getNickname()
	{
		return nickname;
	}
	
	public Queue<String> getMessageQueue()
	{
		return messageQueue;
	}
	
	public void send(String message)
	{
		messageQueue.add(message);
	}
	
	public boolean isSelfBot()
	{
		return nickname.equals(server.getBot().getNickname());
	}
	
	@Override
	public String toString()
	{
		return server + "/" + nickname;
	}
}