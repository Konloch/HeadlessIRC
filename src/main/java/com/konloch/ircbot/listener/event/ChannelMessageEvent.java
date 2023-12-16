package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class ChannelMessageEvent extends GenericChannelEvent
{
	private final String message;
	
	public ChannelMessageEvent(Server server, Channel channel, User user, String message)
	{
		super(server, channel, user);
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}
