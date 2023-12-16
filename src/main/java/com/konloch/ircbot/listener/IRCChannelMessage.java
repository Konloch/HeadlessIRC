package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.ChannelMessageEvent;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCChannelMessage
{
	void message(ChannelMessageEvent event);
}
