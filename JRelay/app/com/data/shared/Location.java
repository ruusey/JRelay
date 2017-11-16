package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Location implements IData {
	
	public float x, y;
	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.x = in.readFloat();
		this.y = in.readFloat();
	}
	
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeFloat(this.x);
		out.writeFloat(this.y);
	}
	
	public float distanceSquaredTo(Location location) {
		float dx = location.x - this.x;
		float dy = location.y - this.y;
		return dx * dx + dy * dy;
	}
	
	public float distanceTo(Location location) {
		return (float) Math.sqrt(this.distanceSquaredTo(location));
	}

}
