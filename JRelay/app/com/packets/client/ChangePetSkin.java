package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class ChangePetSkin extends Packet {

	public int petId;
	public int skinType;
	

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.petId = in.readInt();
		this.skinType = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.petId);
		out.writeInt(this.skinType);
	}

}
