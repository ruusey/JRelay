package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class PlaySoundPacket extends Packet {
	
	public int ownerId;
	public int soundId;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.ownerId = in.readInt();
		this.soundId = in.readUnsignedByte();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.ownerId);
		out.writeByte(this.soundId);
	}

}
