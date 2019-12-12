package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class OtherHitPacket extends Packet {
	
	public int time;
	public int bulletId;
	public int objectId;
	public int targetId;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.bulletId = in.readUnsignedByte();
		this.objectId = in.readInt();
		this.targetId = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		out.writeByte(this.bulletId);
		out.writeInt(this.objectId);
		out.writeInt(this.targetId);
	}

}
