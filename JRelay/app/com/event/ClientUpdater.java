package com.event;

import java.util.Map.Entry;

import com.data.PacketType;
import com.data.State;
import com.data.shared.Location;
import com.data.shared.PlayerData;
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

	public ClientUpdater(User user) {
		super(user);

	}

	@Override
	public void attach() {
		user.hookRequiredPacket(PacketType.CREATESUCCESS, ClientUpdater.class,
				"onCreateSuccess");
		user.hookRequiredPacket(PacketType.MAPINFO, ClientUpdater.class,
				"onMapInfo");
		user.hookRequiredPacket(PacketType.UPDATE, ClientUpdater.class, "onUpdate");
		user.hookRequiredPacket(PacketType.NEWTICK, ClientUpdater.class,
				"onNewTick");
		user.hookRequiredPacket(PacketType.PLAYERSHOOT, ClientUpdater.class,
				"onPlayerShoot");
		user.hookRequiredPacket(PacketType.MOVE, ClientUpdater.class, "onMove");
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
    	user.playerData.parse(packet);
        if (user.state.accId != null) return;

        State resolvedState = null;

        for (State cstate : JRelay.instance.userStates.values()){
        	if (cstate.accId == user.playerData.accountId){
        		resolvedState = cstate;
        	}
                
        }
        if (resolvedState == null){
        	
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
		return null;
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return null;
	}
}
