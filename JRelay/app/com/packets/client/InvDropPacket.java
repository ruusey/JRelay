package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;


public class InvDropPacket extends Packet {
	
	public SlotObject slotObject = new SlotObject();

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.slotObject.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		this.slotObject.writeToOutput(out);
	}

}
