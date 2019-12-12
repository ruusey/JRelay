package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ClaimLoginRewardMsgPacket extends Packet{
	public String key;
	public String type;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.key=in.readUTF();
		this.type=in.readUTF();
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.key);
		out.writeUTF(this.type);
		
	}
	
}
