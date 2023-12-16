package com.konloch.ircbot;

import com.konloch.ircbot.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class IRCBot
{
	private boolean active = true;
	private String nickname;
	private String client;
	private final List<Server> servers = new ArrayList<>();
	
	public IRCBot(String nickname, String client)
	{
		this.nickname = nickname;
		this.client = client;
	}
	
	public Server join(String serverAddress)
	{
		int port = 6667;
		
		if(serverAddress.contains(":"))
		{
			String[] split = serverAddress.split(":", 2);
			port = Integer.parseInt(split[1]);
			serverAddress = split[0];
		}
		
		//setup the server
		Server server = new Server(this, serverAddress, port);
		servers.add(server);
		
		//return the server instance
		return server;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public String getNickname()
	{
		return nickname;
	}
	
	public String getClient()
	{
		return client;
	}
}