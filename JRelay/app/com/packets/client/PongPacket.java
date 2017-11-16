package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class PongPacket extends Packet {
	
	public int serial;
	public int time;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.serial = in.readInt();
		this.time = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.serial);
		out.writeInt(this.time);
	}

}
