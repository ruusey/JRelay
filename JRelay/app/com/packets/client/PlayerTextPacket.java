package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class PlayerTextPacket extends Packet {
	
	public String text;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.text = in.readUTF();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.text);
	}

}
