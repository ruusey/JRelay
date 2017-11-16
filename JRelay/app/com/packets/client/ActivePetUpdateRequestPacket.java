package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ActivePetUpdateRequestPacket extends Packet{
	public byte commandType;
	public int id;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.commandType=in.readByte();
		this.id=in.readInt();
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.commandType);
		out.writeInt(this.id);
		
	}
	
}
