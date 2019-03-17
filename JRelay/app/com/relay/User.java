package com.relay;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.app.JRelayGUI;
import com.crypto.*;
import com.data.PacketType;
import com.data.State;
import com.data.shared.PlayerData;
import com.event.JPlugin;
import com.models.Packet;
import com.packets.client.HelloPacket;
import com.packets.client.PlayerTextPacket;
import java.lang.reflect.Method;

public class User implements Runnable {
	public String GUID = null;
	public boolean connected = true;
	private static final int bufferLength = 65536 * 10;

	public byte[] localBuffer = new byte[bufferLength];
	public int localBufferIndex = 0;
	public RC4 localRecvRC4;
	public RC4 localSendRC4;
	public byte[] remoteBuffer = new byte[bufferLength];
	public int remoteBufferIndex = 0;
	public RC4 remoteRecvRC4;
	public RC4 remoteSendRC4;
	public Socket localSocket;
	public Socket remoteSocket = null;
	public PlayerData playerData = new PlayerData(-1);
	public State state;
	public long lastUpdate = 0;
	public long previousTime = 0;
	public long localNoDataTime = System.currentTimeMillis();
	public long remoteNoDataTime = System.currentTimeMillis();
	public boolean shutdown = false;
	public ArrayList<String> userPluginInstances = new ArrayList<String>();

	public User(Socket localSocket) throws Exception {
		if (localSocket == null) {
			throw new NullPointerException();
		}

		this.localSocket = localSocket;
		this.localRecvRC4 = new RC4(JRelay.instance.key0);
		this.localSendRC4 = new RC4(JRelay.instance.key1);
		this.remoteRecvRC4 = new RC4(JRelay.instance.key1);
		this.remoteSendRC4 = new RC4(JRelay.instance.key0);
		System.out.println(localSocket);
	}

	public void disconnect() {
		this.shutdown = true;
		
	}

	public void destroy() {
		try {
			this.finalize();
			this.localSocket.close();
			this.remoteSocket.close();
		}catch(Throwable e) {
			e.printStackTrace();
		}
		this.localBuffer=null;
		this.remoteBuffer = null;
		this.localRecvRC4 = null;
		this.localSendRC4 = null;
		this.remoteRecvRC4 = null;
		this.remoteSendRC4 = null;
		this.state = null;
		this.playerData = null;
		
	}

	public void kick() {
		this.disconnect();
		this.destroy();	

	}

	public void handleClientPacket(Packet packet) {
		if (packet.send && !this.shutdown) {
			sendToServer(packet);
		}
	}

	public void handleServerPacket(Packet packet) {
		if (packet.send && !this.shutdown) {
			sendToClient(packet);
		}
	}

	public void sendToClient(Packet packet) {
		try {
			this.sendClientPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
			this.kick();
		}
	}

	public void sendToServer(Packet packet) {
		try {
			this.sendServerPacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
			this.kick();
		}
	}

	public void sendClientPacket(Packet packet) throws IOException {

		byte[] packetBytes = packet.getBytes();
		this.localSendRC4.cipher(packetBytes);
		byte packetId = packet.id();
		int packetLength = packetBytes.length + 5;
		DataOutputStream out = new DataOutputStream(this.localSocket.getOutputStream());
		out.writeInt(packetLength);
		out.writeByte(packetId);
		out.write(packetBytes);

	}

	public void sendServerPacket(Packet packet) throws IOException {

		byte[] packetBytes = packet.getBytes();
		this.remoteSendRC4.cipher(packetBytes);
		byte packetId = packet.id();
		int packetLength = packetBytes.length + 5;
		DataOutputStream out = new DataOutputStream(this.remoteSocket.getOutputStream());
		out.writeInt(packetLength);
		out.writeByte(packetId);
		out.write(packetBytes);
	}

	public void hookPacket(PacketType type, Class<? extends JPlugin> location, String callback) {
		Method method = null;
		try {
			method = location.getMethod(callback, Packet.class);
		} catch (Exception e) {
			JRelay.error(e.getMessage());
			e.printStackTrace();
		}
		for (Entry<PacketType, ArrayList<Method>> hook : JRelay.instance.packetHooks.entrySet()) {
			ArrayList<Method> meth = hook.getValue();
			if (meth.contains(method)) {
				return;
			}

		}
		if (method != null) {
			// JRelay.info("Hooked " + method.getName() + " to packet '"
			// + type.name() + "'");
			if (JRelay.instance.packetHooks.containsKey(type)) {
				ArrayList<Method> pMethods = JRelay.instance.packetHooks.get(type);
				pMethods.add(method);
				JRelay.instance.packetHooks.replace(type, pMethods);
			} else {
				ArrayList<Method> pMethods = new ArrayList<Method>();
				pMethods.add(method);
				JRelay.instance.packetHooks.put(type, pMethods);
			}

		} else {
			JRelay.error("The callback " + callback + " is already bound");
		}

	}

