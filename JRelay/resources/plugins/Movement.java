package plugins;

import java.awt.Event;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.ChangeEvent;

import com.app.JRelayGUI;
import com.data.CharacterClass;
import com.data.PacketType;
import com.data.PortalData;
import com.data.shared.Entity;
import com.data.shared.Location;
import com.data.shared.PlayerData;
import com.data.shared.StatData;
import com.data.shared.Status;
import com.event.JPlugin;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.models.Packet;
import com.move.events.HealthChangedEventArgs;
import com.move.events.KeyEventArgs;
import com.move.events.LogEventsArgs;
import com.move.models.Key;
import com.move.models.Keys;
import com.move.models.Target;
import com.packets.client.GoToAckPacket;
import com.packets.client.UsePortalPacket;
import com.packets.server.GoToPacket;
import com.packets.server.MapInfoPacket;
import com.packets.server.NewTickPacket;
import com.packets.server.UpdatePacket;
import com.relay.User;

import jdk.internal.joptsimple.util.RegexMatcher;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

public class Movement extends JPlugin {

	public int tickThreshold = 10;
	public Location realmLocation = new Location();
	public Location fountainLocation = new Location();

	private ArrayList<Target> targets;
	private ArrayList<PortalData> portals;
	private HashMap<Integer, Target> playerPositions;

	private Location lastLocation = null;
	private Location lastAverage;
	private boolean blockNextAck = false;
	private boolean followTarget;
	private int tickCount;
	private boolean gotoRealm;
	private boolean enabled = true;
	private boolean isInNexus;
	private String currentMapName;
	boolean wPressed;
	boolean aPressed;
	boolean sPressed;
	boolean dPressed;
	private float followThreshold = 1.5f;
	public class HealthEventHandler {
		  @Subscribe public void healthChanged(User u, HealthChangedEventArgs args) {
		    user.playerData.health=(int) args.getHealth();
		  }
		}
	public class KeyEventHandler {
		  @Subscribe public void keyChanged(User u, KeyEventArgs args) {
		   if(args.getValue()) {
			   JRelayGUI.kb.keyPress(Key.codes.get(args.getKey().toString()));
		   }else {
			   JRelayGUI.kb.keyRelease(Key.codes.get(args.getKey().toString()));
		   }
		  }
		}
	public class LogHandler {
		  @Subscribe public void logMessage(User u, LogEventsArgs args) {
		    JRelayGUI.log("Client: "+user.playerData.name+" "+args.getMessage());
		  }
		}
	public static EventBus bus = new EventBus();
	
	public Movement(User user) {
		super(user);
		bus.register(new LogHandler());
	}
	 public class A_PRESSED extends Movement {
		 
	        public A_PRESSED(User user) {
			super(user);
			// TODO Auto-generated constructor stub
		}
			public Boolean get() {
	            return this.aPressed;
	        }
	        public void set(Boolean value) {
	            if ((this.aPressed == value)) {
	                return;
	            }
	            
	            this.aPressed = value;
	           // Window.SendMessage(this.flashPtr, value, Question, 256U, :, 257U, new IntPtr(65), IntPtr.Zero);
	      
	            
	            bus.post(new KeyEventArgs(Keys.valueOf("a"), value));
	        }
	    }
	@Override
	public void attach() {
		realmLocation.x = 134f;
		realmLocation.y = 112f;

		fountainLocation.x = 134f;
		fountainLocation.y = 134f;
		targets = new ArrayList<Target>();
		playerPositions = new HashMap<Integer, Target>();
		portals = new ArrayList<PortalData>();

		user.hookPacket(PacketType.UPDATE, Movement.class, "OnUpdate");
		// user.hookPacket(PacketType.NEWTICK, Movement.class, "OnNewTick");
		// user.hookPacket(PacketType.PLAYERHIT, Movement.class, "OnHit");
		user.hookPacket(PacketType.MAPINFO, Movement.class, "OnMapInfo");
		// user.hookPacket(PacketType.TEXT, Movement.class, "OnText");
		user.hookPacket(PacketType.GOTOACK, Movement.class, "OnGotoAck");
	}

	public void OnGotoAck(Packet p) {
		// GoToAckPacket packet = (GoToAckPacket)p;
		if (blockNextAck) {
			p.send = false;
			blockNextAck = false;
		}
	}

	public void OnMapInfo(Packet p) {
		MapInfoPacket packet = (MapInfoPacket) p;
		if (packet == null)
			return;
		portals.clear();
		currentMapName = packet.name;

		if (packet.name.equals("Nexus") && enabled) {
			// If the new map is the nexus, start moving towards the realms again.
			isInNexus = true;
			gotoRealm = true;
			MoveToRealms(user, false);
		} else {
			gotoRealm = false;
			if (enabled)
				followTarget = true;
		}
	}

