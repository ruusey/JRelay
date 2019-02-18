package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.models.Packet;



public class AOEPacket extends Packet {
	
	public Location pos = new Location();
	public float radius;
	public int damage;
	public int effect;
	public float duration;
	public int origType;
	public int color;
	public boolean armorPierce;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.pos.parseFromInput(in);
		this.radius = in.readFloat();
		this.damage = in.readUnsignedShort();
		this.effect = in.readUnsignedByte();
		this.duration = in.readFloat();
		this.origType = in.readUnsignedShort();
		this.color=in.readInt();
		this.armorPierce=in.readBoolean();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		this.pos.writeToOutput(out);
		out.writeFloat(this.radius);
		out.writeShort(this.damage);
		out.writeByte((byte)this.effect);
		out.writeFloat(this.duration);
		out.writeShort(this.origType);
		out.writeInt(this.color);
		out.writeBoolean(this.armorPierce);
	}

}
