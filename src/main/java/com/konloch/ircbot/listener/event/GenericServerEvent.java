package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Server;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class GenericServerEvent
{
	private final Server server;
	
	public GenericServerEvent(Server server)
	{
		this.server = server;
	}
	
	public Server getServer()
	{
		return server;
	}
}
