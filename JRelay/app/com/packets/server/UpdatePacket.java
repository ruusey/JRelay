package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Entity;
import com.data.shared.Tile;
import com.models.Packet;


public class UpdatePacket extends Packet {
	
	public Tile[] tiles = new Tile[0];
	public Entity[] newObjs = new Entity[0];
	public int[] drops = new int[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.tiles = new Tile[in.readShort()];
		for (int i = 0; i < this.tiles.length; i++) {
			Tile tile = new Tile();
			tile.parseFromInput(in);
			this.tiles[i] = tile;
		}
		this.newObjs = new Entity[in.readShort()];
		for (int i = 0; i < this.newObjs.length; i++) {
			Entity Entity = new Entity();
			Entity.parseFromInput(in);
			this.newObjs[i] = Entity;
		}
		this.drops = new int[in.readShort()];
		for (int i = 0; i < this.drops.length; i++) {
			this.drops[i] = in.readInt();
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.tiles.length);
		for (Tile tile: this.tiles) {
			tile.writeToOutput(out);
		}
		out.writeShort(this.newObjs.length);
		for (Entity obj: this.newObjs) {
			obj.writeToOutput(out);
		}
		out.writeShort(this.drops.length);
		for (int drop: this.drops) {
			out.writeInt(drop);
		}
	}

}
