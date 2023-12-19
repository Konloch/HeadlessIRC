package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Server;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class ServerMessageEvent
{
	private final Server server;
	private final String message;
	private final boolean handled;
	
	public ServerMessageEvent(Server server, String message, boolean handled)
	{
		this.server = server;
		this.message = message;
		this.handled = handled;
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public boolean isHandled()
	{
		return handled;
	}
}
