package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class QuestRedeemResponsePacket extends Packet{
	public boolean success;
	public String message;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.success=in.readBoolean();
		this.message=in.readUTF();
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeBoolean(this.success);
		out.writeUTF(this.message);
		
	}
	
}
