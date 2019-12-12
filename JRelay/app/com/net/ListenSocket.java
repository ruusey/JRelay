package com.net;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import com.relay.JRelay;

public abstract class ListenSocket implements Runnable {

	private final SocketAddress endpoint;
	private ServerSocket serversocket;
	private Thread thread;

	public ListenSocket(int port) {
		this.endpoint = new InetSocketAddress(port);
	}
	
	public ListenSocket(String hostname, int port) {
		this.endpoint = new InetSocketAddress(hostname, port);
	}
	
	public boolean isClosed() {
		return this.serversocket == null || this.serversocket.isClosed();
	}

	public boolean start() {
		JRelay.info("Started client listener");
		stop();
		try {
			this.serversocket = new ServerSocket();
			this.serversocket.bind(this.endpoint);
		} catch (Exception e) {
			stop();
			return false;
		}
		this.thread = new Thread(this);
		this.thread.start();
		return true;
	}

	public void stop() {
		this.thread = null;
		if (this.serversocket != null) {
			try {
				this.serversocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.serversocket = null;
		}
	}

	@Override
	public void run() {
		while (this.serversocket != null && !this.serversocket.isClosed()) {
			try {
				final Socket socket = this.serversocket.accept();
				new Thread(new Runnable() {

					@Override
					public void run() {
						socketAccepted(socket);
					}
					
				}).start();
				
			} catch (Exception e) {
				JRelay.error(e.getMessage());
			}
		}
	}

	public abstract void socketAccepted(Socket socket);
	
	@Override
	public String toString() {
		return "ListenSocket[endpoint=" + this.endpoint + "]";
	}

}