	public void MoveToRealms(User client, boolean realmChosen) {
		if (client == null) {
			System.out.println("No client passed to MoveToRealms.");
			return;
		}
		Location target = realmLocation;

		if (client.playerData == null) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MoveToRealms(client, false);
			return;
		}

		float healthPercentage = (float) client.playerData.health / (float) client.playerData.maxHealth;
		if (healthPercentage < 0.95f)
			target = fountainLocation;
		String preferredRealmName = null;
		String bestName = "";
		if ((client.playerData.pos.y < -realmLocation.y + 1f && client.playerData.pos.y != 0) || realmChosen) {
			// When the client reaches the portals, evaluate the best option.
			if (portals.size() != 0) {
				boolean hasNoPreferredRealm = true;
				// Is there a preferred realm?
				if (true) {
//                    if (portals.stream().anyMatch(predicate)(ptl -> string.Compare(ptl.Name, preferredRealmName, true) == 0))
//                    {
//                        hasNoPreferredRealm = false;
//                        Portal preferred = portals.Single(ptl -> string.Compare(ptl.Name, preferredRealmName, true) == 0);
//                        target = preferred.Location;
//                        bestName = preferred.Name;
//                        realmChosen = true;
//                    } else
					{
						// The preferred realm doesn't exist anymore.
						// client.sendClientPacket(packet);(preferredRealmName + " not found. Choosing
						// new realm");
						JRelayGUI.log("The realm \"" + preferredRealmName
								+ "\" was not found. Choosing the best realm instead...");
						preferredRealmName = null;
					}
				}

				if (hasNoPreferredRealm) {
					int bestCount = 0;
					if (portals.stream().filter(ptl -> ptl.population == 85).collect(Collectors.toList()).size() > 1) {
						for (PortalData pdata : portals.stream().filter(ptl -> ptl.population == 85)
								.collect(Collectors.toList())) {
							int count = playerPositions.values().stream()
									.filter(plr -> plr.getPosition().distanceSquaredTo(pdata.loc) <= 4)
									.collect(Collectors.toList()).size();
							if (count > bestCount) {
								bestCount = count;
								bestName = pdata.name;
								target = pdata.loc;
								realmChosen = true;
							}
						}
					} else {
						PortalData ptl = portals.stream().max(Comparator.comparing(PortalData::getPopulation)).get();
						target = ptl.loc;
						bestName = ptl.name;
						realmChosen = true;
					}
				}
			} else
				target = realmLocation;
		}

		CalculateMovement(client, target, 0.5f);

