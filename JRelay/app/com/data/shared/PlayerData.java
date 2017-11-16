package com.data.shared;

import com.data.CharacterClass;
import com.data.ConditionEffect;
import com.data.StatsType;
import com.packets.server.MapInfoPacket;
import com.packets.server.NewTickPacket;
import com.packets.server.UpdatePacket;
import com.data.shared.Entity;
public class PlayerData // TODO: Add the rest of the stats
{
    public int ownerObjectId;
    public String mapName;
    public boolean teleportAllowed;
    public int mapWidth;
    public int mapHeight;
    public int maxHealth;
    public int health;
    public int maxMana;
    public int mana;
    public int xpGoal;
    public int xp;
    public int level = 1;
    public int[] slot = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    public int[] backPack = { -1, -1, -1, -1, -1, -1, -1, -1 };
    public int attack;
    public int defense;
    public int speed;
    public int vitality;
    public int wisdom;
    public int dexterity;
    public int effects;
    public int stars;
    public String name;
    public int realmGold;
    public int price;
    public boolean canEnterPortal;
    public String accountId;
    public int accountFame;
    public int healthBonus;
    public int manaBonus;
    public int attackBonus;
    public int defenseBonus;
    public int speedBonus;
    public int vitalityBonus;
    public int wisdomBonus;
    public int dexterityBonus;
    public int nameChangeRankRequired;
    public boolean nameChosen;
    public int characterFame;
    public int characterFameGoal;
    public boolean glowingEffect;
    public String guildName;
    public int guildRank;
    public int breath;
    public int healthPotionCount;
    public int magicPotionCount;
    public boolean hasBackpack;
    public int skin;
    public Location pos = new Location();
    public CharacterClass cls;

    public PlayerData(int ownerObjectId)
    {
        this.ownerObjectId = ownerObjectId;
        name = "";
    }

    public PlayerData(int ownerObjectId, MapInfoPacket mapInfo)
    {
        this.ownerObjectId = ownerObjectId;
        name = "";
        mapName = mapInfo.name;
        teleportAllowed = mapInfo.allowPlayerTeleport;
        mapWidth = mapInfo.width;
        mapHeight = mapInfo.height;
    }

    public void parse(UpdatePacket update)
    {
        for (Entity newObject : update.newObjs){
        	if (newObject.status.objectId == ownerObjectId){
                cls = CharacterClass.valueOf(newObject.objectType);
                for (StatData data : newObject.status.data)
                parse(data.id, data.intValue, data.stringValue);
            }
        	
        }
            
    }

    public void parse(NewTickPacket newTick)
    {
        for (Status status : newTick.statuses)
            if (status.objectId == ownerObjectId)
                for (StatData data : status.data)
                {
                    pos = status.pos;
                    parse(data.id, data.intValue, data.stringValue);
                }
    }

