package com.data;

import java.util.HashMap;
import java.util.Map;

public enum BagType
{
	Brown(0x500),
	BrownBoosted(0x6ad),
	Pink(0x506),
	PinkBoosted(0x6ae),
	Purple(0x507),
	PurpleBoosted(0x6ba),
	Egg(0x508),
	EggBoosted(0x6bb),
	Gold(0x050E),
	GoldBoosted(0x6bc),
	Cyan(0x509),
	CyanBoosted(0x6bd),
	Blue(0x050B),
	BlueBoosted(0x6be),
	Orange(0x50F),
	OrangeBoosted(0x6bf),
	Red(0x6AC),
	RedBoosted(0x6c0),
	White(0x050C),
	WhiteBoosted(0x0510);
	
	public static Map<Integer, BagType> map = new HashMap<Integer, BagType>();
	static {
        for (BagType lb : BagType.values()) {
            map.put(lb.type, lb);
        }
    }
	
	public int type;
	BagType(int type){
		this.type=type;
	}
	public static BagType valueOf(int type) {
        return map.get(type);
    }
}
