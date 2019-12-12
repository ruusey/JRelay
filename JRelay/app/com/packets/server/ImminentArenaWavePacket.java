package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ImminentArenaWavePacket extends Packet {
	public int time;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time=in.readInt();

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(time);

	}

}
