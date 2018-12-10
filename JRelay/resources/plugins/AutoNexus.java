package plugins;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

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
import com.models.Object;
import com.models.Packet;
import com.models.Projectile;
import com.packets.client.GroundDamagePacket;
import com.packets.client.PlayerHitPacket;
import com.packets.server.AOEPacket;
import com.packets.server.EnemyShootPacket;
import com.packets.server.NewTickPacket;
import com.packets.server.UpdatePacket;
import com.relay.JRelay;
import com.relay.User;

public class AutoNexus extends JPlugin {
	boolean enabled=true;
	float nexusPercent = 0.9f;
	ClientState st = null;
	public AutoNexus(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void attach() {
		for(Object e : GameData.objects.values()) {
			if(!e.enemy)continue;
			
                Bullet.piercing.put(e.id, new ArrayList<Integer>());
                Bullet.breaking.put(e.id, new ArrayList<Integer>());
                for(Projectile proj: e.Projectiles )
                {
                    if (proj.armorPiercing)
                        Bullet.piercing.get(e.id).add((int) proj.id);
                    if (proj.StatusEffects.containsKey("Armor Broken"))
                        Bullet.breaking.get(e.id).add((int) proj.id);
                };
                

            
            //user.HookPacket(PacketType.GROUNDDAMAGE, AutoNexus.class, OnPacket);
		}
		JRelay.info("Auto Nexus: Found "+Bullet.piercing.size()+" Armor Piercing Projectiles, Found "+Bullet.breaking.size()+" Armor Breaking Projectiles.");
		st = new ClientState(user);
        //user.hookCommand("autonexus", AutoNexus.class,"OnCommand");
        user.hookPacket(PacketType.UPDATE, AutoNexus.class, "OnPacket");
        user.hookPacket(PacketType.NEWTICK, AutoNexus.class, "OnPacket");
        user.hookPacket(PacketType.ENEMYSHOOT, AutoNexus.class, "OnPacket");
        user.hookPacket(PacketType.PLAYERHIT, AutoNexus.class, "OnPacket");
        user.hookPacket(PacketType.AOE, AutoNexus.class, "OnPacket");
		
	}
	public void OnPacket(Packet p)
    {
		
           
            int id = (int)p.id();
			if (id == (PacketType.UPDATE.id)) {
				st.Update((UpdatePacket)p);
			} else if (id == PacketType.NEWTICK.id) {
				st.Tick((NewTickPacket)p);
			} else if (id == PacketType.ENEMYSHOOT.id) {
				st.EnemyShoot((EnemyShootPacket)p);
			} else if (id == PacketType.PLAYERHIT.id) {
				st.PlayerHit((PlayerHitPacket)p);
			} else if (id == PacketType.AOE.id) {
				st.AoE((AOEPacket)p);
			}
        
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
		return "Attempts to save you from death by nexusing before a fatal blow." +
                "\nUnlike other auto nexus systems, this one compensates for piercing attacks, broken armor, and even ground damage*." +
                "\nThis plugin will NOT make you completely invulnerable, but it will definitely help prevent you from dying!" +
                "\n\n*The exact damage value cannot be determined when taking ground damage, so for safety's sake the plugin will assume that you took the maximum damage possible for the appropriate tile.";
	}

	@Override
	public String[] getCommands() {
		return new String[] {
                "/autonexus",
                "/autonexus [percentage] - set the percentage to go nexus at (0-99)",
                "/autonexus [on | off] - toggle autonexus on and off",
                "/autonexus debug [on | off] - toggle debug messages on and off"
            };
	}

	@Override
	public String[] getPackets() {
		// TODO Auto-generated method stub
		return  new String[] {
                "/autonexus",
                "/autonexus debug [on | off] - toggle debug messages on and off"
            };
	}
	private static class Bullet
    {
        /// <summary>
        /// Map of piercing projectiles
        /// Object ID -> list of piercing projectile IDs
        /// </summary>
        public static Hashtable<Integer, ArrayList<Integer>> piercing = new Hashtable<Integer, ArrayList<Integer>>();

        /// <summary>
        /// Map of armor break projectiles
        /// Object ID -> list of armor break projectile IDs
        /// </summary>
        public static Hashtable<Integer, ArrayList<Integer>> breaking = new Hashtable<Integer, ArrayList<Integer>>();

        public static boolean IsPiercing(int enemyType, int projectileType)
        {
            return piercing.contains(enemyType) && piercing.get(enemyType).contains(projectileType);
        }

        public static boolean IsArmorBreaking(int enemyType, int projectileType)
        {
            return breaking.contains(enemyType) && breaking.get(enemyType).contains(projectileType);
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
	private class ClientState
    {
        public int HP = 100;

        public boolean safe = true;

        public boolean Armored = false;
        public boolean ArmorBroken = false;

        // enemy id -> projectiles
        public Hashtable<Integer, ArrayList<Bullet>> BulletMap = new Hashtable<Integer, ArrayList<Bullet>>();

        // enemy id -> enemy type
        public Hashtable<Integer, Short> EnemyTypeMap = new Hashtable<Integer, Short>();

        public User client;

        public ClientState(User client)
        {
            this.client = client;
        }

        public void Update(UpdatePacket update)
        {
            for (Entity e : update.newObjs)
                if (!EnemyTypeMap.containsKey(e.status.objectId))
                    EnemyTypeMap.replace(e.status.objectId,e.objectType);
        }

        public void Tick(NewTickPacket tick)
        {
            HP += (int)(0.2 + (client.playerData.vitality * 0.024));

            for (Status status : tick.statuses)
                if (status.objectId == client.playerData.ownerObjectId)
                    for (StatData stat : status.data)
                        if (stat.id == StatsType.HP.type)
                            HP = stat.intValue;

            ArmorBroken = client.playerData.hasConditionEffect(ConditionEffect.ArmorBroken);
            Armored = client.playerData.hasConditionEffect(ConditionEffect.Armored);
        }

        private void MapBullet(Bullet b)
        {
            if (!BulletMap.containsKey(b.OwnerID))
                BulletMap.replace(b.OwnerID, new ArrayList<Bullet>());

            BulletMap.get(b.OwnerID).add(b);
        }

        public void EnemyShoot(EnemyShootPacket eshoot)
        {
            for (int i = 0; i < eshoot.numShots; i++)
            {
                Bullet b = new Bullet
                (
                    eshoot.ownerId,
                    eshoot.bulletId + i,
                    eshoot.containerType,
                     eshoot.damage
                );
                MapBullet(b);
            }
        }

        private int PredictDamage(AOEPacket aoe)
        {
            int def = client.playerData.defense;

            if (aoe.effect == ConditionEffectIndex.ArmorBroken.index)
                ArmorBroken = true;

            if (Armored) def *= 2;

            if (ArmorBroken) def = 0;

            return Math.max(Math.max(aoe.damage - def, 0), (int)(0.15f * aoe.damage));
        }

        private int PredictDamage(Bullet b)
        {
            int def = client.playerData.defense;

            if (EnemyTypeMap.containsKey(b.OwnerID) && Bullet.IsArmorBreaking(EnemyTypeMap.get(b.OwnerID), b.ProjectileID) && !ArmorBroken)
            {
                ArmorBroken = true;

                
            }

            if (Armored) def *= 2;

            if (ArmorBroken || (EnemyTypeMap.containsKey(b.OwnerID) && Bullet.IsPiercing(EnemyTypeMap.get(b.OwnerID), b.ProjectileID)))
                def = 0;

            return Math.max(Math.max(b.Damage - def, 0), (int)(0.15f * b.Damage));
        }

        private boolean ApplyDamage(int dmg)
        {
            if (!safe) return false;
            HP -= dmg;

            
            if (enabled && (float)HP / client.playerData.maxHealth <= nexusPercent)
            {
                
                try {
					client.sendToServer(Packet.create(PacketType.ESCAPE));
					user.sendToClient(EventUtils.CreateOryxNotification("AutoNexus", "Saved Your ASS!"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                safe = false;
                return false;
            }
            return true;
        }

        public void PlayerHit(PlayerHitPacket phit)
        {
            for (Bullet b : BulletMap.get(phit.objectId))
                if (b.ID == phit.bulletId)
                {
                    phit.send = ApplyDamage(PredictDamage(b));
                    break;
                }
        }

        public void AoE(AOEPacket aoe)
        {
            if (client.playerData.pos.distanceSquaredTo(aoe.pos) <= aoe.radius * aoe.radius)
            {
                aoe.send = ApplyDamage(PredictDamage(aoe));
            }
        }

        
    }

}
