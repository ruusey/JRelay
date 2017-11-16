package com.event;

import com.packets.server.NotificationPacket;
import com.packets.server.TextPacket;

public class EventUtils {
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

}
