package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class TradeDonePacket extends Packet {
	
	public int code;
	public String description;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.code = in.readInt();
		this.description = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.code);
		out.writeUTF(this.description);
	}

}
