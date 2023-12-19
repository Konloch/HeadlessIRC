package com.konloch.ircbot;

import com.konloch.ircbot.listener.*;
import com.konloch.ircbot.server.Listeners;
import com.konloch.ircbot.server.Server;

import java.io.IOException;
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
	private boolean debug = false;
	private final List<Server> servers = new ArrayList<>();
	private final Listeners listeners = new Listeners();
	
	public IRCBot(String nickname, String client)
	{
		this.nickname = nickname;
		this.client = client;
		
		new Thread(()->
		{
			while(active)
			{
				try
				{
					Thread.sleep(1);
					
					//process each server message queue
					servers.forEach(Server::process);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
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
		
		//start the server thread
		new Thread(server).start();
		
		//return the server instance
		return server;
	}
	
	public void onConnectionEstablished(IRCConnectionEstablished established)
	{
		getListeners().onConnectionEstablished(established);
	}
	
	public void onConnectionLost(IRCConnectionLost lost)
	{
		getListeners().onConnectionLost(lost);
	}
	
	public void onJoin(IRCJoin join)
	{
		getListeners().onJoin(join);
	}
	
	public void onLeave(IRCLeave leave)
	{
		getListeners().onLeave(leave);
	}
	
	public void onChannelMessage(IRCChannelMessage message)
	{
		getListeners().onChannelMessage(message);
	}
	
	public void onPrivateMessage(IRCPrivateMessage message)
	{
		getListeners().onPrivateMessage(message);
	}
	
	public boolean isDebug()
	{
		return debug;
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
	
	public Listeners getListeners()
	{
		return listeners;
	}
	
	/**
	 * Alert that this is a library
	 * @param args program launch arguments
	 */
	public static void main(String[] args)
	{
		throw new RuntimeException("Incorrect usage - for information on how to use this correctly visit https://konloch.com/JavaIRCBot/");
	}
}