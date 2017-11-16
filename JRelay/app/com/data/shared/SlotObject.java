package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class SlotObject implements IData {
	
	public int objectId;
	public byte slotId;
	public int objectType;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.objectId = in.readInt();
		this.slotId = in.readByte();
		this.objectType = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.objectId);
		out.writeByte(this.slotId);
		out.writeInt(this.objectType);
	}

}
