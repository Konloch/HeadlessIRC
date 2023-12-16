package com.konloch.ircbot.server;

import com.konloch.ircbot.listener.IRCJoin;
import com.konloch.ircbot.listener.IRCLeave;
import com.konloch.ircbot.listener.IRCPrivateMessage;
import com.konloch.ircbot.listener.IRCChannelMessage;
import com.konloch.ircbot.listener.event.GenericChannelEvent;
import com.konloch.ircbot.listener.event.PrivateMessageEvent;
import com.konloch.ircbot.listener.event.ChannelMessageEvent;

import java.util.ArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Listeners
{
	private final ArrayList<IRCJoin> onJoin = new ArrayList<>();
	private final ArrayList<IRCLeave> onLeave = new ArrayList<>();
	private final ArrayList<IRCChannelMessage> channelMessages = new ArrayList<>();
	private final ArrayList<IRCPrivateMessage> privateMessages = new ArrayList<>();
	
	public void callOnJoin(Channel channel, User user)
	{
		for(IRCJoin event : onJoin)
			event.join(new GenericChannelEvent(channel.getServer(), channel, user));
	}
	
	public void callOnLeave(Channel channel, User user)
	{
		for(IRCLeave event : onLeave)
			event.leave(new GenericChannelEvent(channel.getServer(), channel, user));
	}
	
	public void callChannelMessage(Channel channel, User user, String message)
	{
		for(IRCChannelMessage event : channelMessages)
			event.message(new ChannelMessageEvent(channel.getServer(), channel, user, message));
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
	
	public void onChannelMessage(IRCChannelMessage leave)
	{
		channelMessages.add(leave);
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
	
	public ArrayList<IRCChannelMessage> getChannelMessages()
	{
		return channelMessages;
	}
	
	public ArrayList<IRCPrivateMessage> getPrivateMessages()
	{
		return privateMessages;
	}
}
