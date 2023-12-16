package com.konloch.ircbot.server;

import com.konloch.ircbot.event.IRCJoin;
import com.konloch.ircbot.event.IRCLeave;
import com.konloch.ircbot.event.IRCPrivateMessage;
import com.konloch.ircbot.event.IRCRoomMessage;

import java.util.ArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Listeners
{
	private ArrayList<IRCJoin> onJoin = new ArrayList<>();
	private ArrayList<IRCLeave> onLeave = new ArrayList<>();
	private ArrayList<IRCRoomMessage> roomMessages = new ArrayList<>();
	private ArrayList<IRCPrivateMessage> privateMessages = new ArrayList<>();
	
	public void callOnJoin(Room room, User user)
	{
		for(IRCJoin event : onJoin)
			event.join(room, user);
	}
	
	public void callOnLeave(Room room, User user)
	{
		for(IRCLeave event : onLeave)
			event.leave(room, user);
	}
	
	public void callRoomMessage(Room room, User user, String message)
	{
		for(IRCRoomMessage event : roomMessages)
			event.message(room, user, message);
	}
	
	public void callPrivateMessage(User user, String message)
	{
		for(IRCPrivateMessage event : privateMessages)
			event.message(user, message);
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
