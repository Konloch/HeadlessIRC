package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.RoomMessageEvent;
import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCRoomMessage
{
	void message(RoomMessageEvent event);
}
