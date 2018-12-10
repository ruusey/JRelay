package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ActivePetUpdatePacket extends Packet{
	public int id;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.id=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.id);
		
	}
	
}
