package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class GenericRoomEvent extends GenericServerEvent
{
	private final Room room;
	private final User user;
	
	public GenericRoomEvent(Server server, Room room, User user)
	{
		super(server);
		this.room = room;
		this.user = user;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public User getUser()
	{
		return user;
	}
}
