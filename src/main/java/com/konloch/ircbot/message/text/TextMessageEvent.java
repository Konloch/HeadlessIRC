package com.konloch.ircbot.message.text;

import com.konloch.ircbot.server.Server;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface TextMessageEvent
{
	void handle(Server server, String[] splitPartMessage);
}