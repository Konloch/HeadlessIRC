package com.konloch.ircbot.message.text.impl;

import com.konloch.ircbot.message.text.TextMessageEvent;
import com.konloch.ircbot.server.Room;
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
		
		for(Room room : server.getRooms())
		{
			//remove user from room
			User user = room.remove(nickname);
			
			if(user != null)
			{
				//call on global event system
				server.getBot().getGlobalEvents().callOnLeave(room, user);
				break;
			}
		}
	}
}
