package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class DamagePacket extends Packet {
	
	public int targetId;
	public int[] effects = new int[0];
	public int damageAmount;
	public boolean kill;
	public boolean piercesArmor;
	public int bulletId;
	public int objectId;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.targetId = in.readInt();
		this.effects = new int[in.readUnsignedByte()];
		for (int i = 0; i < this.effects.length; i++) {
			this.effects[i] = in.readUnsignedByte();
		}
		this.damageAmount = in.readUnsignedShort();
		this.kill = in.readBoolean();
		this.piercesArmor=in.readBoolean();
		this.bulletId = in.readUnsignedByte();
		this.objectId = in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.targetId);
		out.writeByte(this.effects.length);
		for (int effect: this.effects) {
			out.writeByte(effect);
		}
		out.writeShort(this.damageAmount);
		out.writeBoolean(this.kill);
		out.writeBoolean(this.piercesArmor);
		out.writeByte(this.bulletId);
		out.writeInt(this.objectId);
	}

}
