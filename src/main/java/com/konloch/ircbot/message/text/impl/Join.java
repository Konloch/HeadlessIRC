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
public class Join implements TextMessageEvent
{
	@Override
	public void handle(Server server, String[] splitPartMessage)
	{
		String nickname = split(splitPartMessage[0].substring(1), "!", 2)[0];
		String roomName = splitPartMessage[2];
		
		Room room = server.get(roomName);
		
		if (room != null)
		{
			room.setJoined(true);
			
			//add user to room
			User user = room.add(nickname);
			
			//call on global event system
			server.getBot().getGlobalEvents().callOnJoin(room, user);
		}
	}
}
