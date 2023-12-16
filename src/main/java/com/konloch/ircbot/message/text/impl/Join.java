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
public class Join implements TextMessageEvent
{
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
		String channelName = splitPartMessage[2];
		
		Channel channel = server.get(channelName);
		
		if (channel != null)
		{
			channel.setJoined(true);
			
			//add user to channel
			User user = channel.add(nickname);
			
			//call on listener event
			server.getBot().getListeners().callOnJoin(channel, user);
		}
	}
}
