package com.data;

import java.util.HashMap;
import java.util.Map;

public enum EffectType {
	Unknown(0), Heal(1), Teleport(2), Stream(3), Throw(4), Nova(5), Poison(6), Line(
			7), Burst(8), Flow(9), Ring(10), Lightning(11), Collapse(12), ConeBlast(
			13), Earthquake(14), Flash(15), BeachBall(16), ElectricBolts(17), ElectricFlashing(
			18), RisingFury(19);
	public static Map<Integer, EffectType> map = new HashMap<Integer, EffectType>();
	static {
        for (EffectType et : EffectType.values()) {
            map.put(et.type, et);
        }
    }
	
	public int type;
	EffectType(int type) {
		this.type = type;
	}
	public static EffectType valueOf(int type) {
        return map.get(type);
    }

}