    public void parse(int id, int intValue, String stringValue)
    {
        if (id == StatsType.MaximumHP.type) maxHealth = intValue;
        else if (id == StatsType.HP.type) health = intValue;
        else if (id == StatsType.MaximumMP.type) maxMana = intValue;
        else if (id == StatsType.MP.type) mana = intValue;
        else if (id == StatsType.NextLevelExperience.type) xpGoal = intValue;
        else if (id == StatsType.Experience.type) xp = intValue;
        else if (id == StatsType.Level.type) level = intValue;
        else if (id == StatsType.Inventory0.type) slot[0] = intValue;
        else if (id == StatsType.Inventory1.type) slot[1] = intValue;
        else if (id == StatsType.Inventory2.type) slot[2] = intValue;
        else if (id == StatsType.Inventory3.type) slot[3] = intValue;
        else if (id == StatsType.Inventory4.type) slot[4] = intValue;
        else if (id == StatsType.Inventory5.type) slot[5] = intValue;
        else if (id == StatsType.Inventory6.type) slot[6] = intValue;
        else if (id == StatsType.Inventory7.type) slot[7] = intValue;
        else if (id == StatsType.Inventory8.type) slot[8] = intValue;
        else if (id == StatsType.Inventory9.type) slot[9] = intValue;
        else if (id == StatsType.Inventory10.type) slot[10] = intValue;
        else if (id == StatsType.Inventory11.type) slot[11] = intValue;
        else if (id == StatsType.Attack.type) attack = intValue;
        else if (id == StatsType.Defense.type) defense = intValue;
        else if (id == StatsType.Speed.type) speed = intValue;
        else if (id == StatsType.Vitality.type) vitality = intValue;
        else if (id == StatsType.Wisdom.type) wisdom = intValue;
        else if (id == StatsType.Dexterity.type) dexterity = intValue;
        else if (id == StatsType.Effects.type) effects = intValue;
        else if (id == StatsType.Stars.type) stars = intValue;
        else if (id == StatsType.Name.type) name = stringValue;
        else if (id == StatsType.Credits.type) realmGold = intValue;
        else if (id == StatsType.MerchandisePrice.type) price = intValue;
        //else if (id == 37) CanEnterPortal = bool.Parse(stringValue);
        else if (id == StatsType.AccountId.type) accountId = stringValue;
        else if (id == StatsType.AccountFame.type) accountFame = intValue; //fame you got when you died
        else if (id == StatsType.HealthBonus.type) healthBonus = intValue;
        else if (id == StatsType.ManaBonus.type) manaBonus = intValue;
        else if (id == StatsType.AttackBonus.type) attackBonus = intValue;
        else if (id == StatsType.DefenseBonus.type) defenseBonus = intValue;
        else if (id == StatsType.SpeedBonus.type) speedBonus = intValue;
        else if (id == StatsType.VitalityBonus.type) vitalityBonus = intValue;
        else if (id == StatsType.WisdomBonus.type) wisdomBonus = intValue;
        else if (id == StatsType.DexterityBonus.type) dexterityBonus = intValue;
        else if (id == StatsType.RankRequired.type) nameChangeRankRequired = intValue;
        else if (id == StatsType.NameChosen.type) nameChosen = intValue > 0;
        else if (id == StatsType.CharacterFame.type) characterFame = intValue; //fame on this character
        else if (id == StatsType.CharacterFameGoal.type) characterFameGoal = intValue;
        else if (id == StatsType.Glowing.type) glowingEffect = intValue > -1;
        else if (id == StatsType.GuildName.type) guildName = stringValue;
        else if (id == StatsType.GuildRank.type) guildRank = intValue;
        else if (id == StatsType.OxygenBar.type) breath = intValue;
        else if (id == StatsType.HealthPotionCount.type) healthPotionCount = intValue;
        else if (id == StatsType.MagicPotionCount.type) magicPotionCount = intValue;
        else if (id == StatsType.Backpack0.type) backPack[0] = intValue;
        else if (id == StatsType.Backpack1.type) backPack[1] = intValue;
        else if (id == StatsType.Backpack2.type) backPack[2] = intValue;
        else if (id == StatsType.Backpack3.type) backPack[3] = intValue;
        else if (id == StatsType.Backpack4.type) backPack[4] = intValue;
        else if (id == StatsType.Backpack5.type) backPack[5] = intValue;
        else if (id == StatsType.Backpack6.type) backPack[6] = intValue;
        else if (id == StatsType.Backpack7.type) backPack[7] = intValue;
        else if (id == StatsType.HasBackpack.type) hasBackpack = intValue > 0;
        else if (id == StatsType.Skin.type) skin = intValue;
    }

    public boolean hasConditionEffect(ConditionEffect effect)
    {
        return (effects & effect.type) != 0;
    }

    public float tilesPerTick()
    {
        // Ticks per second = 5
        return (4.0f + 5.6f * (speed / 75.0f)) / 5.0f;
    }

    
}
