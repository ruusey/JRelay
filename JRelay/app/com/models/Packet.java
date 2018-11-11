package com.models;

import com.app.JRelayGUI;
import com.data.GameData;
import com.data.PacketType;
import com.data.shared.IData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.models.Packet;
import com.relay.JRelay;
import com.util.ClassFinder;

public abstract class Packet implements IData {
	public boolean send = true;
	private static List<Class<? extends Packet>> packetIdtoClassMap = new ArrayList<Class<? extends Packet>>(127);
	private static List<Class<? extends Packet>> serverPacketClasses = new ArrayList<Class<? extends Packet>>();
	private static List<Class<? extends Packet>> clientPacketClasses = new ArrayList<Class<? extends Packet>>();
	public static void destroy() {
		packetIdtoClassMap = new ArrayList<Class<? extends Packet>>(127);
	}

	public static void init() {
		for (int i = 0; i < 127; i++) {
			packetIdtoClassMap.add(null);
		}
		List<Class<? extends Packet>> list = loadPackets();

		try {
			JRelay.info("Mapping " + GameData.packets.size() + " packets");
			for (Class<? extends Packet> packetClass : list) {
				Packet packet = packetClass.newInstance();
				String name = packet.getName();
				
				try {
					GameData.packetNameToId.containsKey(name);
				} catch (Exception e) {
					JRelay.error(e.getLocalizedMessage());
				}
				
				int id = 0;
				try{
				    id = GameData.packetNameToId.get(name);
				}catch(Exception e) {
				    System.out.println("Packet("+packetClass.getName()+") did  not match "+name);
				}
				if (id == -1) {
				    System.out.print("Packet("+id+") did  not match "+packetClass.getName());
					packetIdtoClassMap.set(-99, packetClass);
				} else {
					packetIdtoClassMap.set(id, packetClass);
				}
			}
			Set<Integer> ids = GameData.packetIdToName.keySet();
			for (int id : ids) {
				Class<? extends Packet> p = packetIdtoClassMap.get(id);
				if (p == null) {
					PacketModel model = GameData.packetIdToName.get(id);
					JRelayGUI.warn("No class found for " + JRelay.gen.serialize(model));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Packet create(PacketType id) throws Exception {
		Class<? extends Packet> packetClass = packetIdtoClassMap.get((byte) id.id);
		if (packetClass == null) {
			UnknownPacket packet = new UnknownPacket();
			packet.setId((byte) id.id);
			return packet;
		}
		return packetClass.newInstance();
	}

	public static Packet create(byte id) throws Exception {
		Class<? extends Packet> packetClass = packetIdtoClassMap.get(id);
		if (packetClass == null) {
			UnknownPacket packet = new UnknownPacket();
			packet.setId(id);
			return packet;
		}
		return packetClass.newInstance();
	}

	public static Packet create(byte id, byte[] bytes) throws Exception {
		
		Packet packet = Packet.create(id);
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytes));
		packet.parseFromInput(stream);
		int byteLength = packet.getBytes().length;
		if (byteLength != bytes.length) {
			JRelay.info(packet + " byte length is " + byteLength + " after parsing, but was " + bytes.length
					+ " before parsing. Try updating packet definitions");
			UnknownPacket ukp = new UnknownPacket();
			ukp.setId(id);
			return packet;
		} else {
			return packet;
		}
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		this.writeToOutput(out);
		return baos.toByteArray();
	}

	public String getName() {
		String simpleName = this.getClass().getSimpleName();
		int end = simpleName.indexOf("Packet");
		if (end < 0) {
		    //System.out.println(simpleName);
			return "";
		}
		simpleName = simpleName.substring(0, end);
		return simpleName.toUpperCase();
	}

	public byte id() {
		String name = this.getName();
		Integer id = (Integer) GameData.packetNameToId.get(name);
		if (id == null) {
			return -1;
		}
		return id.byteValue();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public static List<Class<? extends Packet>> loadPackets() {
		List<Class<? extends Packet>> res = addClientPackets();
		
		res.addAll(addServerPackets());
		return res;
	}

	public static List<Class<? extends Packet>> addClientPackets() {
		List<Class<? extends Packet>> classes = ClassFinder.find(JRelay.APP_PKG+"com.packets.client");
		clientPacketClasses=classes;
		return classes;
	}

	public static List<Class<? extends Packet>> addServerPackets() {
		List<Class<? extends Packet>> classes = ClassFinder.find(JRelay.APP_PKG+"com.packets.server");
		serverPacketClasses=classes;
		return classes;
	}

	
	public static String buildMetaString(PacketMeta meta) {
		String res = "";
		for(String s : meta.entities) {
			res+=s+"\n";
		}
		return res;
	}
	public static PacketMeta getClientPacketMeta(String name) {
		List<Class<? extends Packet>> cPacks = clientPacketClasses;
		for(Class<? extends Packet> pack : cPacks) {
			if(name.equals(pack.getSimpleName())) {
				PacketMeta meta = new PacketMeta(pack.getSimpleName(),new ArrayList<String>());
				Field[] fields = pack.getDeclaredFields();
				for (Field field : fields) {
					String s = field.getName();
					String t = field.getType().getTypeName();
					if (t.contains(".")) {
						t = t.substring(t.lastIndexOf(".") + 1, t.length());
					}
					String res = "{"+t+"} "+s;
					meta.addEntity(res);	
				}
				return meta;
			}
			
		}
		return null;
	}
	public static PacketMeta getServerPacketMeta(String name) {
		List<Class<? extends Packet>> sPacks = serverPacketClasses;
		for(Class<? extends Packet> pack : sPacks) {
			if(name.equals(pack.getSimpleName())) {
				PacketMeta meta = new PacketMeta(pack.getSimpleName(),new ArrayList<String>());
				Field[] fields = pack.getDeclaredFields();
				for (Field field : fields) {
					String s = field.getName();
					String t = field.getType().getTypeName();
					if (t.contains(".")) {
						t = t.substring(t.lastIndexOf(".") + 1, t.length());
					}
					String res = "{"+t+"} "+s;
					meta.addEntity(res);	
				}
				return meta;
			}
			
		}
		return null;
	}
}