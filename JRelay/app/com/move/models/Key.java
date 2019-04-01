package com.move.models;

import com.google.common.collect.ImmutableMap;

public class Key {

	final static ImmutableMap<String,Integer> codes = ImmutableMap.<String, Integer>builder()
		    .put("W", 0x57) 
		    .put("A", 0x41) 
		    .put("S", 0x53) 
		    .put("D", 0x44) 
		    .put("KeyUp", 0x0101) 
		    .put("KeyDown", 0x0100)
		    .build();

}
