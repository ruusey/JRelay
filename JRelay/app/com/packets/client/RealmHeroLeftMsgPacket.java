package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class RealmHeroLeftMsgPacket extends Packet {

	public int heroesLeft;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.heroesLeft=in.readInt();

	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.heroesLeft);
	}

}
