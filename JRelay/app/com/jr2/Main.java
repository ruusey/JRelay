package com.jr2;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.crypto.RC4;
import com.data.GameData;
import com.models.Packet;
import com.util.ClassFinder;

/**
 * (<b>Entry point</b>) Demonstrates how to write a very simple tunneling proxy
 * using MINA. The proxy only logs all data passing through it. This is only
 * suitable for text based protocols since received data will be converted into
 * strings before being logged.
 * <p>
 * Start a proxy like this:<br>
 * <code>org.apache.mina.example.proxy.Main 12345 www.google.com 80</code><br>
 * and open <a href="http://localhost:12345">http://localhost:12345</a> in a
 * browser window.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class Main {
	public static final int PORT = 2050;
    public static String key1 = "c79332b197f92ba85ed281a023";
	public static String key0 = "6a39570cc9de4ec71d64821894";
	public static RC4 localRecvRC4;
	public static RC4 localSendRC4;
	public static RC4 remoteRecvRC4;
	public static RC4 remoteSendRC4;
	public static void main(String[] args) throws Exception {
		//ClassFinder.getClassOfPackage("com.packets.server");
		GameData.loadData();
   	    Packet.init();
   	    localRecvRC4 = new RC4(key0);
		localSendRC4 = new RC4(key1);
		remoteRecvRC4 = new RC4(key1);
		remoteSendRC4 = new RC4(key0);
		NioSocketAcceptor acceptor = new NioSocketAcceptor();

		// Create TCP/IP connector.
		IoConnector connector = new NioSocketConnector();

		// Set connect timeout.
		connector.setConnectTimeoutMillis(30 * 1000L);

		ClientToProxyIoHandler handler = new ClientToProxyIoHandler(connector,
				new InetSocketAddress("52.91.68.60", 2050));

		// Start proxy.
		acceptor.setHandler(handler);
		acceptor.bind(new InetSocketAddress(2050));

		System.out.println("Listening on port " + 2050);
	}

}
