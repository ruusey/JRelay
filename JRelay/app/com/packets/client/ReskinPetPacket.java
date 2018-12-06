package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;


public class ReskinPetPacket extends Packet {

	public int petId;
	public int skinType;
	 public SlotObject item;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.petId = in.readInt();
		this.skinType = in.readInt();
		this.item.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.petId);
		out.writeInt(this.skinType);
		item.writeToOutput(out);
	}

}
