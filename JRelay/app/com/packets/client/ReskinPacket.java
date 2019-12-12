package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class ReskinPacket extends Packet {
	
	public int skinID;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.skinID = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.skinID);
	}

}
