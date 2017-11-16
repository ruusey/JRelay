package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class TradeChangedPacket extends Packet {
	
	public boolean[] offer = new boolean[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.offer = new boolean[in.readShort()];
		for (int i = 0; i < this.offer.length; i++) {
			this.offer[i] = in.readBoolean();
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.offer.length);
		for (boolean b: this.offer) {
			out.writeBoolean(b);
		}
	}

}
