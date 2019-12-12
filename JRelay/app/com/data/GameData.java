package com.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.models.Item;
import com.models.PacketModel;
import com.models.Projectile;
import com.models.Server;
import com.models.Tile;
import com.models.Object;
import com.relay.JRelay;
import com.util.Parse;

@SuppressWarnings("unchecked")
public class GameData {
	// **********************
	// MEMORY STORED GAMEDATA
	// **********************
	public static HashMap<Integer, Item> items = new HashMap<Integer, Item>();
	public static HashMap<String, Item> nameToItem = new HashMap<String, Item>();
	public static HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
	public static HashMap<String, Tile> nameToTile = new HashMap<String, Tile>();
	public static HashMap<Integer, Object> objects = new HashMap<Integer, Object>();
	public static HashMap<String, Object> nameToObject = new HashMap<String, Object>();
	public static HashMap<Byte, PacketModel> packets = new HashMap<Byte, PacketModel>();
	public static HashMap<String, Server> abbrToServer = new HashMap<String, Server>();
	public static HashMap<String, Server> servers = new HashMap<String, Server>();
	public static HashMap<String, Server> nameToServer = new HashMap<String, Server>();
	public static HashMap<Integer, PacketModel> packetIdToName = new HashMap<Integer, PacketModel>();
	public static HashMap<String, Integer> packetNameToId = new HashMap<String, Integer>();
	// **********************
	// MEMORY STORED PACKET TYPES
	// **********************
	public static ArrayList<String> packetType = new ArrayList<String>();

	public static Logger log = Logger.getLogger(GameData.class.getName());
	
	public static boolean loadData() {

		SAXReader reader = new SAXReader();
		try {

			log.log(Level.INFO, "Loading game assets...");
			Document objects = reader.read(JRelay.RES_LOC + "xml\\objects.xml");
			Document tiles = reader.read(JRelay.RES_LOC + "xml\\tiles.xml");
			Document packets = reader.read(JRelay.RES_LOC + "xml\\packets.xml");
			Document servers = reader.read(JRelay.RES_LOC + "xml\\servers.xml");
			parseItems(objects);
			parseTiles(tiles);
			parseObjects(objects);
			parsePackets(packets);
			parseServers(servers);

			if (GameData.tiles.isEmpty() || GameData.objects.isEmpty() || GameData.packets.isEmpty()
					|| GameData.abbrToServer.isEmpty()) {
				//JRelay.info("Error loading game assets");
				return false;
			} else {
				//JRelay.info("Succesfully loaded all game assets");
				return true;
			}

		} catch (DocumentException e) {

			log.log(Level.SEVERE, e.getMessage());
			return false;
		}

	}

	public static void destroy() {
		items = new HashMap<Integer, Item>();
		tiles = new HashMap<Integer, Tile>();
		nameToTile = new HashMap<String, Tile>();
		objects = new HashMap<Integer, Object>();
		nameToObject = new HashMap<String, Object>();
		packets = new HashMap<Byte, PacketModel>();
		abbrToServer = new HashMap<String, Server>();
		servers = new HashMap<String, Server>();
		packetIdToName = new HashMap<Integer, PacketModel>();
		packetNameToId = new HashMap<String, Integer>();
	}

