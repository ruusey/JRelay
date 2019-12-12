package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.BitmapData;
import com.models.Packet;



public class PicPacket extends Packet {
	
	public BitmapData bitmapData = new BitmapData();

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.bitmapData.parseFromInput(in);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		this.bitmapData.writeToOutput(out);
	}

}
