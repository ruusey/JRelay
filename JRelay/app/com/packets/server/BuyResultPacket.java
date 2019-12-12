package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class BuyResultPacket extends Packet {
	
	public int result;
	public String resultString;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.result = in.readInt();
		this.resultString = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.result);
		out.writeUTF(this.resultString);
	}

}
