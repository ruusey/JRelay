package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class SetConditionPacket extends Packet {
	
	public int conditionEffect;
	public float conditionDuration;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.conditionEffect = in.readUnsignedByte();
		this.conditionDuration = in.readFloat();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.conditionEffect);
		out.writeFloat(this.conditionDuration);
	}

}
