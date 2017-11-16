package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ArenaDeathPacket extends Packet{
	public int cost;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.cost=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.cost);
		
	}
	
}
