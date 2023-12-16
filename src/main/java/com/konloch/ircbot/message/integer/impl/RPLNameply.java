package com.konloch.ircbot.message.integer.impl;

import com.konloch.ircbot.message.integer.IntegerMessageEvent;
import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

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
		String[] channelThenUsers = split(splitPartMessage[4], " ", 2);
		
		if(channelThenUsers.length <= 0)
			return;
		
		String channelName = channelThenUsers[0];
		String[] users = split(channelThenUsers[1].substring(1), " ");
		
		Room room = server.get(channelName);
		
		if(room != null)
		{
			room.setJoined(true);
			
			//process each nick
			for(String nickname : users)
			{
				//add user to room
				User user = room.add(nickname);
				
				//call on listener event
				server.getBot().getListeners().callOnJoin(room, user);
			}
		}
	}
}
