package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.models.Packet;

public class EnemyShootPacket extends Packet{

	public byte bulletId;
	public int ownerId;
	public int bulletType;
	public Location startingPos = new Location();
	public float angle;
	public int damage;
	public byte numShots;
	public float angleInc;
	

	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.bulletId = in.readByte();
		this.ownerId = in.readInt();
		this.bulletType = in.readByte();
		this.startingPos.parseFromInput(in);
		this.angle = in.readFloat();
		this.damage = in.readShort();
		try{
			 this.numShots = in.readByte();
	         this.angleInc = in.readFloat();
		}catch(Exception e){
			this.numShots = 1;
            this.angleInc = 0;
		}
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(this.bulletId);
		out.writeInt(this.ownerId);
		out.writeByte(this.bulletType);
		this.startingPos.writeToOutput(out);
		out.writeFloat(this.angle);
		out.writeShort(this.damage);
		
		if(this.numShots!=1 && this.angleInc!=0){
			out.writeByte(this.numShots);
			out.writeFloat(this.angleInc);
		}
		
		
		
		
		
	}

}