	public void hookRequiredPacket(PacketType type, Class<? extends JPlugin> location, String callback) {
		Method method = null;

		try {
			method = location.getMethod(callback, Packet.class);
		} catch (Exception e) {

			JRelay.error(e.getMessage());
			e.printStackTrace();
		}
		for (Entry<PacketType, ArrayList<Method>> hook : JRelay.instance.requiredPacketHooks.entrySet()) {
			ArrayList<Method> meth = hook.getValue();
			if (meth.contains(method)) {
				return;
			}

		}
		if (method != null) {
			// JRelay.info("Hooked " + method.getName() + " to packet '"
			// + type.name() + "'");
			if (JRelay.instance.requiredPacketHooks.containsKey(type)) {
				ArrayList<Method> pMethods = JRelay.instance.requiredPacketHooks.get(type);
				pMethods.add(method);

				JRelay.instance.requiredPacketHooks.replace(type, pMethods);

			} else {
				ArrayList<Method> pMethods = new ArrayList<Method>();
				pMethods.add(method);

				JRelay.instance.requiredPacketHooks.put(type, pMethods);

			}

		} else {
			JRelay.error("The callback " + callback + " is already bound");
		}

	}

	public void hookCommand(String command, Class<? extends JPlugin> location, String callback) {
		Method method = null;
		if (JRelay.instance.commandHooks.containsKey(command)) {
			return;
		}
		try {
			method = location.getMethod(callback, String.class, String[].class);
		} catch (Exception e) {

			JRelay.error(e.getMessage());
			e.printStackTrace();
		}
		if (method != null) {
			JRelay.instance.commandHooks.put(command, method);
		} else {
			JRelay.error("The callback " + callback + " is already bound");
		}
	}

	public void invokeOnPacketRequired(Packet packet) {

		for (PacketType pt : JRelay.instance.requiredPacketHooks.keySet()) {
			if (pt.id == packet.id()) {

				try {
					List<Method> methods = JRelay.instance.requiredPacketHooks.get(pt);
					for (Method method : methods) {

						String m = method.getDeclaringClass().getSimpleName();
						method.invoke(JRelay.instance.requiredPlugins.get(m), packet);
					}

				} catch (Exception e) {
					JRelay.error(e.getMessage() + e.getCause().toString());
					e.printStackTrace();
				}

			}
		}

	}

	public void invokeOnPacket(Packet packet) {

		for (PacketType pt : JRelay.instance.packetHooks.keySet()) {
			if (pt.id == packet.id()) {
				try {
					List<Method> methods = JRelay.instance.packetHooks.get(pt);
					for (Method method : methods) {
						String m = method.getDeclaringClass().getSimpleName();
						method.invoke(JRelay.instance.userPlugins.get(m), packet);
					}
				} catch (Exception e) {
					JRelay.error(e.getMessage());
					e.printStackTrace();
				}

			}
		}

	}

	public void invokeOnCommand(Packet packet) {
		if (packet instanceof PlayerTextPacket) {
			PlayerTextPacket text = (PlayerTextPacket) packet;
			String msg = text.text;
			if (msg.toLowerCase().startsWith("/")) {
				msg = msg.replace("/", "");

				try {
					String[] args = msg.split(" ");

					Method m = JRelay.instance.commandHooks.get(args[0]);
					if (m != null) {
						String method = m.getDeclaringClass().getSimpleName();

						if (JRelay.instance.userPlugins.containsKey(method)) {

							m.invoke(JRelay.instance.userPlugins.get(method), args[0], args);
						} else {
							// IS A SERVER COMMAND SEND TO SERVER
							handleClientPacket(packet);
						}
					} else {
						handleClientPacket(packet);
					}

				} catch (Exception e) {
					e.printStackTrace();
					// IS A SERVER COMMAND SEND TO SERVER
					handleClientPacket(packet);
				}

			} else {
				handleClientPacket(packet);
			}

		}
	}

