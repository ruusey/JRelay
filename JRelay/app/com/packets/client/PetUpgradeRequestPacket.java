package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;

public class PetUpgradeRequestPacket extends Packet {
	
	public static final int UPGRADE_PET_YARD = 1;
    public static final int FEED_PET = 2;
    public static final int FUSE_PET = 3;
	
	public byte commandId;
	public int pidOne;
	public int pidTwo;
	public int objectId;
	public SlotObject slot= new SlotObject();
	public byte paymentType;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.commandId = in.readByte();
		this.pidOne = in.readInt();
		this.pidTwo = in.readInt();
		this.objectId = in.readInt();
		this.slot.parseFromInput(in);
		this.paymentType = in.readByte();

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.commandId);
		out.writeInt(this.pidOne);
		out.writeInt(this.pidTwo);
		out.writeInt(this.objectId);
		this.slot.writeToOutput(out);
		out.writeByte(this.paymentType);

	}
}
