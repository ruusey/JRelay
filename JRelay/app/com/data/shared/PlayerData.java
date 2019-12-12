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
                this.pos = newObject.status.pos;
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
        if (id== StatsType.maxHp.type) maxHealth = intValue;
        else if (id == StatsType.HP.type) health = intValue;
        else if (id == StatsType.maximumMP.type) maxMana = intValue;
        else if (id == StatsType.MP.type) mana = intValue;
        else if (id == StatsType.nextLevelExperience.type) xpGoal = intValue;
        else if (id == StatsType.experience.type) xp = intValue;
        else if (id == StatsType.level.type) level = intValue;
        else if (id == StatsType.inventory0.type) slot[0] = intValue;
        else if (id == StatsType.inventory1.type) slot[1] = intValue;
        else if (id == StatsType.inventory2.type) slot[2] = intValue;
        else if (id == StatsType.inventory3.type) slot[3] = intValue;
        else if (id == StatsType.inventory4.type) slot[4] = intValue;
        else if (id == StatsType.inventory5.type) slot[5] = intValue;
        else if (id == StatsType.inventory6.type) slot[6] = intValue;
        else if (id == StatsType.inventory7.type) slot[7] = intValue;
        else if (id == StatsType.inventory8.type) slot[8] = intValue;
        else if (id == StatsType.inventory9.type) slot[9] = intValue;
        else if (id == StatsType.inventory10.type) slot[10] = intValue;
        else if (id == StatsType.inventory11.type) slot[11] = intValue;
        else if (id == StatsType.attack.type) attack = intValue;
        else if (id == StatsType.defense.type) defense = intValue;
        else if (id == StatsType.speed.type) speed = intValue;
        else if (id == StatsType.vitality.type) vitality = intValue;
        else if (id == StatsType.wisdom.type) wisdom = intValue;
        else if (id == StatsType.dexterity.type) dexterity = intValue;
        else if (id == StatsType.effects.type) effects = intValue;
        else if (id == StatsType.stars.type) stars = intValue;
        else if (id == StatsType.name.type) name = stringValue;
        else if (id == StatsType.credits.type) realmGold = intValue;
        else if (id == StatsType.merchandisePrice.type) price = intValue;
        else if (id == StatsType.accountId.type) accountId = stringValue;
        //else if (id == 37.type) CanEnterPortal = bool.Parse(stringValue.type);
        else if (id == StatsType.accountId.type) accountId = stringValue;
        else if (id == StatsType.accountFame.type) accountFame = intValue; //fame you got when you died
        else if (id == StatsType.healthBonus.type) healthBonus = intValue;
        else if (id == StatsType.manaBonus.type) manaBonus = intValue;
        else if (id == StatsType.attackBonus.type) attackBonus = intValue;
        else if (id == StatsType.defenseBonus.type) defenseBonus = intValue;
        else if (id == StatsType.speedBonus.type) speedBonus = intValue;
        else if (id == StatsType.vitalityBonus.type) vitalityBonus = intValue;
        else if (id == StatsType.wisdomBonus.type) wisdomBonus = intValue;
        else if (id == StatsType.dexterityBonus.type) dexterityBonus = intValue;
        
        else if (id == StatsType.rankRequired.type) nameChangeRankRequired = intValue;
        else if (id == StatsType.nameChosen.type) nameChosen = intValue > 0;
        else if (id == StatsType.characterFame.type) characterFame = intValue; //fame on this character
        else if (id == StatsType.characterFameGoal.type) characterFameGoal = intValue;
        else if (id == StatsType.glowing.type) glowingEffect = intValue > -1;
        else if (id == StatsType.guildName.type) guildName = stringValue;
        else if (id == StatsType.guildRank.type) guildRank = intValue;
        else if (id == StatsType.oxygenBar.type) breath = intValue;
        else if (id == StatsType.healthPotionCount.type) healthPotionCount = intValue;
        else if (id == StatsType.magicPotionCount.type) magicPotionCount = intValue;
        else if (id == StatsType.backpack0.type) backPack[0] = intValue;
        else if (id == StatsType.backpack1.type) backPack[1] = intValue;
        else if (id == StatsType.backpack2.type) backPack[2] = intValue;
        else if (id == StatsType.backpack3.type) backPack[3] = intValue;
        else if (id == StatsType.backpack4.type) backPack[4] = intValue;
        else if (id == StatsType.backpack5.type) backPack[5] = intValue;
        else if (id == StatsType.backpack6.type) backPack[6] = intValue;
        else if (id == StatsType.backpack7.type) backPack[7] = intValue;
        else if (id == StatsType.hasBackpack.type) hasBackpack = intValue > 0;
        else if (id == StatsType.skin.type) skin = intValue;
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
