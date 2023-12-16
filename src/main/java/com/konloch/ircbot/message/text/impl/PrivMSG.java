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
public class PrivMSG implements TextMessageEvent
{
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
		String location = splitPartMessage[2];
		String msg = splitPartMessage[3].substring(1);
		
		if(location.startsWith("#"))
		{
			Room room = server.get(location);
			
			if (room != null)
			{
				room.setJoined(true);
				
				User user = room.get(nickname);
				
				//call on global event system
				server.getBot().getGlobalEvents().callRoomMessage(room, user, msg);
			}
		}
		else
		{
			//TODO handle pm
		}
	}
}
