package com.konloch.ircbot.message.text;

import com.konloch.ircbot.message.integer.IntegerMessage;
import com.konloch.ircbot.message.text.impl.Join;
import com.konloch.ircbot.message.text.impl.PrivMSG;
import com.konloch.ircbot.message.text.impl.Quit;

import java.util.HashMap;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public enum TextMessage
{
	PRIVMSG("privmsg", new PrivMSG()),
	JOIN("join", new Join()),
	QUIT("quit", new Quit()),
	;
	
	private static final HashMap<String, TextMessage> LOOKUP = new HashMap<>();
	
	static
	{
		for(TextMessage msg : values())
			LOOKUP.put(msg.opcode, msg);
	}
	
	private final String opcode;
	private final TextMessageEvent event;
	
	TextMessage(String opcode, TextMessageEvent event)
	{
		this.opcode = opcode;
		this.event = event;
	}
	
	public String getOpcode()
	{
		return opcode;
	}
	
	public TextMessageEvent getEvent()
	{
		return event;
	}
	
	public static TextMessage opcode(String opcode)
	{
		return LOOKUP.get(opcode);
	}
}
