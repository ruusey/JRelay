package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.crypto.RSA;
import com.models.Packet;
import com.relay.JRelay;



public class HelloPacket extends Packet{
	 public String buildVersion;
public int gameId;
public String guid;
public int random1;
public String password;
public int random2;
public String secret;
public int keyTime;
public byte[] key;
public byte[] mapJSON;
public String entryTag;
public String gameNet;
public String gameNetUserId;
public String playPlatform;
public String platformToken;
public String userToken;

public void parseFromInput(DataInput in) throws IOException
{
	
    buildVersion = in.readUTF();
    gameId = in.readInt();
    guid = in.readUTF();
    random1=in.readInt();
    password = in.readUTF();
    random2=in.readInt();
    secret = in.readUTF();
    keyTime = in.readInt();
    key = new byte[in.readShort()];
    in.readFully(this.key);
    mapJSON = new byte[in.readInt()];
    in.readFully(this.mapJSON);
    entryTag = in.readUTF();
    gameNet = in.readUTF();
    gameNetUserId = in.readUTF();
    playPlatform = in.readUTF();
    platformToken = in.readUTF();
    userToken = in.readUTF();
    
}
public void writeToOutput(DataOutput out) throws IOException
{
	out.writeUTF(this.buildVersion);
	out.writeInt(this.gameId);
	out.writeUTF((this.guid));
	out.writeInt(this.random1);
	out.writeUTF((this.password));
	out.writeInt(this.random2);
	out.writeUTF((this.secret));
	out.writeInt(this.keyTime);
	out.writeShort(this.key.length);
	out.write(this.key);
	out.writeInt(this.mapJSON.length);
	out.write(this.mapJSON);
	out.writeUTF(this.entryTag);
	out.writeUTF(this.gameNet);
	out.writeUTF(this.gameNetUserId);
	out.writeUTF(this.playPlatform);
	out.writeUTF(this.platformToken);
	out.writeUTF(this.userToken);
		
}
}
