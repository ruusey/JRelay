package com.data;

import java.util.HashMap;
import java.util.Map;

public enum BagType
{
	Normal(0x500),
	Purple(0x503),
	Pink(0x506),
	Cyan(0x509),
	Red(0x510),
	Blue(0x050B),
	Purple2(0x507),
	Egg(0x508),
	White(0x050C),
	White2(0x050E),
	White3(0x50F);
	
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
