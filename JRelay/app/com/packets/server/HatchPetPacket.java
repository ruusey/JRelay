package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class HatchPetPacket extends Packet{
	public String name;
	public int skin;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.name=in.readUTF();
		this.skin=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.name);
		out.writeInt(this.skin);
		
	}

}
