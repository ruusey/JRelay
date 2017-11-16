package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class DeathPacket extends Packet {
	
	public String accountId;
	public int charId;
	public String killedBy;
	public int obf0;
	public int obf1;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.accountId = in.readUTF();
		this.charId = in.readInt();
		this.killedBy = in.readUTF();
		this.obf0 = in.readInt();
		this.obf1 = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.accountId);
		out.writeInt(this.charId);
		out.writeUTF(this.killedBy);
		out.writeInt(this.obf0);
		out.writeInt(this.obf1);
	}

}
