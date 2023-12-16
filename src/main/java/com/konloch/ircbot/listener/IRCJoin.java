package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.GenericChannelEvent;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCJoin
{
	void join(GenericChannelEvent event);
}