	public static void parseItems(Document itemDoc) {

		List<Node> list = itemDoc.selectNodes("//Object[Item]");
		for (Node node : list) {
			int id = Parse.parseHex(Parse.attrDefault(node, "type", "0x0"));
			String name = Parse.attrDefault(node, "id", "");

			boolean soulbound = Parse.hasElement(node, "Soulbound");
			boolean useable = Parse.hasElement(node, "Useable");
			boolean consumeable = Parse.hasElement(node, "Consumeable");

			int tier = 0;
			if (Parse.hasElement(node, "Tier")) {
				tier = Parse.parseInt(Parse.elemDefault(node, "Tier", "0"));
			}

			byte slotType = (byte) Parse.parseInt(Parse.elemDefault(node, "SlotType", "0"));
			float rof = Parse.parseFloat(Parse.elemDefault(node, "RateOfFire", "1"));
			int feed = Parse.parseInt(Parse.elemDefault(node, "feedPower", "0"));
			byte bag = (byte) Parse.parseInt(Parse.elemDefault(node, "BagType", "0"));
			byte mp = (byte) Parse.parseInt(Parse.elemDefault(node, "MPCost", "0"));
			byte fame = (byte) Parse.parseInt(Parse.elemDefault(node, "FameBonus", "0"));
			int projNum = Parse.parseInt(Parse.elemDefault(node, "NumProjectiles", "0"));

			Projectile p = null;
			if (Parse.hasElement(node, "Projectile")) {
				p = parseProjectile(node.selectSingleNode("Projectile"));
			}

			Item i = new Item(id, name, p, projNum, tier, slotType, rof, feed, bag, mp, fame, soulbound, useable,
					consumeable);

			items.put(id, i);
			nameToItem.put(name, i);

		}
		//JRelay.info("Loaded " + items.keySet().size() + " Items...");
	}

	public static void parseTiles(Document tileDoc) {
		List<Node> list = tileDoc.selectNodes("//Ground");

		for (Node node : list) {
			int id = Parse.parseHex(Parse.attrDefault(node, "type", "0x0"));

			String name = Parse.attrDefault(node, "id", "");

			boolean walk = Parse.hasElement(node, "NoWalk");
			float speed = Parse.parseFloat(Parse.elemDefault(node, "Speed", "1"));

			boolean sink = Parse.hasElement(node, "Sink");

			int maxDmg = Parse.parseInt(Parse.elemDefault(node, "MaxDamage", "0"));
			int minDmg = Parse.parseInt(Parse.elemDefault(node, "MinDamage", "0"));

			Tile t = new Tile(id, name, walk, speed, sink, maxDmg, minDmg);
			tiles.put(id, t);
			nameToTile.put(name, t);

		}
		//JRelay.info("Loaded " + tiles.keySet().size() + " Tiles...");
	}

	public static void parseObjects(Document objectDoc) {
		List<Node> list = objectDoc.selectNodes("//Object");

		for (Node node : list) {
			int id = Parse.parseHex(Parse.attrDefault(node, "type", "0x0"));
			String obj = Parse.elemDefault(node, "Class", "GameObject");
			String name = Parse.attrDefault(node, "id", "");

			int hp = Parse.parseHex(Parse.elemDefault(node, "MaxHitPoints", "0"));
			float xp = Parse.parseFloat(Parse.elemDefault(node, "XpMult", "0"));
			boolean stat = Parse.hasElement(node, "Static");
			boolean occ = Parse.hasElement(node, "OccupySquare");
			boolean eOcc = Parse.hasElement(node, "EnemyOccupySquare");
			boolean fOcc = Parse.hasElement(node, "FullOccupy");
			boolean block = Parse.hasElement(node, "BlocksSight");
			boolean enemy = Parse.hasElement(node, "Enemy");
			boolean player = Parse.hasElement(node, "Player");
			boolean draw = Parse.hasElement(node, "DrawOnGround");

			int size = Parse.parseInt(Parse.elemDefault(node, "Size", "0"));
			int sSize = Parse.parseInt(Parse.elemDefault(node, "ShadowSize", "0"));
			int def = Parse.parseInt(Parse.elemDefault(node, "Defense", "0"));

			boolean fly = Parse.hasElement(node, "Flying");
			boolean god = Parse.hasElement(node, "God");

			List<Node> projList = node.selectNodes("Projectile");
			ArrayList<Projectile> proj = new ArrayList<Projectile>();
			for (Node p : projList) {
				proj.add(parseProjectile(p));
			}
			Projectile[] finalP = new Projectile[projList.size()];
			finalP = proj.toArray(finalP);
			Object o = new Object(id, name, obj, hp, xp, stat, occ, eOcc, fOcc, block, enemy, player, draw, size, sSize,
					def, fly, god, finalP);
			objects.put(id, o);
			nameToObject.put(name, o);

		}
		//JRelay.info("Loaded " + objects.keySet().size() + " Objects...");
	}

