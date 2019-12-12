package com.models;

import com.objects.IntObject;

public class Tile extends IntObject{
	public boolean noWalk;
	public float speed;
	public boolean Sink;
	public int minDamage;
	public int maxDamage;
	
	public Tile(int id, String name, boolean noWalk, float speed, boolean sink,
			int minDamage, int maxDamage) {
		super(id, name);
		this.noWalk = noWalk;
		this.speed = speed;
		Sink = sink;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
	}
	
}
