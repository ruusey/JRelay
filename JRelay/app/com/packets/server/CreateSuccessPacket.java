package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class CreateSuccessPacket extends Packet {
	
	public int objectId;
	public int charId;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.objectId = in.readInt();
		this.charId = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.objectId);
		out.writeInt(this.charId);
	}

}
