package plugins;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import javax.swing.SwingUtilities;

import com.app.JRelayGUI;
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
import java.net.UnknownHostException;
public class ReconnectHandler extends JPlugin {
	
	public ReconnectHandler(User user) {
		super(user);
		
	}

	public void attach() {
		user.hookPacket(PacketType.CREATESUCCESS,
				ReconnectHandler.class, "onCreateSuccess");
		user.hookPacket(PacketType.RECONNECT, ReconnectHandler.class,
				"onReconnect");
		user.hookPacket(PacketType.HELLO, ReconnectHandler.class,
				"onHello");
		user.hookCommand("jcon", ReconnectHandler.class, "onConnectCommand");
		user.hookCommand("jconnect", ReconnectHandler.class, "onConnectCommand");
		user.hookCommand("defserver", ReconnectHandler.class, "changeDefaultServer");
		user.hookCommand("jrecon", ReconnectHandler.class, "onReconCommand");
		user.hookCommand("jdrecon", ReconnectHandler.class, "onDreconCommand");
	}

	public void onHello(Packet pack) {
		HelloPacket packet = (HelloPacket) pack;
		State thisState = JRelay.instance.getState(user, packet.key);
		if(packet.key.length>2) {
			JRelayGUI.log("Key: "+packet.key[0]+""+packet.key[1]+""+packet.key[2]+""+", User state: "+thisState.GUID+", "+thisState.conTargetAddress);
		}else {
			
			JRelayGUI.log("New User state: "+thisState.GUID+", "+thisState.conTargetAddress);
		}
		
		user.state=thisState;
		user.state.lastHello=packet;
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
		ReconnectPacket ppacket = (ReconnectPacket) pack;
		ReconnectPacket packet = cloneReconnectPacket(user, ppacket);
        user.state.lastReconnect = cloneReconnectPacket(user, packet);
        if (packet.host.contains(".com")) {
        	java.net.InetAddress addr = null;
			try {
				addr = InetAddress.getByName(packet.host);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  String host = addr.getHostName();
			  packet.host=host;
        }
        
        if (packet.name.toLowerCase().contains("nexusportal")) {
            user.state.lastRealm = cloneReconnectPacket(user, packet);
        }
        else if (((!packet.name.equals("")) 
                    && (!packet.name.contains("vault") 
                    && (packet.gameId != -2)))) {
            user.state.lastDungeon = cloneReconnectPacket(user, packet);
        }
        
        if ((packet.port != -1)) {
            user.state.conTargetPort = packet.port;
        }
        
        if ((!packet.host.equals(""))) {
            user.state.conTargetAddress = packet.host;
        }
        
        if ((packet.key.length != 0)) {
            user.state.conRealKey = packet.key;
        }
        
        //  Tell the client to connect to the proxy
        try {
			ppacket.key = (user.state.GUID).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ppacket.host = "localhost";
        ppacket.port = 2050;
        sendToClient(ppacket);
        //sendReconnect(user,ppacket);
	}
	 public static ReconnectPacket cloneReconnectPacket(User client, ReconnectPacket packet)
     {
         ReconnectPacket clone = null;
		try {
			clone = (ReconnectPacket)Packet.create(PacketType.RECONNECT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         clone.isFromArena = false;
         clone.gameId = packet.gameId;
         clone.host = packet.host.equals("") ? client.state.conTargetAddress : packet.host;
         clone.port = packet.port == -1 ? client.state.conTargetPort : packet.port;
         clone.key = packet.key;
         clone.stats = packet.stats;
         clone.keyTime = packet.keyTime;
         clone.name = packet.name;

         return clone;
     }
	 public void changeDefaultServer(String command, String[] args) {
			
			if (args.length == 2) {
				JRelayGUI.log("Changing default server to " +args[1].toUpperCase());
				if (GameData.abbrToServer.containsKey(args[1].toUpperCase())) {
					ReconnectPacket reconnect = null;
					try {
						reconnect = (ReconnectPacket) Packet
								.create(PacketType.RECONNECT);
					} catch (Exception e) {
						e.printStackTrace();
					}
					reconnect.host = GameData.abbrToServer.get(args[1]
							.toUpperCase()).address;
					reconnect.port = 2050;
					reconnect.gameId = -2;
					reconnect.stats="";
					reconnect.name = "Nexus";
					reconnect.isFromArena = false;
					reconnect.key = new byte[0];
					reconnect.keyTime = 0;
					JRelay.DEFAULT_SERVER=reconnect.host;
					sendReconnect(user, reconnect);
				} else {
					try {
						user.sendClientPacket(EventUtils.createOryxNotification("JRelay",
								"Unknown server specified!"));
					} catch (Exception e) {

					}
				}
			}
		}
	public void onConnectCommand(String command, String[] args) {
		
		if (args.length == 2) {
			JRelay.info("Connecting client to " +args[1].toUpperCase());
			if (GameData.abbrToServer.containsKey(args[1].toUpperCase())) {
				ReconnectPacket reconnect = null;
				try {
					reconnect = (ReconnectPacket) Packet
							.create(PacketType.RECONNECT);
				} catch (Exception e) {
					e.printStackTrace();
				}
				reconnect.host = GameData.abbrToServer.get(args[1]
						.toUpperCase()).address;
				reconnect.port = 2050;
				reconnect.gameId = -2;
				reconnect.stats="";
				reconnect.name = "Nexus";
				reconnect.isFromArena = false;
				reconnect.key = new byte[0];
				reconnect.keyTime = 0;
				JRelay.DEFAULT_SERVER=reconnect.host;
				sendReconnect(user, reconnect);
			} else {
				try {
					user.sendClientPacket(EventUtils.createText("JRelay",
							"Unknown server specified!"));
				} catch (Exception e) {

				}
			}
		}
	}

	public void onReconCommand(String command, String[] args) {
		if (user.state.lastRealm != null)
			sendReconnect(user, user.state.lastRealm);
		else {
			sendToClient(EventUtils.createText("JRelay",
					"Last realm is unknown!"));
		}
	}

	public void onDreconCommand(String command, String[] args) {
		if (user.state.lastDungeon != null)
			sendReconnect(user, user.state.lastDungeon);
		else {
			sendToClient(EventUtils.createText("JRelay",
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
		reconnect.host = "localhost";
		reconnect.port = 2050;
		
		try {
			user.sendToClient(reconnect);
		} catch (Exception e) {
			e.printStackTrace();
		}

		reconnect.key = key;
		reconnect.host = host;
		reconnect.port = port;
		JRelay.DEFAULT_SERVER=host;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "System";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JRelay.ReconectHandler";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Handles reconnection and proxy tunneling";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] { "/jcon || {jconnect,jserver} [server abbreviation] - connect to the specified server",
				"/jrecon  - soon to be implemented",
				"/jdrecon  - soon to be implemented"};
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] {};
	}

}
