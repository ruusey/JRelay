package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Entity implements IData {
	
	public short objectType;
	public Status status = new Status();
	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.objectType = in.readShort();
		this.status.parseFromInput(in);
	}
	
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.objectType);
		this.status.writeToOutput(out);
	}
	
}
