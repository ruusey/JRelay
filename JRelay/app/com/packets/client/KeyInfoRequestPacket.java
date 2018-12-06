package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class KeyInfoRequestPacket extends Packet {



	private int itemType;








	@Override

	public void parseFromInput(DataInput in) throws IOException {

		this.itemType = in.readInt();

	}



	@Override

	public void writeToOutput(DataOutput out) throws IOException {

		out.writeInt(this.itemType);

	}



}

