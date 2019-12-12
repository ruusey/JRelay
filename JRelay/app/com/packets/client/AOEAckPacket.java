package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.models.Packet;


public class AOEAckPacket extends Packet {
	
	public int time;
	public Location position = new Location();

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.position.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		this.position.writeToOutput(out);
	}

}
