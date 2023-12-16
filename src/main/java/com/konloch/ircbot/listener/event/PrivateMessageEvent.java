package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class PrivateMessageEvent extends GenericServerEvent
{
	private final User user;
	private final String message;
	
	public PrivateMessageEvent(Server server, User user, String message)
	{
		super(server);
		this.user = user;
		this.message = message;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public String getMessage()
	{
		return message;
	}
}
