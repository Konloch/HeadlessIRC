package com.konloch.ircbot.event;

import com.konloch.ircbot.server.Room;
import com.konloch.ircbot.server.User;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCJoin
{
	void join(Room room, User user);
}
