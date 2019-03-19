package plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;

import com.app.JRelayGUI;
import com.data.ConditionEffect;
import com.data.ConditionEffectIndex;
import com.data.GameData;
import com.data.PacketType;
import com.data.StatsType;
import com.data.shared.Entity;
import com.data.shared.StatData;
import com.data.shared.Status;
import com.event.EventUtils;
import com.event.JPlugin;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.models.Object;
import com.models.Packet;
import com.models.Projectile;
import com.packets.client.PlayerHitPacket;
import com.packets.server.AOEPacket;
import com.packets.server.EnemyShootPacket;
import com.packets.server.NewTickPacket;
import com.packets.server.TextPacket;
import com.packets.server.UpdatePacket;
import com.relay.JRelay;
import com.relay.User;

public class AutoNexus extends JPlugin {
	boolean enabled = true;
	float nexusPercent = JRelay.AUTONEXUS_PERCENT;
	public static ClientState st = null;
	static int counter = 0;
	static int delay = 10;

	public AutoNexus(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attach() {
		int piercingProj = 0;
		int breakingProj = 0;
		for (Object e : GameData.objects.values()) {
			if (e.enemy) {
				ArrayList<Projectile> targetList = Lists.newArrayList(e.Projectiles);
				List<Projectile> piercing = targetList.stream().filter(x -> x.armorPiercing)
						.collect(Collectors.toList());
				List<Integer> piercingIds = piercing.stream().map(x -> (int) x.id).collect(Collectors.toList());
				for (Integer id : piercingIds) {
					piercingProj++;
					Bullet.piercing.put(e.id, id);
				}
				List<Projectile> breaking = targetList.stream().filter(x -> x.StatusEffects.containsKey("Armor Broken"))
						.collect(Collectors.toList());
				List<Integer> breakingIds = breaking.stream().map(x -> (int) x.id).collect(Collectors.toList());
				for (Integer id : breakingIds) {
					breakingProj++;
					Bullet.breaking.put(e.id, id);
				}

			}

			// user.HookPacket(PacketType.GROUNDDAMAGE, AutoNexus.class, OnPacket);
		}
		JRelayGUI.log("Auto Nexus: Found " + piercingProj + " Armor Piercing Projectiles, Found "
				+ breakingProj + " Armor Breaking Projectiles.");
		st = new ClientState(user);
		user.hookCommand("anx", AutoNexus.class, "onJXCommand");
		user.hookPacket(PacketType.UPDATE, AutoNexus.class, "UpdateAN");
		user.hookPacket(PacketType.NEWTICK, AutoNexus.class, "NewTickAN");
		user.hookPacket(PacketType.ENEMYSHOOT, AutoNexus.class, "EShootAN");
		user.hookPacket(PacketType.PLAYERHIT, AutoNexus.class, "PHitAN");
		user.hookPacket(PacketType.AOE, AutoNexus.class, "AoeAN");
		user.hookPacket(PacketType.CREATESUCCESS, AutoNexus.class, "OnConnectAN");
	}

	public void OnConnectAN(Packet pack) {
		Bullet.destroyData();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(800);
					sendToClient(EventUtils.createOryxNotification("AutoNexus",
							"AutoNexus at " + (nexusPercent * 100) + "%"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void onJXCommand(String command, String[] args) {
		if (args.length < 2) {
			if (this.enabled) {
				this.enabled = false;
				TextPacket packet = EventUtils.createOryxNotification("AutoNexus", "Autonexus disabled");
				sendToClient(packet);
			} else {
				this.enabled = true;
				TextPacket packet = EventUtils.createOryxNotification("AutoNexus", "Autonexus enabled");
				sendToClient(packet);
			}

		} else {
			try {
				int anx = Integer.parseInt(args[1]);
				this.nexusPercent = ((float) anx) / 100.0f;
				JRelay.AUTONEXUS_PERCENT = ((float) anx) / 100.0f;
				TextPacket packet = EventUtils.createOryxNotification("AutoNexus",
						" AutoNexus percent now set to " + this.nexusPercent * 100);
				sendToClient(packet);
			} catch (Exception e) {
				TextPacket packet = EventUtils.createOryxNotification("AutoNexus",
						args[1] + " must be an integer 1-99");
				sendToClient(packet);

			}
		}
	}

	public void UpdateAN(Packet p) {
		st.Update((UpdatePacket) p);
	}

	public void NewTickAN(Packet p) {
		st.Tick((NewTickPacket) p);
	}

	public void EShootAN(Packet p) {
		st.EnemyShoot((EnemyShootPacket) p);
	}

	public void PHitAN(Packet p) {
		st.PlayerHit((PlayerHitPacket) p);
	}

	public void AoeAN(Packet p) {
		st.AoE((AOEPacket) p);
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "apemanzilla";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Auto Nexus";
	}

	@Override
	public String getDescription() {
		return "Attempts to save you from death by nexusing before a fatal blow."
				+ "\nUnlike other auto nexus systems, this one compensates for piercing attacks, broken armor, and even ground damage*."
				+ "\nThis plugin will NOT make you completely invulnerable, but it will definitely help prevent you from dying!"
				+ "\n\n*The exact damage value cannot be determined when taking ground damage, so for safety's sake the plugin will assume that you took the maximum damage possible for the appropriate tile.";
	}

	@Override
	public String[] getCommands() {
		return new String[] { "/anx", "/anx [percentage] - set the percentage to go nexus at (q-99)" };
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return new String[] { "/autonexus", "/autonexus debug [on | off] - toggle debug messages on and off" };
	}

	public static class Bullet {
		public static void destroyData() {
			piercing.clear();
			breaking.clear();
		}

		/// <summary>
		/// Map of piercing projectiles
		/// Object ID -> list of piercing projectile IDs
		/// </summary>
		public static ArrayListMultimap<Integer, Integer> piercing = ArrayListMultimap.create();

		/// <summary>
		/// Map of armor break projectiles
		/// Object ID -> list of armor break projectile IDs
		/// </summary>
		public static ArrayListMultimap<Integer, Integer> breaking = ArrayListMultimap.create();

		public static boolean IsPiercing(int enemyType, int projectileType) {
			return piercing.containsKey(enemyType) && piercing.get(enemyType).contains(projectileType);
		}

		public static boolean IsArmorBreaking(int enemyType, int projectileType) {
			return breaking.containsKey(enemyType) && breaking.get(enemyType).contains(projectileType);
		}

		// owner ID of bullet
		public int ownerId;

		// the ID of the bullet
		public int id;

		// the type of projectile
		public int projectileId;

		// raw damage
		public int damage;

		public Bullet(int ownerID, int iD, int projectileID, int damage) {
			super();
			ownerId = ownerID;
			id = iD;
			projectileId = projectileID;
			this.damage = damage;
		}
	}

	public class ClientState {
		public int HP = 100;

		public boolean safe = true;

		public boolean Armored = false;
		public boolean armorBroken = false;

		// enemy id -> projectiles
		public BiMap<Integer, ArrayList<Bullet>> bulletMap = HashBiMap.create();

		// enemy id -> enemy type
		public BiMap<Integer, Short> enemyMap = HashBiMap.create();

		public User client;

		public ClientState(User client) {
			this.client = client;
		}

		public void destroyData() {
			bulletMap.clear();
			enemyMap.clear();
		}

		public void Update(UpdatePacket update) {

			for (Entity e : update.newObjs) {
				if (!enemyMap.containsKey(e.status.objectId)) {
					Short ins = enemyMap.forcePut(e.status.objectId, e.objectType);
					if (ins != null) {
						System.out.println("Added new enemyType " + e.objectType);
					}

				}
			}

		}

		public void Tick(NewTickPacket tick) {
			HP += (int) (0.2 + (client.playerData.vitality * 0.024));

			for (Status status : tick.statuses)
				if (status.objectId == client.playerData.ownerObjectId)
					for (StatData stat : status.data)
						if (stat.id == StatsType.HP.type)
							HP = stat.intValue;

			armorBroken = client.playerData.hasConditionEffect(ConditionEffect.ArmorBroken);
//			if (ArmorBroken) {
//				sendToClient(EventUtils.createText("AutoNexus", " Armor Broken!"));
//			}
//			Armored = client.playerData.hasConditionEffect(ConditionEffect.Armored);
//			if (Armored) {
//				sendToClient(EventUtils.createText("AutoNexus", " Player Armored!"));
//			}

		}

		private void MapBullet(Bullet b) {
			if (!bulletMap.containsKey(b.ownerId)) {

				bulletMap.forcePut(b.ownerId, new ArrayList<Bullet>());

			}
			bulletMap.get(b.ownerId).add(b);

		}

		public void EnemyShoot(EnemyShootPacket eshoot) {
			// System.out.println("Enemy shot detected "+eshoot.ownerId+",
			// "+eshoot.bulletId+", eshoot.containerType");
			for (int i = 0; i < eshoot.numShots; i++) {
				Bullet b = new Bullet(eshoot.ownerId, eshoot.bulletId + i, eshoot.bulletType, eshoot.damage);
				MapBullet(b);
			}
		}

		private int PredictDamage(AOEPacket aoe) {
			int def = client.playerData.defense;

			if (ConditionEffectIndex.valueOf(aoe.effect) == ConditionEffectIndex.ArmorBroken)
				armorBroken = true;

			if (Armored)
				def *= 2;

			if (armorBroken)
				def = 0;

			return Math.max(Math.max(aoe.damage - def, 0), (int) (0.15f * aoe.damage));
		}

		private int PredictDamage(Bullet b) {
			int def = client.playerData.defense;

			if (enemyMap.containsKey(b.ownerId)
					&& Bullet.IsArmorBreaking(enemyMap.get(b.ownerId), b.projectileId) && !armorBroken) {
				armorBroken = true;
			}

			if (Armored) {
				def *= 2;
			}

			if (armorBroken || (enemyMap.containsKey(b.ownerId)
					&& Bullet.IsPiercing(enemyMap.get(b.ownerId), b.projectileId))) {
				def = 0;
			}

			return Math.max(Math.max(b.damage - def, 0), (int) (0.15f * b.damage));
		}

		private boolean ApplyDamage(int dmg) {
			
			if (!safe)
				return false;
			HP -= dmg;

			if (enabled && (float) HP / client.playerData.maxHealth <= nexusPercent) {

				try {
					client.sendToServer(Packet.create(PacketType.ESCAPE));
					
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								Thread.sleep(800);
								sendToClient(EventUtils.createOryxNotification("AutoNexus",
										"Saved You At " + HP + " health!"));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				safe = false;
				return false;
			}
			return true;
		}

		public void PlayerHit(PlayerHitPacket phit) {

			for (Bullet b : bulletMap.get(phit.objectId))
				if (b.id == phit.bulletId) {
					phit.send = ApplyDamage(PredictDamage(b));
					break;
				}
		}

		public void AoE(AOEPacket aoe) {
			if (client.playerData.pos.distanceSquaredTo(aoe.pos) <= aoe.radius * aoe.radius) {
				aoe.send = ApplyDamage(PredictDamage(aoe));
			}
		}

	}

}
