package plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.app.JRelayGUI;
import com.data.ConditionEffect;
import com.data.ConditionEffectIndex;
import com.data.GameData;
import com.data.PacketType;
import com.data.State;
import com.data.StatsType;
import com.data.shared.Entity;
import com.data.shared.StatData;
import com.data.shared.Status;
import com.event.EventUtils;
import com.event.JPlugin;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.models.Object;
import com.models.Packet;
import com.models.Projectile;
import com.models.Server;
import com.packets.client.GroundDamagePacket;
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
	float nexusPercent = JRelayGUI.anPercent;
	public static ClientState st = null;
	static int counter = 0;
	static int delay = 10;

	public AutoNexus(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attach() {

		for (Object e : GameData.objects.values()) {
			if (e.enemy) {
				ArrayList<Projectile> targetList = Lists.newArrayList(e.Projectiles);
				List<Projectile> piercing = targetList.stream().filter(x -> x.armorPiercing)
						.collect(Collectors.toList());
				List<Integer> piercingIds = piercing.stream().map(x -> (int) x.id).collect(Collectors.toList());
				for (Integer id : piercingIds) {
					Bullet.piercing.put(e.id, id);
				}
				List<Projectile> breaking = targetList.stream().filter(x -> x.StatusEffects.containsKey("Armor Broken"))
						.collect(Collectors.toList());
				List<Integer> breakingIds = breaking.stream().map(x -> (int) x.id).collect(Collectors.toList());
				for (Integer id : breakingIds) {
					Bullet.breaking.put(e.id, id);
				}

			}

			// user.HookPacket(PacketType.GROUNDDAMAGE, AutoNexus.class, OnPacket);
		}
		JRelay.info("Auto Nexus: Found " + Bullet.piercing.size() + " Armor Piercing Projectiles, Found "
				+ Bullet.breaking.size() + " Armor Breaking Projectiles.");
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
					sendToClient(EventUtils.CreateOryxNotification("AutoNexus",
							"AutoNexus at " + (nexusPercent * 100) + "%"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void onJXCommand(String command, String[] args) {
		if (args.length < 2) {
			if(this.enabled) {
				this.enabled = false;
				TextPacket packet = EventUtils.CreateOryxNotification("AutoNexus", "Autonexus disabled");
				sendToClient(packet);
			}else {
				this.enabled = true;
				TextPacket packet = EventUtils.CreateOryxNotification("AutoNexus", "Autonexus enabled");
				sendToClient(packet);
			}
			
		} else {
			try {
				int anx = Integer.parseInt(args[1]);
				this.nexusPercent = ((float) anx) / 100.0f;
				JRelayGUI.anPercent = ((float) anx) / 100.0f;
				TextPacket packet = EventUtils.CreateOryxNotification("AutoNexus",
						" AutoNexus percent now set to " + this.nexusPercent * 100);
				sendToClient(packet);
			} catch (Exception e) {
				TextPacket packet = EventUtils.CreateOryxNotification("AutoNexus",
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
		public int OwnerID;

		// the ID of the bullet
		public int ID;

		// the type of projectile
		public int ProjectileID;

		// raw damage
		public int Damage;

		public Bullet(int ownerID, int iD, int projectileID, int damage) {
			super();
			OwnerID = ownerID;
			ID = iD;
			ProjectileID = projectileID;
			Damage = damage;
		}
	}

	public class ClientState {
		public int HP = 100;

		public boolean safe = true;

		public boolean Armored = false;
		public boolean ArmorBroken = false;

		// enemy id -> projectiles
		public BiMap<Integer, ArrayList<Bullet>> BulletMap = HashBiMap.create();

		// enemy id -> enemy type
		public BiMap<Integer, Short> EnemyTypeMap = HashBiMap.create();

		public User client;

		public ClientState(User client) {
			this.client = client;
		}

		public void destroyData() {
			BulletMap.clear();
			EnemyTypeMap.clear();
		}

		public void Update(UpdatePacket update) {

			for (Entity e : update.newObjs) {
				if (!EnemyTypeMap.containsKey(e.status.objectId)) {
					Short ins = EnemyTypeMap.forcePut(e.status.objectId, e.objectType);
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

			ArmorBroken = client.playerData.hasConditionEffect(ConditionEffect.ArmorBroken);

			Armored = client.playerData.hasConditionEffect(ConditionEffect.Armored);

		}

		private void MapBullet(Bullet b) {
			if (!BulletMap.containsKey(b.OwnerID)) {

				BulletMap.forcePut(b.OwnerID, new ArrayList<Bullet>());

			}
			BulletMap.get(b.OwnerID).add(b);

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
				ArmorBroken = true;

			if (Armored)
				def *= 2;

			if (ArmorBroken)
				def = 0;

			return Math.max(Math.max(aoe.damage - def, 0), (int) (0.15f * aoe.damage));
		}

		private int PredictDamage(Bullet b) {
			int def = client.playerData.defense;

			if (EnemyTypeMap.containsKey(b.OwnerID)
					&& Bullet.IsArmorBreaking(EnemyTypeMap.get(b.OwnerID), b.ProjectileID) && !ArmorBroken) {
				ArmorBroken = true;
				sendToClient(EventUtils.createText("AutoNexus", " Armor Broken!"));
			}

			if (Armored) {
				def *= 2;
				sendToClient(EventUtils.createText("AutoNexus", " Player Is Armored!"));
			}

			if (ArmorBroken || (EnemyTypeMap.containsKey(b.OwnerID)
					&& Bullet.IsPiercing(EnemyTypeMap.get(b.OwnerID), b.ProjectileID))) {
				def = 0;
				sendToClient(EventUtils.createText("AutoNexus", " Player Armor Pierced!"));
			}
				

			return Math.max(Math.max(b.Damage - def, 0), (int) (0.15f * b.Damage));
		}

		private boolean ApplyDamage(int dmg) {
			// System.out.println("Applying damage "+dmg);
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
								sendToClient(EventUtils.CreateOryxNotification("AutoNexus",
										"Saved You At " + HP+"hp"));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					//sendToClient(EventUtils.createText("AutoNexus", "Saved with " + HP + " health!"));

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

			for (Bullet b : BulletMap.get(phit.objectId))
				if (b.ID == phit.bulletId) {
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
