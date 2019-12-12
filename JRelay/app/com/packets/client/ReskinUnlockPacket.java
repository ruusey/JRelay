package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;

public class ReskinUnlockPacket extends Packet{
	public int petId;
	public int newSkin;
	public SlotObject slot;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.petId=in.readInt();
		this.newSkin=in.readInt();
		this.slot.parseFromInput(in);
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(petId);
		out.writeInt(newSkin);
		this.slot.writeToOutput(out);
		
	}
	
}
