package com.konloch.ircbot.message.integer;

import com.konloch.ircbot.message.integer.impl.RPLNameply;

import java.util.HashMap;

/**
 * @author Konloch
 * @since 12/15/2023
 */
public enum IntegerMessage
{
	RPL_NAMREPLY(353, new RPLNameply()),
	;
	
	private static final HashMap<Integer, IntegerMessage> LOOKUP = new HashMap<>();
	
	static
	{
		for(IntegerMessage msg : values())
			LOOKUP.put(msg.opcode, msg);
	}
	
	private final int opcode;
	private final IntegerMessageEvent event;
	
	IntegerMessage(int opcode, IntegerMessageEvent event)
	{
		this.opcode = opcode;
		this.event = event;
	}
	
	public int getOpcode()
	{
		return opcode;
	}
	
	public IntegerMessageEvent getEvent()
	{
		return event;
	}
	
	public static IntegerMessage opcode(int opcode)
	{
		return LOOKUP.get(opcode);
	}
}
