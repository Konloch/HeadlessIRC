package com.konloch.ircbot.listener;

import com.konloch.ircbot.listener.event.GenericServerEvent;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public interface IRCConnectionLost
{
	void lost(GenericServerEvent event);
}
