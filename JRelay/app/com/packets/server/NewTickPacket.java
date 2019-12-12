package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Status;
import com.models.Packet;



public class NewTickPacket extends Packet {
	
	public int tickId;
	public int tickTime;
	public Status[] statuses = new Status[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.tickId = in.readInt();
		this.tickTime = in.readInt();
		this.statuses = new Status[in.readShort()];
		for (int i = 0; i < this.statuses.length; i++) {
			Status status = new Status();
			status.parseFromInput(in);
			this.statuses[i] = status;
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.tickId);
		out.writeInt(this.tickTime);
		out.writeShort(this.statuses.length);
		for (Status status: this.statuses) {
			status.writeToOutput(out);
		}
	}

}
