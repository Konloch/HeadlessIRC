package com.konloch.ircbot.server;

import com.konloch.ircbot.listener.IRCJoin;
import com.konloch.ircbot.listener.IRCLeave;
import com.konloch.ircbot.listener.IRCPrivateMessage;
import com.konloch.ircbot.listener.IRCRoomMessage;
import com.konloch.ircbot.listener.event.GenericRoomEvent;
import com.konloch.ircbot.listener.event.PrivateMessageEvent;
import com.konloch.ircbot.listener.event.RoomMessageEvent;

import java.util.ArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Listeners
{
	private final ArrayList<IRCJoin> onJoin = new ArrayList<>();
	private final ArrayList<IRCLeave> onLeave = new ArrayList<>();
	private final ArrayList<IRCRoomMessage> roomMessages = new ArrayList<>();
	private final ArrayList<IRCPrivateMessage> privateMessages = new ArrayList<>();
	
	public void callOnJoin(Room room, User user)
	{
		for(IRCJoin event : onJoin)
			event.join(new GenericRoomEvent(room.getServer(), room, user));
	}
	
	public void callOnLeave(Room room, User user)
	{
		for(IRCLeave event : onLeave)
			event.leave(new GenericRoomEvent(room.getServer(), room, user));
	}
	
	public void callRoomMessage(Room room, User user, String message)
	{
		for(IRCRoomMessage event : roomMessages)
			event.message(new RoomMessageEvent(room.getServer(), room, user, message));
	}
	
	public void callPrivateMessage(User user, String message)
	{
		for(IRCPrivateMessage event : privateMessages)
			event.message(new PrivateMessageEvent(user.getServer(), user, message));
	}
	
	public void onJoin(IRCJoin join)
	{
		onJoin.add(join);
	}
	
	public void onLeave(IRCLeave leave)
	{
		onLeave.add(leave);
	}
	
	public void onRoomMessage(IRCRoomMessage leave)
	{
		roomMessages.add(leave);
	}
	
	public void onPrivateMessage(IRCPrivateMessage leave)
	{
		privateMessages.add(leave);
	}
	
	public ArrayList<IRCJoin> getOnJoin()
	{
		return onJoin;
	}
	
	public ArrayList<IRCLeave> getOnLeave()
	{
		return onLeave;
	}
	
	public ArrayList<IRCRoomMessage> getRoomMessages()
	{
		return roomMessages;
	}
	
	public ArrayList<IRCPrivateMessage> getPrivateMessages()
	{
		return privateMessages;
	}
}
