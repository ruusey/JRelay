package com.event;

import java.io.DataOutputStream;
import java.io.IOException;

import com.models.Packet;
import com.relay.User;

public abstract class JPlugin implements PluginData{
	public User user;
	
	public JPlugin(User user){
		this.user=user;
	}
	
	public void sendToClient(Packet packet){
		try{
			if(packet.send){
				sendEventToClient(packet);
			}
			
		}catch(Exception e){
			user.kick();
		}
	}
	
	public void sendToServer(Packet packet){
		try{
			if(packet.send){
				sendEventToServer(packet);	
			}		
		}catch(Exception e){
			user.kick();
		}
	}
	
	public void sendEventToClient(Packet packet) throws IOException {
		byte[] packetBytes = packet.getBytes();
		user.localSendRC4.cipher(packetBytes);
		byte packetId = packet.id();
		int packetLength = packetBytes.length + 5;
		DataOutputStream out = new DataOutputStream(user.localSocket.getOutputStream());
		out.writeInt(packetLength);
		out.writeByte(packetId);
		out.write(packetBytes);
	}
	
	public void sendEventToServer(Packet packet) throws IOException {
		byte[] packetBytes = packet.getBytes();
		user.remoteSendRC4.cipher(packetBytes);
		byte packetId = packet.id();
		int packetLength = packetBytes.length + 5;
		DataOutputStream out = new DataOutputStream(user.remoteSocket.getOutputStream());
		out.writeInt(packetLength);
		out.writeByte(packetId);
		out.write(packetBytes);
	}
	
	
}
