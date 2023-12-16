package com.konloch.ircbot.message.text.impl;

import com.konloch.ircbot.message.text.TextMessageEvent;
import com.konloch.ircbot.server.Channel;
import com.konloch.ircbot.server.Server;
import com.konloch.ircbot.server.User;

import static com.konloch.util.FastStringUtils.split;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public class Quit implements TextMessageEvent
{
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
		
		for(Channel channel : server.getChannels())
		{
			//remove user from channel
			User user = channel.remove(nickname);
			
			if(user != null)
			{
				//call on listener event
				server.getBot().getListeners().callOnLeave(channel, user);
				break;
			}
		}
	}
}
