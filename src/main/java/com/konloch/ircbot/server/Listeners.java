package com.konloch.ircbot.server;

import com.konloch.ircbot.listener.*;
import com.konloch.ircbot.listener.event.*;

import java.util.ArrayList;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Listeners
{
	private final ArrayList<IRCConnectionEstablished> onConnectionEstablished = new ArrayList<>();
	private final ArrayList<IRCConnectionLost> onConnectionLost = new ArrayList<>();
	private final ArrayList<IRCJoin> onJoin = new ArrayList<>();
	private final ArrayList<IRCLeave> onLeave = new ArrayList<>();
	private final ArrayList<IRCServerMessage> serverMessages = new ArrayList<>();
	private final ArrayList<IRCOutboundMessage> outboundMessages = new ArrayList<>();
	private final ArrayList<IRCChannelMessage> channelMessages = new ArrayList<>();
	private final ArrayList<IRCPrivateMessage> privateMessages = new ArrayList<>();
	
	public void callOnConnectionEstablished(Server server)
	{
		GenericServerEvent event = new GenericServerEvent(server);
		for(IRCConnectionEstablished listener : onConnectionEstablished)
			listener.established(event);
	}
	
	public void callOnConnectionLost(Server server)
	{
		GenericServerEvent event = new GenericServerEvent(server);
		for(IRCConnectionLost listener : onConnectionLost)
			listener.lost(event);
	}
	
	public void callOnJoin(Channel channel, User user)
	{
		GenericChannelEvent event = new GenericChannelEvent(channel.getServer(), channel, user);
		for(IRCJoin listener : onJoin)
			listener.join(event);
	}
	
	public void callOnLeave(Channel channel, User user)
	{
		GenericChannelEvent event = new GenericChannelEvent(channel.getServer(), channel, user);
		for(IRCLeave listener : onLeave)
			listener.leave(event);
	}
	
	public void callServerMessage(Server server, String message, boolean handled)
	{
		ServerMessageEvent event = new ServerMessageEvent(server, message, handled);
		for(IRCServerMessage listener : serverMessages)
			listener.message(event);
	}
	
	public void callOutboundMessage(Server server, String message, boolean handled)
	{
		ServerMessageEvent event = new ServerMessageEvent(server, message, handled);
		for(IRCOutboundMessage listener : outboundMessages)
			listener.message(event);
	}
	
	public void callChannelMessage(Channel channel, User user, String message)
	{
		ChannelMessageEvent event = new ChannelMessageEvent(channel.getServer(), channel, user, message);
		for(IRCChannelMessage listener : channelMessages)
			listener.message(event);
	}
	
	public void callPrivateMessage(User user, String message)
	{
		PrivateMessageEvent event = new PrivateMessageEvent(user.getServer(), user, message);
		for(IRCPrivateMessage listener : privateMessages)
			listener.message(event);
	}
	
	public void onConnectionEstablished(IRCConnectionEstablished established)
	{
		onConnectionEstablished.add(established);
	}
	
	public void onConnectionLost(IRCConnectionLost lost)
	{
		onConnectionLost.add(lost);
	}
	
	public void onJoin(IRCJoin join)
	{
		onJoin.add(join);
	}
	
	public void onLeave(IRCLeave leave)
	{
		onLeave.add(leave);
	}
	
	public void onServerMessage(IRCServerMessage message)
	{
		serverMessages.add(message);
	}
	
	public void onOutboundMessage(IRCOutboundMessage message)
	{
		outboundMessages.add(message);
	}
	
	public void onChannelMessage(IRCChannelMessage message)
	{
		channelMessages.add(message);
	}
	
	public void onPrivateMessage(IRCPrivateMessage message)
	{
		privateMessages.add(message);
	}
	
	public ArrayList<IRCConnectionEstablished> getOnConnectionEstablished()
	{
		return onConnectionEstablished;
	}
	
	public ArrayList<IRCConnectionLost> getOnConnectionLost()
	{
		return onConnectionLost;
	}
	
	public ArrayList<IRCJoin> getOnJoin()
	{
		return onJoin;
	}
	
	public ArrayList<IRCLeave> getOnLeave()
	{
		return onLeave;
	}
	
	public ArrayList<IRCServerMessage> getServerMessages()
	{
		return serverMessages;
	}
	
	public ArrayList<IRCOutboundMessage> getOutboundMessages()
	{
		return outboundMessages;
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