	public static void parsePackets(Document packetDoc) {
		List<Node> list = packetDoc.selectNodes("//Packet");

		for (Node node : list) {
			byte id = (byte) Parse.parseInt(Parse.elemDefault(node, "PacketId", "0"));
			int baseId = Parse.parseInt(Parse.elemDefault(node, "PacketId", "0"));
			String name = Parse.elemDefault(node, "PacketName", "");

			name = name.replaceAll("[^A-Za-z0-9]", "");
			if (name.contains("_")) {
				System.out.println();
			}
			packetType.add(name);

			PacketModel p = new PacketModel(id, name);
			packets.put(id, p);
			packetIdToName.put(baseId, p);

			packetNameToId.put(name, baseId);

		}
		log.log(Level.INFO, "Loaded " + packets.keySet().size() + " Packets...");
	}

	public static void parseServers(Document serverDoc) {
		List<Node> list = serverDoc.selectNodes("//Server");

		for (Node node : list) {
			String name = Parse.elemDefault(node, "Name", "");
			String abbr = Servers.serverNames.get(name);
			if (abbr == (null)) {
				abbr = "";
			}
			String addr = Parse.elemDefault(node, "DNS", "");
			if (addr == (null)) {
				addr = "";
			}
			Server s = new Server(abbr, name, abbr, addr);
			abbrToServer.put(abbr, s);
			nameToServer.put(name, s);
			servers.put(addr, s);

		}
		//JRelay.info("Loaded " + abbrToServer.keySet().size() + " Servers...");
	}

	public static Projectile parseProjectile(Node node) {

		byte id = (byte) Parse.parseHex(Parse.attrDefault(node, "id", "0x0"));
		String name = Parse.elemDefault(node, "ObjectId", "");
		int dmg = Parse.parseInt(Parse.elemDefault(node, "Damage", "0"));
		float spd = Parse.parseFloat(Parse.elemDefault(node, "Speed", "0")) / 10000f;
		int size = Parse.parseInt(Parse.elemDefault(node, "Size", "0"));
		int life = Parse.parseInt(Parse.elemDefault(node, "Lifetime", "0"));

		int maxDmg = Parse.parseInt(Parse.elemDefault(node, "MaxDamage", "0"));
		int minDmg = Parse.parseInt(Parse.elemDefault(node, "MinDamage", "0"));

		float mag = Parse.parseFloat(Parse.elemDefault(node, "Magnitute", "0"));
		float amp = Parse.parseFloat(Parse.elemDefault(node, "Amplitude", "0"));
		float frq = Parse.parseFloat(Parse.elemDefault(node, "Frequency", "0"));

		boolean wavy = Parse.hasElement(node, "Wavy");
		boolean parametric = Parse.hasElement(node, "Parametric");
		boolean boom = Parse.hasElement(node, "Boomerang");
		boolean pierce = Parse.hasElement(node, "ArmorPiercing");
		boolean multi = Parse.hasElement(node, "MultiHit");
		boolean pass = Parse.hasElement(node, "PassesCover");

		List<Node> efx = node.selectNodes("ConditionEffect");
		HashMap<String, Float> efxMap = new HashMap<String, Float>();
		for (Node n : efx) {
			float dur = Parse.parseFloat(Parse.attrDefault(n, "duration", "0"));
			String effName =n.getText();
			efxMap.put(effName, dur);
		}
		Projectile res = new Projectile(id, name, dmg, spd, size, life, maxDmg, minDmg, mag, amp, frq, wavy, parametric,
				boom, pierce, multi, pass, efxMap);

		return res;

	}

}
