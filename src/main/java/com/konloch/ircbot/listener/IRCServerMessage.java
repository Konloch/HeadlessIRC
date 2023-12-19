package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.ServerMessageEvent;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCServerMessage
{
	void message(ServerMessageEvent event);
}
