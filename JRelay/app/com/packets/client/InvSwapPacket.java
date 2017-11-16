package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.data.shared.SlotObject;
import com.models.Packet;


public class InvSwapPacket extends Packet {
	
	public int time;
	public Location position = new Location();
	public SlotObject slotObject1 = new SlotObject();
	public SlotObject slotObject2 = new SlotObject();

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.position.parseFromInput(in);
		this.slotObject1.parseFromInput(in);
		this.slotObject2.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		this.position.writeToOutput(out);
		this.slotObject1.writeToOutput(out);
		this.slotObject2.writeToOutput(out);
	}

}
