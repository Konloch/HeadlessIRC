package com.konloch.ircbot.message.integer.impl;

import com.konloch.ircbot.message.integer.IntegerMessageEvent;
import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;

import java.util.Arrays;

import static com.konloch.util.FastStringUtils.split;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class RPLNameply implements IntegerMessageEvent
{
	
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		if(splitPartMessage.length <= 4)
		{
			//TODO figure out what's going on here when we get a chance
			System.err.println("ERROR: " + Arrays.toString(splitPartMessage));
			return;
		}
		
		String[] channelThenUsers = split(splitPartMessage[4], " ", 2);
		
		if(channelThenUsers.length <= 0)
			return;
		
		String channelName = channelThenUsers[0];
		String[] users = split(channelThenUsers[1].substring(1), " ");
		
		Channel channel = server.getChannel(channelName);
		
		if(channel != null)
		{
			channel.setJoined(true);
			
			//process each nick
			for(String nickname : users)
			{
				//add user to channel
				channel.add(nickname);
			}
		}
	}
}
