package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class ReskinUnlockPacket extends Packet{

   public int skinID;
   public int isPetSkin;

    @Override
    public void parseFromInput(DataInput in) throws IOException {
	this.skinID=in.readInt();
	this.isPetSkin=in.readInt();
	
    }

    @Override
    public void writeToOutput(DataOutput out) throws IOException {
	out.writeInt(this.skinID);
	out.writeInt(this.isPetSkin);
	
    }

}
