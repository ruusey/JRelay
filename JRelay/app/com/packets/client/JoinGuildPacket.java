package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class JoinGuildPacket extends Packet {
	
	public String guildName;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.guildName = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.guildName);
	}

}
