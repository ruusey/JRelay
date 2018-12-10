package plugins;

import java.util.HashMap;

import com.data.GameData;
import com.data.PacketType;
import com.data.StatsType;
import com.data.shared.Entity;
import com.data.shared.StatData;
import com.event.EventUtils;
import com.event.JPlugin;
import com.models.Packet;
import com.packets.server.TextPacket;
import com.packets.server.UpdatePacket;
import com.relay.User;

public class IncFinder extends JPlugin {
	public int INC_ID = GameData.nameToItem.get("Wine Cellar Incantation").id;
	public HashMap<Integer, String> incHolders;

	public IncFinder(User user) {
		super(user);

	}

	@Override
	public void attach() {
		incHolders = new HashMap<Integer, String>();
		user.hookPacket(PacketType.UPDATE, IncFinder.class, "onUpdatePacket");
		user.hookCommand("inc", IncFinder.class, "onInc");

	}

	public void onUpdatePacket(Packet p) {
		UpdatePacket update = (UpdatePacket) p;

		// New Objects
		for (Entity entity : update.newObjs) {
			boolean inc = false;

			for (StatData statData : entity.status.data) {
				if (!statData.isUTFData() && (statData.id >= 8 && statData.id <= 19)
						|| (statData.id >= 71 && statData.id <= 78))
					if (statData.intValue == INC_ID)
						inc = true;

				if ((inc && statData.id == StatsType.Name.type)
						&& !(statData.stringValue.equals(user.playerData.name))) {
					if (!(incHolders.containsKey(entity.status.objectId)))
						incHolders.put(entity.status.objectId, statData.stringValue);
					TextPacket pak = EventUtils.createText("IncFinder", statData.stringValue + " has an Incantation!");
					sendToClient(pak);

				}
			}
		}
		for (int drop : update.drops) {
			if (incHolders.containsKey(drop)) {
				TextPacket pak = EventUtils.createText("IncFinder", incHolders.get(drop) + " has left!");
				sendToClient(pak);
				incHolders.remove(drop);
			}
		}
	}

	public void onInc(String command, String[] args) {
		String msg = "Inc holders: ";
		if (incHolders.size() > 0) {
			for (String s : incHolders.values()) {
				msg += s + ", ";
			}
		} else {
			msg = "No inc holders detected";
		}

		msg = msg.substring(0, msg.length() - 1);
		TextPacket packet = EventUtils.createText("IncFinder", msg);
		sendToClient(packet);

	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "System";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "JRelay.IncFinder";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Alerts the player when a user holding an incantation is nearby";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] { "/inc - displays a list of players holding incs in your realm" };
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { "UPDATE -> onUpdatePacket()" };
	}

}
