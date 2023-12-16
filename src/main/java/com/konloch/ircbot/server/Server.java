package com.konloch.ircbot.server;

import com.konloch.ircbot.IRCBot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Server
{
	private final IRCBot bot;
	private final String server;
	private final int port;
	private boolean active = true;
	
	private final List<Room> rooms = new ArrayList<>();
	
	public Server(IRCBot bot, String server, int port)
	{
		this.bot = bot;
		this.server = server;
		this.port = port;
	}
	
	public Room join(String channel)
	{
		Room room = new Room(this, channel);
		rooms.add(room);
		return room;
	}
	
	public List<Room> getRooms()
	{
		return rooms;
	}
	
	public IRCBot getBot()
	{
		return bot;
	}
}