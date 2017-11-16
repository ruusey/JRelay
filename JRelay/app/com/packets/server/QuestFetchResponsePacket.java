package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class QuestFetchResponsePacket extends Packet{
	public int tier;
	public String goal;
	public String description;
	public String image;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.tier=in.readInt();
		this.goal=in.readUTF();
		this.description=in.readUTF();
		this.image=in.readUTF();
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.tier);
		out.writeUTF(this.goal);
		out.writeUTF(this.description);
		out.writeUTF(this.image);
		
	}
	
	
}
