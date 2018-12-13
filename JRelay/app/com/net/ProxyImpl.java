package com.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.event.EventUtils;
import com.models.Packet;
import com.relay.Client;
import com.relay.JRelay;
import com.relay.User;

public class ProxyImpl {
	public static Logger log = Logger.getLogger(ProxyImpl.class.getName());
	@FunctionalInterface
	interface FootBall{
		void invoke(Client client);
	}
	@FunctionalInterface
	interface PacketHandler{
		void invoke(Client client, Packet packet);
	}
	@FunctionalInterface
	interface GenericPacketHandler{
		void invoke(Client client, Class<? extends Packet> packet);
	}
	@FunctionalInterface
	interface ListenerHandler{
		void invoke(ProxyImpl proxy);
	}
	@FunctionalInterface
	interface ConnectionHandler{
		void invoke(Client client);
	}
	@FunctionalInterface
	interface CommandHandler {
		void invoke(Client client, String command, String[] args);
	}
	
	FootBall footBall;
	ListenerHandler proxyStarted = (ProxyImpl impl) ->{impl.start();};
	ListenerHandler proxyClosed;
	ConnectionHandler clientConnected = (Client client)-> { localConnect(client); };
	ConnectionHandler clientDisconnected;
	PacketHandler serverPacketRecieved;
	PacketHandler clientPacketRecieved;
	public Hashtable<String,State> states;
	private Hashtable<Object,?> genericPacketHooks;
	private Hashtable<PacketHandler, ArrayList<PacketType>> packetHooks;
    private Hashtable<CommandHandler, ArrayList<String>> commandHooks;
	private ServerSocket localListener=null;
	public ProxyImpl() {
		states= new Hashtable<String,State>();
		genericPacketHooks=new Hashtable<Object,Class<? extends Packet>>();
		packetHooks = new Hashtable<PacketHandler, ArrayList<PacketType>>();
		commandHooks = new Hashtable<CommandHandler, ArrayList<String>>();
	}
	public void start() {
		try {
			this.localListener = new ServerSocket(2050,10,InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CompletableFuture.runAsync(() -> {
			try {
				Socket newUser = this.localListener.accept();
				
				Client cli = new Client(this,newUser);
				clientConnected.invoke(cli);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	public void stop() {
		if(this.localListener==null) {
			return;
		}
		EventUtils.info("Stopping local listener", log);
		
			try {
				this.localListener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.localListener=null;
		
	}
	private void localConnect(Client cli) {
		
	}

}
