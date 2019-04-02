package com.move.events;

public class HealthChangedEventArgs {
	private float health;
	public HealthChangedEventArgs(float health) {
		this.health=health;
	}
	public float getHealth() {
		return health;
	}
	public void setHealth(float health) {
		this.health = health;
	}

}
