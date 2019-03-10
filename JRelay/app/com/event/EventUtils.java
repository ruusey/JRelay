package com.event;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.app.JRelayGUI;
import com.data.PacketType;
import com.models.Packet;
import com.packets.server.NotificationPacket;
import com.packets.server.TextPacket;

public class EventUtils {
	public static void error(String message, Logger log) {
		JRelayGUI.error(message);
		log.log(Level.SEVERE, message);
	}

	public static void info(String message,Logger log) {
		JRelayGUI.log(message);
		log.log(Level.INFO, message);
	}
	public static TextPacket createText(String sender, String text) {
		TextPacket alert = new TextPacket();
		alert.bubbleTime = -1;
		alert.cleanText = text;
		alert.name = "" + sender;
		alert.numStars = -1;
		alert.objectId = -1;
		alert.recipient = "";
		alert.text = text;
		return alert;
	}

	public static NotificationPacket createNotification(int objectId, String message) {
		return notification(objectId, 0xFF0000, message);
	}

	public static NotificationPacket notification(int objectId, int color, String message) {
		NotificationPacket alert = new NotificationPacket();
		alert.objectId = objectId;
		alert.message = "{\"key\":\"blank\",\"tokens\":{\"data\":\"" + message + "\"}}";
		alert.color = color;
		return alert;
	}
	 public static TextPacket createOryxNotification(String sender, String message)
     {
         TextPacket tpacket = null;
		try {
			tpacket = (TextPacket)Packet.create(PacketType.TEXT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         tpacket.bubbleTime = 0;
         tpacket.cleanText = message;
         tpacket.name = "";
         tpacket.numStars = -1;
         tpacket.objectId = -1;
         tpacket.recipient = "";
         tpacket.text = "<JRelay> " + message;
         return tpacket;
     }
	 
	 

}
