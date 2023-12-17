package com.konloch.ircbot.message.text.impl;

import com.konloch.ircbot.message.text.TextMessageEvent;
import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

import java.util.Arrays;

import static com.konloch.util.FastStringUtils.split;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class PrivMSG implements TextMessageEvent
{
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
		String[] locations = split(splitPartMessage[2], ",");
		String msg = splitPartMessage[3].substring(1);
		
		for(String location : locations)
		{
			if (location.startsWith("#"))
			{
				Channel channel = server.get(location);
				
				if (channel != null)
				{
					channel.setJoined(true);
					
					User user = channel.get(nickname);
					
					//create a temporary user object if we can't retrieve it from the name list
					//TODO not entirely sure why this is happening, the RPL_NAMEPLY should handle this for us
					if (user == null)
						user = new User(server, nickname);
					
					//call on listener event
					server.getBot().getListeners().callChannelMessage(channel, user, msg);
				}
			}
			else
			{
				//create new user object
				User user = new User(server, nickname);
				
				//call on listener event
				server.getBot().getListeners().callPrivateMessage(user, msg);
			}
		}
	}
}
