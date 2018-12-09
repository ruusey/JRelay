package plugins;

import java.util.Map.Entry;

import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.data.shared.Location;
import com.data.shared.PlayerData;
import com.data.shared.Tile;
import com.event.EventUtils;
import com.event.JPlugin;
import com.models.Packet;
import com.packets.client.MovePacket;
import com.packets.client.PlayerShootPacket;
import com.packets.server.CreateSuccessPacket;
import com.packets.server.MapInfoPacket;
import com.packets.server.NewTickPacket;
import com.packets.server.UpdatePacket;
import com.relay.JRelay;
import com.relay.User;

public class ClientUpdater extends JPlugin {
	public boolean onTiles=false;
	public ClientUpdater(User user) {
		super(user);

	}

	@Override
	public void attach() {
		user.hookCommand("tile", ClientUpdater.class, "onTiles");
		user.hookPacket(PacketType.CREATESUCCESS, ClientUpdater.class,
				"onCreateSuccess");
		user.hookPacket(PacketType.MAPINFO, ClientUpdater.class,
				"onMapInfo");
		user.hookPacket(PacketType.UPDATE, ClientUpdater.class, "onUpdate");
		user.hookPacket(PacketType.NEWTICK, ClientUpdater.class,
				"onNewTick");
		user.hookPacket(PacketType.PLAYERSHOOT, ClientUpdater.class,
				"onPlayerShoot");
		user.hookPacket(PacketType.MOVE, ClientUpdater.class, "onMove");
	}
	public void onTiles(String command, String[] args) {
		onTiles=true;
		
	}
	public void onMove(Packet pack) {
		MovePacket packet = (MovePacket)pack;
		user.previousTime = packet.time;
		user.lastUpdate = System.currentTimeMillis();
		user.playerData.pos = packet.newPosition;
	}
	
	public void onPlayerShoot(Packet pack) {
		PlayerShootPacket packet = (PlayerShootPacket)pack;
		float x = packet.startingPos.x - 0.3f * (float) Math.cos(packet.angle);
		float y = packet.startingPos.y - 0.3f * (float) Math.sin(packet.angle);
		Location loc = new Location();
		loc.x = x;
		loc.y = y;
		user.playerData.pos = loc;

	}

	public void onNewTick(Packet pack) {
		NewTickPacket packet = (NewTickPacket)pack;
		user.playerData.parse(packet);
	}

	public void onMapInfo(Packet pack) {
		MapInfoPacket packet = (MapInfoPacket)pack;
		user.state.locationName=packet.name;
		user.state.setState("MapInfo", packet);
		//sendToClient(packet);
	}

	public void onCreateSuccess(Packet pack) {
		CreateSuccessPacket packet = (CreateSuccessPacket)pack;
		user.playerData = new PlayerData(packet.objectId,
				(MapInfoPacket) user.state.getState("MapInfo"));
	}

	public void onUpdate(Packet pack)
    {
		UpdatePacket packet = (UpdatePacket)pack;
		if(onTiles) {
			for (Tile t : packet.tiles) {
				System.out.print(t.type+", ");
				Location l = new Location();
				l.x=t.x;
				l.y=t.y;
				float dist = user.playerData.pos.distanceTo(l);
				System.out.println(dist);
				if(dist<=1.0) {
					sendToClient(EventUtils.createNotification(
							user.playerData.ownerObjectId, "On tile id: "+t.type+", name: "+GameData.tiles.get(t.type).name));
				}
				
			}
		}
		onTiles=false;
    	user.playerData.parse(packet);
        if (user.state.accId != null) return;

        State resolvedState = null;

        for (State cstate : JRelay.instance.userStates.values()){
        	if(cstate.accId==null) continue;
        	if (cstate.accId.equals(user.playerData.accountId)){
        		resolvedState = cstate;
        	}
                
        }
        if (resolvedState == null){
        	user.state.accId = user.playerData.accountId;
        }else{
            for (Entry<String,Object> pair : user.state.states.entrySet())
                resolvedState.setState(pair.getKey(), pair.getValue());
            for (Entry<String,Object> pair : user.state.states.entrySet())
                resolvedState.setState(pair.getKey(), pair.getValue());
            
            user.state = resolvedState;
        }
    }

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "System";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JRelay.ClientUpdater";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Updates the client with useful information provided by the server";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] {};
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { };
	}
}
