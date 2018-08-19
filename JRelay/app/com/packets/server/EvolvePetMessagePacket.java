package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class EvolvePetMessagePacket extends Packet{
	public int petId;
	public int oldSkin;
	public int newSkin;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.petId=in.readInt();
		this.oldSkin=in.readInt();
		this.newSkin=in.readInt();
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(petId);
		out.writeInt(oldSkin);
		out.writeInt(newSkin);
	}
	
	
}
