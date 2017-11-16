package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class LoginRewardMsgPacket extends Packet{
	public int id;
	public int quantity;
	public int gold;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.id=in.readInt();
		this.quantity=in.readInt();
		this.gold=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.id);
		out.writeInt(this.quantity);
		out.writeInt(this.gold);
		
	}

}
