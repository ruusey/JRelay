package com.move.models;

import com.google.common.collect.ImmutableMap;

public class Key {

	public final static ImmutableMap<String,Integer> codes = ImmutableMap.<String, Integer>builder()
		    .put("w", 0x57) 
		    .put("a", 0x41) 
		    .put("s", 0x53) 
		    .put("d", 0x44) 
		    .put("KeyUp", 0x0101) 
		    .put("KeyDown", 0x0100)
		    .build();

}
