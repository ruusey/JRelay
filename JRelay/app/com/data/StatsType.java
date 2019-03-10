package com.data;
public class StatsType {
	public static StatsType maxHp = new StatsType(0);
    public static StatsType HP = new StatsType(1);
    public static StatsType size = new StatsType(2);
    public static StatsType maximumMP = new StatsType(3);
    public static StatsType MP = new StatsType(4);
    public static StatsType nextLevelExperience = new StatsType(5);
    public static StatsType experience = new StatsType(6);
    public static StatsType level = new StatsType(7);
    public static StatsType inventory0 = new StatsType(8);
    public static StatsType inventory1 = new StatsType(9);
    public static StatsType inventory2 = new StatsType(10);
    public static StatsType inventory3 = new StatsType(11);
    public static StatsType inventory4 = new StatsType(12);
    public static StatsType inventory5 = new StatsType(13);
    public static StatsType inventory6 = new StatsType(14);
    public static StatsType inventory7 = new StatsType(15);
    public static StatsType inventory8 = new StatsType(16);
    public static StatsType inventory9 = new StatsType(17);
    public static StatsType inventory10 = new StatsType(18);
    public static StatsType inventory11 = new StatsType(19);
    public static StatsType attack = new StatsType(20);
    public static StatsType defense = new StatsType(21);
    public static StatsType speed = new StatsType(22);
    public static StatsType vitality = new StatsType(26);
    public static StatsType wisdom = new StatsType(27);
    public static StatsType dexterity = new StatsType(28);
    public static StatsType effects = new StatsType(29);
    public static StatsType stars = new StatsType(30);
    public static StatsType name = new StatsType(31); //Is UTF
    public static StatsType texture1 = new StatsType(32);
    public static StatsType texture2 = new StatsType(33);
    public static StatsType merchandiseType = new StatsType(34);
    public static StatsType credits = new StatsType(35);
    public static StatsType merchandisePrice = new StatsType(36);
    public static StatsType portalUsable = new StatsType(37); 
    public static StatsType accountId = new StatsType(38); //Is UTF
    public static StatsType accountFame = new StatsType(39);
    public static StatsType merchandiseCurrency = new StatsType(40);
    public static StatsType objectConnection = new StatsType(41);
  
    public static StatsType merchandiseRemainingCount = new StatsType(42);
    public static StatsType merchandiseRemainingMinutes = new StatsType(43);
    public static StatsType merchandiseDiscount = new StatsType(44);
    public static StatsType merchandiseRankRequirement = new StatsType(45);
    public static StatsType healthBonus = new StatsType(46);
    public static StatsType manaBonus = new StatsType(47);
    public static StatsType attackBonus = new StatsType(48);
    public static StatsType defenseBonus = new StatsType(49);
    public static StatsType speedBonus = new StatsType(50);
    public static StatsType vitalityBonus = new StatsType(51);
    public static StatsType wisdomBonus = new StatsType(52);
    public static StatsType dexterityBonus = new StatsType(53);
    public static StatsType ownerAccountId = new StatsType(54); //Is UTF
    public static StatsType rankRequired = new StatsType(55);
    public static StatsType nameChosen = new StatsType(56);
    public static StatsType characterFame = new StatsType(57);
    public static StatsType characterFameGoal = new StatsType(58);
    public static StatsType glowing = new StatsType(59);
    public static StatsType sinkLevel = new StatsType(60);
    public static StatsType altTextureIndex = new StatsType(61);
    public static StatsType guildName = new StatsType(62); //Is UTF
    public static StatsType guildRank = new StatsType(63);
    public static StatsType oxygenBar = new StatsType(64);
    public static StatsType xpBoosterActive = new StatsType(65);
    public static StatsType xpBoostTime = new StatsType(66);
    public static StatsType lootDropBoostTime = new StatsType(67);
    public static StatsType lootTierBoostTime = new StatsType(68);
    public static StatsType healthPotionCount = new StatsType(69);
    public static StatsType magicPotionCount = new StatsType(70);
    public static StatsType backpack0 = new StatsType(71);
    public static StatsType backpack1 = new StatsType(72);
    public static StatsType backpack2 = new StatsType(73);
    public static StatsType backpack3 = new StatsType(74);
    public static StatsType backpack4 = new StatsType(75);
    public static StatsType backpack5 = new StatsType(76);
    public static StatsType backpack6 = new StatsType(77);
    public static StatsType backpack7 = new StatsType(78);
    public static StatsType hasBackpack = new StatsType(79);
    public static StatsType skin = new StatsType(80);
    public static StatsType petInstanceId = new StatsType(81);
    public static StatsType petName = new StatsType(82); //Is UTF
    public static StatsType petType = new StatsType(83);
    public static StatsType petRarity = new StatsType(84);
    public static StatsType petMaximumLevel = new StatsType(85);
    public static StatsType petFamily = new StatsType(86); 
    public static StatsType petPoints0 = new StatsType(87);
    public static StatsType petPoints1 = new StatsType(88);
    public static StatsType petPoints2 = new StatsType(89);
    public static StatsType petLevel0 = new StatsType(90);
    public static StatsType petLevel1 = new StatsType(91);
    public static StatsType petLevel2 = new StatsType(92);
    public static StatsType petAbilityType0 = new StatsType(93);
    public static StatsType petAbilityType1 = new StatsType(94);
    public static StatsType petAbilityType2 = new StatsType(95);
    public static StatsType effects2 = new StatsType(96); // Other Effects
    public static StatsType fortuneTokens = new StatsType(97);

    private byte m_type;
    public int type;
    public StatsType(int type)
    {	
    	this.type=type;
        this.m_type = (byte)type;
    }

    public boolean IsUTF()
    {
        if (this == StatsType.name || this == StatsType.accountId || this == StatsType.ownerAccountId
           || this == StatsType.guildName || this == StatsType.petName)
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