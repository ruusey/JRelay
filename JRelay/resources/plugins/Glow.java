package plugins;

import com.data.PacketType;
import com.event.JPlugin;
import com.models.Packet;
import com.packets.server.UpdatePacket;
import com.relay.User;

public class Glow extends JPlugin{

	public Glow(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attach() {
		 user.hookPacket(PacketType.UPDATE, Glow.class, "onUpdatePacket");
		
	}
	public void onUpdatePacket(Packet p){
		 UpdatePacket update = (UpdatePacket)p;

         for (int i = 0; i < update.newObjs.length; i++)
         {
             if (update.newObjs[i].status.objectId == user.playerData.ownerObjectId)
             {
                 for (int j = 0; j < update.newObjs[i].status.data.length; j++)
                 {
                     if (update.newObjs[i].status.data[j].id == 59)
                         update.newObjs[i].status.data[j].intValue = 100;
                 }
             }
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
		return "JRelay.Glow";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Makes you glow!";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[] {""};
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { "UPDATE -> onUpdate()"};
	}

}
