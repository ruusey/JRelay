package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Tile implements IData {
	
	public short x;
	public short y;
	public int type;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.x = in.readShort();
		this.y = in.readShort();
		this.type = in.readUnsignedShort();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.x);
		out.writeShort(this.y);
		out.writeShort(this.type);
	}

}
