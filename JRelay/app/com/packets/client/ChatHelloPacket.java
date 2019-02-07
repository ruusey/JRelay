package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ChatHelloPacket extends Packet {
	public String token;
	public String accountId;
	
	public ChatHelloPacket() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.accountId = in.readUTF();
		this.token = in.readUTF();
        
        

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.accountId);
		out.writeUTF(this.token);
		

	}

}
