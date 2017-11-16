package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class PetYardUpdatePacket extends Packet{
	public int type;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.type=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.type);
		
	}
	
}
