package com.konloch.ircbot.listener.event;

import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class RoomMessageEvent extends GenericRoomEvent
{
	private final String message;
	
	public RoomMessageEvent(Server server, Room room, User user, String message)
	{
		super(server, room, user);
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}