		if (client.playerData.pos.distanceTo(target) < 1f && portals.size() != 0) {
			if (client.playerData.pos.distanceTo(target) < -client.playerData.tilesPerTick()
					&& client.playerData.pos.distanceTo(target) > 0.01f) {
				if (client.connected) {
					// ResetAllKeys();
					GoToPacket gotoPacket = null;
					try {
						gotoPacket = (GoToPacket) Packet.create(PacketType.GOTO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gotoPacket.pos = target;
					gotoPacket.objectId = client.playerData.ownerObjectId;
					blockNextAck = true;
					client.sendToClient(gotoPacket);
				}
			}
//            if (client.state.lastRealm!=null)
//            {
//                // If the best realm is the last realm the client is connected to, send a reconnect.
//                JRelayGUI.log("Last realm is still the best realm. Sending reconnect.");
//                if (client.connect(client.state.lastHello))
//                {
//                    gotoRealm = false;
//                    return;
//                }
//            }

			JRelayGUI.log("Attempting connection.");
			gotoRealm = false;

			// AttemptConnection(client, portals.sort(() -> {plt->
			// Collections.min(ptl.loc.DistanceSquaredTo(client.playerData.pos);}));

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (gotoRealm) {
				MoveToRealms(client, realmChosen);
			} else {
				JRelayGUI.log("Stopped moving to realm.");
			}
		}
	}

	public void AttemptConnection(User client, int portalId) {
		UsePortalPacket packet = null;
		try {
			packet = (UsePortalPacket) Packet.create(PacketType.USEPORTAL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		packet.objectId = portalId;

		if (portals.stream().filter(ptl -> ptl.id == portalId) != null) {
			gotoRealm = true;
			MoveToRealms(client, false);
			return;
		}

		// Get the player count of the current portal. The packet should
		// only be sent if there is space for the player to enter.
		int pCount = portals.stream().filter(p -> p.id == portalId).findFirst().get().population;
		if (client.connected && pCount < 85)
			client.sendToServer(packet);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (client.connected && enabled)
			AttemptConnection(client, portalId);
		else if (enabled)
			JRelayGUI.log("Connection successful.");
		else
			JRelayGUI.log("Bot disabled, cancelling connection attempt.");
	}

	public void OnUpdate(Packet p)
    {
        UpdatePacket packet = (UpdatePacket)p;

        // Get new info.
        for (Entity obj : packet.newObjs){
            // Player info.
            if(CharacterClass.valueOf((int)obj.objectType)!=null)
            {
                PlayerData playerData = new PlayerData(obj.status.objectId);
                playerData.cls = CharacterClass.valueOf((int)obj.objectType);
                playerData.pos = obj.status.pos;
                for (StatData data : obj.status.data)
                {
                    playerData.parse(data.id, data.intValue, data.stringValue);
                }

                if (playerPositions.containsKey(obj.status.objectId))
                    playerPositions.remove(obj.status.objectId);
                playerPositions.put(obj.status.objectId, new Target(obj.status.objectId, playerData.name, playerData.pos));
            }
            // Portals.
            if (obj.objectType == 1810)
            {
                for (StatData data : obj.status.data)
                {
                    if (data.stringValue != null)
                    {
                        // Get the portal info.
                        // This regex matches the name and the player count of the portal.
                        String pattern = "\\.(\\w+) \\((\\d+)";
                        
                        String[] match = data.stringValue.split(pattern);
                        PortalData portal = new PortalData(Integer.parseInt(match[1].replace("/", "").replace(")", "")),obj.status.objectId, obj.status.pos, match[0]);
                        if (portals.stream().anyMatch(ptl -> ptl.id == obj.status.objectId))
                            portals.removeIf(ptl -> ptl.id == obj.status.objectId);
                        portals.add(portal);
                    }
                }
            }
            // Enemies. Only look for enemies if EnableEnemyAvoidance is true.
            
            // Obstacles.
            
        }

        // Remove old info
        for (int dropId : packet.drops)
        {
            // Remove from players list.
            if (playerPositions.containsKey(dropId))
            {
                if (followTarget && targets.stream().anyMatch(t -> t.getObjectId() == dropId))
                {
                    // If one of the players who left was also a target, remove them from the targets list.
                    targets.removeIf(t -> t.getObjectId() == dropId);
                    JRelayGUI.log(String.format("Dropping \"{0}\" from targets.", playerPositions.get(dropId).getName()));
                    if (targets.size() == 0)
                    {
                    	
                    }
                }
                playerPositions.remove(dropId);
            }

            // Remove from enemies list.
           

            if (portals.removeIf(ptl -> ptl.id == dropId))
            	portals.removeIf(ptl -> ptl.id == dropId);
        }
    }

	private void CalculateMovement(User client, Location targetPosition, float tolerance) {
		// Left or right
		if (client.playerData.pos.x < targetPosition.x - tolerance) {
			// Move right
			JRelayGUI.kb.pressKeys("d");
			JRelayGUI.kb.releaseKeys("a");
		} else if (client.playerData.pos.x <= targetPosition.x + tolerance) {
			// Stop moving
			JRelayGUI.kb.releaseKeys("d");
		}
		if (client.playerData.pos.x > targetPosition.x + tolerance) {
			// Move left
			JRelayGUI.kb.pressKeys("a");
			JRelayGUI.kb.releaseKeys("d");
		} else if (client.playerData.pos.x >= targetPosition.x - tolerance) {
			// Stop moving
			JRelayGUI.kb.releaseKeys("a");
		}

		// Up or down
		if (client.playerData.pos.y < targetPosition.y - tolerance) {
			// Move down
			JRelayGUI.kb.pressKeys("s");
			JRelayGUI.kb.releaseKeys("w");
		} else if (client.playerData.pos.y <= targetPosition.y + tolerance) {
			// Stop moving
			JRelayGUI.kb.releaseKeys("s");
		}
		if (client.playerData.pos.y > targetPosition.y + tolerance) {
			// Move up
			JRelayGUI.kb.releaseKeys("s");
			;
			JRelayGUI.kb.pressKeys("w");
		} else if (client.playerData.pos.y >= targetPosition.y - tolerance) {
			// Stop moving
			JRelayGUI.kb.releaseKeys("w");
		}
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Ruusey";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Movement";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Movement";
	}

	@Override
	public String[] getCommands() {
		// TODO Auto-generated method stub
		return new String[0];
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[0];
	}

}
