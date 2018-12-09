package plugins;

import java.util.ArrayList;
import java.util.Random;
import java.util.Map.Entry;

import com.data.GameData;
import com.data.PacketType;
import com.data.shared.Entity;
import com.data.shared.Location;
import com.data.shared.Tile;
import com.event.EventUtils;
import com.event.JPlugin;
import com.models.ObjectMapper;
import com.models.Packet;
import com.packets.client.PlayerTextPacket;
import com.packets.server.TextPacket;
import com.packets.server.UpdatePacket;
import com.relay.JRelay;
import com.relay.User;

public class Core extends JPlugin {
	public boolean onTiles = false;
	public Core(User user) {
		super(user);

	}

	@Override
	public void attach() {
		user.hookCommand("hi", Core.class, "onHiCommand");
		user.hookCommand("jr", Core.class, "onJr");
		
		user.hookPacket(PacketType.UPDATE, Core.class, "onUpdatePacket");
		user.hookPacket(PacketType.TEXT, Core.class, "filterShops");

	}

	public void onHiCommand(String command, String[] args) {
		if (args.length < 2) {
			TextPacket packet = EventUtils.createText("hi", "Too few argumenets /hi [on/off]");
			sendToClient(packet);
		} else if (args[1].equals("on")) {
			TextPacket packet = EventUtils.createText("hi", "Hello! heres some player data: name="
					+ user.playerData.name + " fame=" + user.playerData.characterFame);
			sendToClient(packet);
		} else if (args[1].equals("off")) {
			TextPacket packet = EventUtils.createText("hi", "Hello! no player data requested");
			sendToClient(packet);
		}
	}
	public void onJr(String command, String[] args) {

		TextPacket packet = EventUtils.createText("JRelay", "JRelay Alpha Build "+JRelay.JRELAY_VERSION+" for RotMG"+JRelay.GAME_VERSION+". Created by Ruusey");
		sendToClient(packet);

	}
	
	
	public void filterShops(Packet p) {
		TextPacket pack = (TextPacket) p;
		if(pack.numStars<20)pack.send=false;
		
	}
	public void onUpdatePacket(Packet p) {
		
		UpdatePacket pack = (UpdatePacket) p;
		
		for (Entry<ArrayList<String>, ArrayList<String>> entry : ObjectMapper.tiles.entrySet()) {
			for (String from : entry.getKey()) {
				if (from.equals(ObjectMapper.ALL_SELECTOR)) {
					for (Tile t : pack.tiles) {
						if (entry.getValue().size() == 1) {
							t.type = GameData.nameToTile.get(entry.getValue().get(0)).id;
						} else {
							Random r = new Random();
							int index = r.nextInt(entry.getValue().size());
							t.type = GameData.nameToTile.get(entry.getValue().get(index)).id;
						}
					}
				} else {
					int id = GameData.nameToTile.get(from).id;
					for (Tile t : pack.tiles) {
						
						if (t.type == id) {
							if (entry.getValue().size() == 1) {
								t.type = GameData.nameToTile.get(entry.getValue().get(0)).id;
							} else {
								Random r = new Random();
								int index = r.nextInt(entry.getValue().size());
								t.type = GameData.nameToTile.get(entry.getValue().get(index)).id;

							}
						}
					}
				}

			}
		}
		for (Entry<ArrayList<String>, ArrayList<String>> entry : ObjectMapper.objects.entrySet()) {
			for (String from : entry.getKey()) {
				if (from.equals(ObjectMapper.ALL_SELECTOR)) {
					for (Entity t : pack.newObjs) {
						if (entry.getValue().size() == 1) {
							t.objectType = (short) GameData.nameToObject.get(entry.getValue().get(0)).id;
						} else {
							Random r = new Random();
							int index = r.nextInt(entry.getValue().size());
							t.objectType = (short) GameData.nameToObject.get(entry.getValue().get(index)).id;
						}
					}
				} else {
					int id = GameData.nameToTile.get(from).id;
					for (Entity t : pack.newObjs) {
						if (t.objectType == id) {
							if (entry.getValue().size() == 1) {
								t.objectType = (short) GameData.nameToObject.get(entry.getValue().get(0)).id;
							} else {
								Random r = new Random();
								int index = r.nextInt(entry.getValue().size());
								t.objectType = (short) GameData.nameToObject.get(entry.getValue().get(index)).id;

							}
						}
					}
				}
			}
		}
		
		sendToClient(pack);
		p.send = false;
	}
	public double distTo(int x0,int y0,float x,float y) {
		return Math.sqrt((x0-x)*(x0-x)+(y0-y)*(y0-y));
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "System";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JRelay.Core";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Test methods for plugins usage in JRelay";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] { "/hi [on/off] - prints JRelay hello world message", "/jr - prints JRelay version" };
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { "UPDATE -> onUpdatePacket()" };
	}

}
