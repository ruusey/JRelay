package com.move.events;

import com.move.models.Keys;

public class KeyEventArgs {
	private Keys key;
    private boolean value;
	
	public KeyEventArgs(Keys key, boolean value) {
		super();
		this.key = key;
		this.value = value;
	}
	public Keys getKey() {
		return key;
	}
	public void setKey(Keys key) {
		this.key = key;
	}
	public boolean getValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}

}
