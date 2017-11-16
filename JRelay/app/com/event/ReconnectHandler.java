package com.event;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import javax.swing.SwingUtilities;

import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.event.EventUtils;
import com.event.JPlugin;
import com.models.Packet;
import com.models.Server;
import com.packets.client.HelloPacket;
import com.packets.server.ReconnectPacket;
import com.relay.JRelay;
import com.relay.User;

public class ReconnectHandler extends JPlugin {

	public ReconnectHandler(User user) {
		super(user);
		
	}

	public void attach() {
		user.hookRequiredPacket(PacketType.CREATESUCCESS,
				ReconnectHandler.class, "onCreateSuccess");
		user.hookRequiredPacket(PacketType.RECONNECT, ReconnectHandler.class,
				"onReconnect");
		user.hookRequiredPacket(PacketType.HELLO, ReconnectHandler.class,
				"onHello");
		user.hookCommand("con", ReconnectHandler.class, "onConnectCommand");
		user.hookCommand("connect", ReconnectHandler.class, "onConnectCommand");
		user.hookCommand("server", ReconnectHandler.class, "onConnectCommand");
		user.hookCommand("recon", ReconnectHandler.class, "onReconCommand");
		user.hookCommand("drecon", ReconnectHandler.class, "onDreconCommand");
	}

	public void onHello(Packet pack) {
		HelloPacket packet = (HelloPacket) pack;
		State thisState = JRelay.instance.getState(user, packet.key);
		user.state=thisState;
		user.state.locationName="n/a";
		if (user.state.conRealKey.length != 0) {
			packet.key = user.state.conRealKey;
			user.state.conRealKey = new byte[0];
		}
		user.connect(packet);
		pack.send=false;
	}

	public void onCreateSuccess(Packet pack) {
		String message = "JRelay - ";
		Server s = GameData.servers.get(user.state.conTargetAddress);
		
		String displayLoc=user.state.locationName;
		if(s!=null){
			message+=s.name;
		}else{
			message+=displayLoc;
		}
		final String msg = message;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(800);
					sendToClient(EventUtils.createNotification(
							user.playerData.ownerObjectId, msg));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void onReconnect(Packet pack) {
		ReconnectPacket packet = (ReconnectPacket) pack;
		user.state.locationName="/"+user.state.locationName+"/"+packet.name;
		if (packet.host.contains(".com")) {
			try {
				InetAddress addr = InetAddress.getByName(packet.host);
				String host = addr.getHostName();
				packet.host = host;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (packet.name.toLowerCase().contains("nexusportal")) {
			ReconnectPacket recon = new ReconnectPacket();
			recon.isFromArena = false;
			recon.gameId = packet.gameId;
			recon.host = packet.host == "" ? user.state.conTargetAddress
					: packet.host;
			recon.port = packet.port == -1 ? user.state.conTargetPort
					: packet.port;
			recon.key = packet.key;
			recon.keyTime = packet.keyTime;
			recon.name = packet.name;
			user.state.lastRealm = recon;
		} else if (packet.name != "" && !packet.name.contains("vault")
				&& packet.gameId != -2) {
			ReconnectPacket drecon = new ReconnectPacket();

			drecon.isFromArena = false;
			drecon.gameId = packet.gameId;
			drecon.host = packet.host == "" ? user.state.conTargetAddress
					: packet.host;
			drecon.port = packet.port == -1 ? user.state.conTargetPort
					: packet.port;
			drecon.key = packet.key;
			drecon.keyTime = packet.keyTime;
			drecon.name = packet.name;
			user.state.lastDungeon = drecon;
		}
		
		if (packet.port != -1)
			user.state.conTargetPort = packet.port;

		if (!packet.host.equals(""))
			user.state.conTargetAddress = packet.host;

		if (packet.key.length != 0)
			user.state.conRealKey = packet.key;

		try {
			packet.key = (user.state.GUID).getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        packet.host = "localhost";
        packet.port = 2050;
        user.saveState();

	}

	public void onConnectCommand(String command, String[] args) {
		if (args.length == 1) {
			if (GameData.abbrToServer.containsKey(args[0].toUpperCase())) {
				ReconnectPacket reconnect = null;
				try {
					reconnect = (ReconnectPacket) Packet
							.create((byte) PacketType.RECONNECT.id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				reconnect.host = GameData.abbrToServer.get(args[0]
						.toUpperCase()).address;
				reconnect.port = 2050;
				reconnect.gameId = -2;
				reconnect.name = "Nexus";
				reconnect.isFromArena = false;
				reconnect.key = new byte[0];
				reconnect.keyTime = 0;
				sendReconnect(user, reconnect);
			} else {
				try {
					user.sendClientPacket(EventUtils.createText("K Relay",
							"Unknown server!"));
				} catch (Exception e) {

				}
			}
		}
	}

	public void onReconCommand(String command, String[] args) {
		if (user.state.lastRealm != null)
			sendReconnect(user, user.state.lastRealm);
		else {
			sendToClient(EventUtils.createText("K Relay",
					"Last realm is unknown!"));
		}
	}

	public void onDreconCommand(String command, String[] args) {
		if (user.state.lastDungeon != null)
			sendReconnect(user, user.state.lastDungeon);
		else {
			sendToClient(EventUtils.createText("K Relay",
					"Last dungeon is unknown!"));
		}
	}

	public static void sendReconnect(User user, ReconnectPacket reconnect) {
		String host = reconnect.host;
		int port = reconnect.port;
		byte[] key = reconnect.key;
		user.state.conTargetAddress = host;
		user.state.conTargetPort = port;
		user.state.conRealKey = key;
		try {
			reconnect.key = (user.state.GUID).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(reconnect.host.length()==0){
			user.state.conTargetAddress = JRelay.instance.remoteHost;
			user.state.conTargetPort = 2050;
			reconnect.key=user.state.conRealKey;
		}else{
			user.state.conTargetAddress=host;
			user.state.conTargetPort=2050;
		}
		reconnect.host = "localhost";
		reconnect.port = 2050;
		
		user.saveState();
		try {
			user.handleServerPacket(reconnect);
		} catch (Exception e) {
			e.printStackTrace();
		}

		reconnect.key = key;
		reconnect.host = host;
		reconnect.port = port;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return null;
	}

}
