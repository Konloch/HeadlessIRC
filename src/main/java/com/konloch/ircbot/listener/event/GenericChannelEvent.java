package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class GenericChannelEvent extends GenericServerEvent
{
	private final Channel channel;
	private final User user;
	
	public GenericChannelEvent(Server server, Channel channel, User user)
	{
		super(server);
		this.channel = channel;
		this.user = user;
	}
	
	public Channel getChannel()
	{
		return channel;
	}
	
	public User getUser()
	{
		return user;
	}
}
