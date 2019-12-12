package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.data.shared.SlotObject;
import com.models.Packet;


public class UseItemPacket extends Packet {
	
	public int time;
	public SlotObject slotObject = new SlotObject();
	public Location itemUsePos = new Location();
	public byte useType;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.time = in.readInt();
		this.slotObject.parseFromInput(in);
		this.itemUsePos.parseFromInput(in);
		this.useType = in.readByte();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.time);
		this.slotObject.writeToOutput(out);
		this.itemUsePos.writeToOutput(out);
		out.writeByte(useType);
	}

}
