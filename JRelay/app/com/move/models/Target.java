package com.move.models;

import com.data.shared.Location;

public class Target {
	private int objectId;
	private String name;
	private Location position;
	
	public Target(int objectId, String name, Location position) {
		super();
		this.objectId = objectId;
		this.name = name;
		this.position = position;
		
		
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getPosition() {
		return position;
	}

	public void setPosition(Location position) {
		this.position = position;
	}

	public void updatePosition(Location position) {
		this.position = position;
	}

	public float DistanceTo(Target target) {
		return position.distanceTo(target.position);
	}

}
