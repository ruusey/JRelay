package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class LoadPacket extends Packet {
	
	public int charId;
	public boolean isFromArena;
	public boolean isChallenger;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.charId = in.readInt();
		this.isFromArena = in.readBoolean();
		this.isChallenger=in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.charId);
		out.writeBoolean(this.isFromArena);
		out.writeBoolean(this.isChallenger);
	}

}
