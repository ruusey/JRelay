package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.data.shared.LocationRecord;
import com.models.Packet;



public class MovePacket extends Packet {
	
	public int tickId;
	public int time;
	public Location newPosition = new Location();
	public LocationRecord[] records = new LocationRecord[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.tickId = in.readInt();
		this.time = in.readInt();
		this.newPosition.parseFromInput(in);
		this.records = new LocationRecord[in.readShort()];
		for (int i = 0; i < this.records.length; i++) {
			LocationRecord record = new LocationRecord();
			record.parseFromInput(in);
			this.records[i] = record;
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.tickId);
		out.writeInt(this.time);
		this.newPosition.writeToOutput(out);
		out.writeShort(this.records.length);
		for (LocationRecord record: this.records) {
			record.writeToOutput(out);
		}
	}

}
