package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class MapInfoPacket extends Packet {
	
	public int width;
	public int height;
	public String name;
	public String displayName;
	public String realmName;
	public int difficulty;
	public int fp;
	public int background;
	public boolean allowPlayerTeleport;
	public boolean showDisplays;
	public String[] clientXML = new String[0];
	public String[] extraXML = new String[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.width = in.readInt();
		this.height = in.readInt();
		this.name = in.readUTF();
		this.displayName = in.readUTF();
		this.realmName= in.readUTF();
		this.fp = in.readInt(); // TODO: fp is supposed to be unsigned
		this.background = in.readInt();
		this.difficulty = in.readInt();
		this.allowPlayerTeleport = in.readBoolean();
		this.showDisplays = in.readBoolean();
		this.clientXML = new String[in.readShort()];
		 for (int i = 0; i < this.clientXML.length; i++) {
		      this.clientXML[i] = in.readUTF();
		    }
		this.extraXML = new String[in.readShort()];
		for (int i = 0; i < this.extraXML.length; i++) {
		    this.extraXML[i] = in.readUTF();
		}
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.width);
		out.writeInt(this.height);
		out.writeUTF(this.name);
		out.writeUTF(this.displayName);
		out.writeUTF(this.realmName);
		out.writeInt(this.fp);
		out.writeInt(this.background);
		out.writeInt(this.difficulty);
		out.writeBoolean(this.allowPlayerTeleport);
		out.writeBoolean(this.showDisplays);
		out.writeShort(this.clientXML.length);
		for (String xml: this.clientXML) {
			out.writeUTF(xml);
		}
		out.writeShort(this.extraXML.length);
		for (String xml: this.extraXML) {
		    out.writeUTF(xml);
		}
	}

}
