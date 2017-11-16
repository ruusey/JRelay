package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class LocationRecord extends Location {
	
	public int time;
	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		super.parseFromInput(in);
	}
	
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		super.writeToOutput(out);
	}

}
