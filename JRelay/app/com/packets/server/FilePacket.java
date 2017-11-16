package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class FilePacket extends Packet {

	public String name;
	public byte[] bytes = new byte[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.bytes = new byte[in.readInt()];
		in.readFully(this.bytes);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.name);
		out.writeInt(this.bytes.length);
		out.write(this.bytes);
	}

}
