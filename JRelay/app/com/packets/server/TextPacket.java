package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class TextPacket extends Packet {
	
	public String name;
	public int objectId;
	public int numStars;
	public int bubbleTime;
	public String recipient;
	public String text;
	public String cleanText;
	public boolean isSupporter;
	public int starBg;

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.objectId = in.readInt();
		this.numStars = in.readInt();
		this.bubbleTime = in.readUnsignedByte();
		this.recipient = in.readUTF();
		this.text = in.readUTF();
		this.cleanText = in.readUTF();
		this.isSupporter=in.readBoolean();
		this.starBg=in.readInt();
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.name);
		out.writeInt(this.objectId);
		out.writeInt(this.numStars);
		out.writeByte(this.bubbleTime);
		out.writeUTF(this.recipient);
		out.writeUTF(this.text);
		out.writeUTF(this.cleanText);
		out.writeBoolean(this.isSupporter);
		out.writeInt(this.starBg);
	}

}
