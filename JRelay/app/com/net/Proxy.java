package com.net;

import com.models.Packet;
import com.relay.Client;

public interface Proxy {
	public void listenerHandler(ProxyImpl proxy);
	public void connectionHandler(Client client);
	public void footBall(Client client);
	public void packetHandler(Client client, Packet packet);
	public void genericPacketHandler(Client client, Class<? extends Packet> packet);
	public void commandHandler(Client client, String command, String[] args);
}
