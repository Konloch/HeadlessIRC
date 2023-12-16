package com.konloch.ircbot.message.integer;

import com.konloch.ircbot.server.Server;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IntegerMessageEvent
{
	void handle(Server server, String[] splitPartMessage);
}
