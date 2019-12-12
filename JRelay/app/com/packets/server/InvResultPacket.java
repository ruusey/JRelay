package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class InvResultPacket extends Packet {
	
	public int result;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.result = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.result);
	}

}
