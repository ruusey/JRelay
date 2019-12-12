package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class DeletePetPacket extends Packet{
	public int petId;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.petId=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.petId);
		
	}

}
