package com.models;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UnknownPacket extends Packet {
	
	private byte id;
	private final List<Byte> bytes = new LinkedList<Byte>();

	@Override
	public byte id() {
		return this.id;
	}
	
	public void parseFromInput(DataInput in) throws IOException {
		try {
			while (true) {
				this.bytes.add(in.readByte());
			}
		} catch (Exception e) {}
	}
	
	protected void setId(byte id) {
		this.id = id;
	}

	public void writeToOutput(DataOutput out) throws IOException {
		for (Byte b: this.bytes) {
			out.writeByte(b);
		}
	}

}