	public void connect(HelloPacket state) {
		try {
			//System.out.println(this.state.conTargetAddress+" "+this.state.conTargetPort);
			this.remoteSocket = new Socket(this.state.conTargetAddress, this.state.conTargetPort);
			this.remoteSocket.setTcpNoDelay(true);
			this.sendServerPacket(state);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveState() {
		JRelay.instance.userStates.forcePut(this.state.GUID, this.state);
	}

	@Override
	public void run() {
		while (!shutdown) {
			try {
				if (this.remoteSocket != null) {
					try {
						InputStream in = this.remoteSocket.getInputStream();
						if (in.available() > 0) {
							int bytesRead = this.remoteSocket.getInputStream().read(this.remoteBuffer,
									this.remoteBufferIndex, this.remoteBuffer.length - this.remoteBufferIndex);
							if (bytesRead == -1) {
								throw new SocketException("end of stream");
							} else if (bytesRead > 0) {
								this.remoteBufferIndex += bytesRead;

								while (this.remoteBufferIndex >= 5) {
									int packetLength = ((ByteBuffer) ByteBuffer.allocate(4).put(this.remoteBuffer[0])
											.put(this.remoteBuffer[1]).put(this.remoteBuffer[2])
											.put(this.remoteBuffer[3]).rewind()).getInt();
									if (this.remoteBufferIndex < packetLength) {
										break;
									}
									byte packetId = this.remoteBuffer[4];
									byte[] packetBytes = new byte[packetLength - 5];
									System.arraycopy(this.remoteBuffer, 5, packetBytes, 0, packetLength - 5);
									if (this.remoteBufferIndex > packetLength) {
										System.arraycopy(this.remoteBuffer, packetLength, this.remoteBuffer, 0,
												this.remoteBufferIndex - packetLength);
									}
									this.remoteBufferIndex -= packetLength;
									this.remoteRecvRC4.cipher(packetBytes);
									Packet packet = Packet.create(packetId, packetBytes);
									//System.out.println(JRelay.gen.serialize(packetId+" "+ packet));
									invokeOnPacketRequired(packet);
									invokeOnPacket(packet);
									handleServerPacket(packet);

								}
							}
							this.remoteNoDataTime = System.currentTimeMillis();
						} else if (System.currentTimeMillis() - this.remoteNoDataTime >= 10000) {
							throw new SocketException("remote data timeout");
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (!(e instanceof SocketException)) {
							JRelay.error(e.getMessage() + " End of remote stream.");
								this.kick();
							 e.printStackTrace();
						} else {
							JRelay.error(e.getMessage() + " End of remote stream.");
							this.kick();
						}
						
					}
				}
				try {
					Thread.sleep(10);
				}catch(Exception e) {};
				InputStream in = this.localSocket.getInputStream();
				if (in.available() > 0) {
					int bytesRead = in.read(this.localBuffer, this.localBufferIndex,
							this.localBuffer.length - this.localBufferIndex);
					if (bytesRead == -1) {
						throw new SocketException("eof");
					} else if (bytesRead > 0) {
						this.localBufferIndex += bytesRead;
						while (this.localBufferIndex >= 5) {
							int packetLength = ((ByteBuffer) ByteBuffer.allocate(4).put(this.localBuffer[0])
									.put(this.localBuffer[1]).put(this.localBuffer[2]).put(this.localBuffer[3])
									.rewind()).getInt();
							if (this.localBufferIndex < packetLength) {
								break;
							}
							byte packetId = this.localBuffer[4];
							byte[] packetBytes = new byte[packetLength - 5];
							System.arraycopy(this.localBuffer, 5, packetBytes, 0, packetLength - 5);
							if (this.localBufferIndex > packetLength) {
								System.arraycopy(this.localBuffer, packetLength, this.localBuffer, 0,
										this.localBufferIndex - packetLength);
							}
							this.localBufferIndex -= packetLength;
							this.localRecvRC4.cipher(packetBytes);
							
							Packet packet = null;
							try{
								packet = Packet.create(packetId, packetBytes);
							}catch(Exception e) {
								JRelay.error("Unable to read HELLO packet. Are your RC4 keys up to date?");
							}
							//System.out.println(JRelay.gen.serialize(packet));
							if (packet instanceof PlayerTextPacket) {
								// ATTEMPT TO INVOKE COMMAND IF PLAYER TEXT
								invokeOnCommand(packet);

							} else {
								// ATTEMPT TO INVOKE ON PACKET HOOKS THEN SEND TO SERVER
								invokeOnPacketRequired(packet);
								invokeOnPacket(packet);
								handleClientPacket(packet);

							}

						}
					}

					this.localNoDataTime = System.currentTimeMillis();
				} else if (System.currentTimeMillis() - this.localNoDataTime >= 10000) {
					
					throw new SocketException("Local data read timeout");
				}
			} catch (Exception e) {
				e.printStackTrace();
//				if (shutdown) {
//					this.kick();
//				}
			}
			
		}

	}

}
