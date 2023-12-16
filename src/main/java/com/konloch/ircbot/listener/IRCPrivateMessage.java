package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.PrivateMessageEvent;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCPrivateMessage
{
	void message(PrivateMessageEvent event);
}
