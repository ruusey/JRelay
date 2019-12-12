package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class CreatePacket extends Packet {
	
	public int classType;
	public int skinType;
	public boolean isChallenger;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.classType = in.readUnsignedShort();
		this.skinType = in.readUnsignedShort();
		this.isChallenger=in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.classType);
		out.writeShort(this.skinType);
		out.writeBoolean(this.isChallenger);
	}

}
