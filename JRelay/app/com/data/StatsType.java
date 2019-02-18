package com.data;
public class StatsType {
	public static StatsType MaximumHP = new StatsType(0);
    public static StatsType HP = new StatsType(1);
    public static StatsType Size = new StatsType(2);
    public static StatsType MaximumMP = new StatsType(3);
    public static StatsType MP = new StatsType(4);
    public static StatsType NextLevelExperience = new StatsType(5);
    public static StatsType Experience = new StatsType(6);
    public static StatsType Level = new StatsType(7);
    public static StatsType Inventory0 = new StatsType(8);
    public static StatsType Inventory1 = new StatsType(9);
    public static StatsType Inventory2 = new StatsType(10);
    public static StatsType Inventory3 = new StatsType(11);
    public static StatsType Inventory4 = new StatsType(12);
    public static StatsType Inventory5 = new StatsType(13);
    public static StatsType Inventory6 = new StatsType(14);
    public static StatsType Inventory7 = new StatsType(15);
    public static StatsType Inventory8 = new StatsType(16);
    public static StatsType Inventory9 = new StatsType(17);
    public static StatsType Inventory10 = new StatsType(18);
    public static StatsType Inventory11 = new StatsType(19);
    public static StatsType Attack = new StatsType(20);
    public static StatsType Defense = new StatsType(21);
    public static StatsType Speed = new StatsType(22);
    public static StatsType Vitality = new StatsType(26);
    public static StatsType Wisdom = new StatsType(27);
    public static StatsType Dexterity = new StatsType(28);
    public static StatsType Effects = new StatsType(29);
    public static StatsType Stars = new StatsType(30);
    public static StatsType Name = new StatsType(31); //Is UTF
    public static StatsType Texture1 = new StatsType(32);
    public static StatsType Texture2 = new StatsType(33);
    public static StatsType MerchandiseType = new StatsType(34);
    public static StatsType Credits = new StatsType(35);
    public static StatsType MerchandisePrice = new StatsType(36);
    public static StatsType PortalUsable = new StatsType(37); 
    public static StatsType AccountId = new StatsType(38); //Is UTF
    public static StatsType AccountFame = new StatsType(39);
    public static StatsType MerchandiseCurrency = new StatsType(40);
    public static StatsType ObjectConnection = new StatsType(41);
  
    public static StatsType MerchandiseRemainingCount = new StatsType(42);
    public static StatsType MerchandiseRemainingMinutes = new StatsType(43);
    public static StatsType MerchandiseDiscount = new StatsType(44);
    public static StatsType MerchandiseRankRequirement = new StatsType(45);
    public static StatsType HealthBonus = new StatsType(46);
    public static StatsType ManaBonus = new StatsType(47);
    public static StatsType AttackBonus = new StatsType(48);
    public static StatsType DefenseBonus = new StatsType(49);
    public static StatsType SpeedBonus = new StatsType(50);
    public static StatsType VitalityBonus = new StatsType(51);
    public static StatsType WisdomBonus = new StatsType(52);
    public static StatsType DexterityBonus = new StatsType(53);
    public static StatsType OwnerAccountId = new StatsType(54); //Is UTF
    public static StatsType RankRequired = new StatsType(55);
    public static StatsType NameChosen = new StatsType(56);
    public static StatsType CharacterFame = new StatsType(57);
    public static StatsType CharacterFameGoal = new StatsType(58);
    public static StatsType Glowing = new StatsType(59);
    public static StatsType SinkLevel = new StatsType(60);
    public static StatsType AltTextureIndex = new StatsType(61);
    public static StatsType GuildName = new StatsType(62); //Is UTF
    public static StatsType GuildRank = new StatsType(63);
    public static StatsType OxygenBar = new StatsType(64);
    public static StatsType XpBoosterActive = new StatsType(65);
    public static StatsType XpBoostTime = new StatsType(66);
    public static StatsType LootDropBoostTime = new StatsType(67);
    public static StatsType LootTierBoostTime = new StatsType(68);
    public static StatsType HealthPotionCount = new StatsType(69);
    public static StatsType MagicPotionCount = new StatsType(70);
    public static StatsType Backpack0 = new StatsType(71);
    public static StatsType Backpack1 = new StatsType(72);
    public static StatsType Backpack2 = new StatsType(73);
    public static StatsType Backpack3 = new StatsType(74);
    public static StatsType Backpack4 = new StatsType(75);
    public static StatsType Backpack5 = new StatsType(76);
    public static StatsType Backpack6 = new StatsType(77);
    public static StatsType Backpack7 = new StatsType(78);
    public static StatsType HasBackpack = new StatsType(79);
    public static StatsType Skin = new StatsType(80);
    public static StatsType PetInstanceId = new StatsType(81);
    public static StatsType PetName = new StatsType(82); //Is UTF
    public static StatsType PetType = new StatsType(83);
    public static StatsType PetRarity = new StatsType(84);
    public static StatsType PetMaximumLevel = new StatsType(85);
    public static StatsType PetFamily = new StatsType(86); 
    public static StatsType PetPoints0 = new StatsType(87);
    public static StatsType PetPoints1 = new StatsType(88);
    public static StatsType PetPoints2 = new StatsType(89);
    public static StatsType PetLevel0 = new StatsType(90);
    public static StatsType PetLevel1 = new StatsType(91);
    public static StatsType PetLevel2 = new StatsType(92);
    public static StatsType PetAbilityType0 = new StatsType(93);
    public static StatsType PetAbilityType1 = new StatsType(94);
    public static StatsType PetAbilityType2 = new StatsType(95);
    public static StatsType Effects2 = new StatsType(96); // Other Effects
    public static StatsType FortuneTokens = new StatsType(97);

    private byte m_type;
    public int type;
    public StatsType(int type)
    {	
    	this.type=type;
        this.m_type = (byte)type;
    }

    public boolean IsUTF()
    {
        if (this == StatsType.Name || this == StatsType.AccountId || this == StatsType.OwnerAccountId
           || this == StatsType.GuildName || this == StatsType.PetName)
            return true;
        return false;
    }

    public StatsType newStatType(int type) throws Exception
    {
        if (type > Byte.MAX_VALUE) throw new Exception("Not a valid StatData number.");
        return new StatsType((byte)type);
    }

    public StatsType newStatType(byte type) throws Exception
    {
        if (type > Byte.MAX_VALUE) throw new Exception("Not a valid StatData number.");
        return new StatsType((int)type);
    }

    public static boolean checkStat(StatsType type, int id) throws Exception
    {
        if (id > Byte.MAX_VALUE) throw new Exception("Not a valid StatData number.");
        return type.m_type == (byte)id;
    }

    public static boolean checkTypeId(StatsType type, byte id)
    {
        return type.m_type == id;
    }

    public static boolean checkTypeNot(StatsType type, int id) throws Exception
    {
        if (id > Byte.MAX_VALUE) throw new Exception("Not a valid StatData number.");
        return type.m_type != (byte)id;
    }

    public static boolean checkTypeNot(StatsType type, byte id)
    {
        return type.m_type != id;
    }

    public static boolean checkTypeId(StatsType type, StatsType id)
    {
        return type.m_type == id.m_type;
    }

    public static boolean checkTypeNot(StatsType type, StatsType id)
    {
        return type.m_type != id.m_type;
    }

    public static byte getType(StatsType type)
    {
        return type.m_type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof StatsType)) return false;
        return this == (StatsType)obj;
    }
   
}