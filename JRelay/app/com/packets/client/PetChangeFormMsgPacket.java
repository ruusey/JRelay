package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;

public class PetChangeFormMsgPacket extends Packet{
	public int id;
	public int newType;
	public SlotObject slot;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.id=in.readInt();
		this.newType=in.readInt();
		slot.parseFromInput(in);
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.id);
		out.writeInt(this.newType);
		slot.writeToOutput(out);
		
	}
	
	
}
