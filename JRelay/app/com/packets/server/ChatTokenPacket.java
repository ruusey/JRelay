package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ChatTokenPacket extends Packet {
	public String token;
	public String host;
	public int port;
	public ChatTokenPacket() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.token = in.readUTF();
        this.host = in.readUTF();
        this.port = in.readInt();

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.token);
		out.writeUTF(this.host);
		out.writeInt(this.port);

	}

}
