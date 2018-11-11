package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import com.models.Packet;



public class HelloPacket extends Packet{
	 public String buildVersion;
public int gameId;
public String GUID;
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
    GUID = in.readUTF();
    random1 = in.readInt();
    password = in.readUTF();
    random2 = in.readInt();
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
	out.writeUTF(buildVersion);
	out.writeInt(this.gameId);
		out.writeUTF(this.GUID);
		out.writeInt(this.random1);
		out.writeUTF(this.password);
		out.writeInt(this.random2);
		out.writeUTF(this.secret);
		out.writeInt(this.keyTime);
		out.writeShort(this.key.length);
		out.write(this.key);
		out.writeInt(this.mapJSON.length);
		out.write(mapJSON);
		out.writeUTF(entryTag);
		out.writeUTF(gameNet);
		out.writeUTF(gameNetUserId);
		out.writeUTF(playPlatform);
		out.writeUTF(platformToken);
		out.writeUTF(userToken);
		
}
}
