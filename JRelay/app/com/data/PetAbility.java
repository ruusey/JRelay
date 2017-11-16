package com.data;

import java.util.HashMap;
import java.util.Map;

public enum PetAbility {
	AttackClose(402), AttackMid(404), AttackFar(405), Electric(406), Heal(407), MagicHeal(408), Savage(409), Decoy(
			410), RisingFury(411);

	public static Map<Integer, PetAbility> map = new HashMap<Integer, PetAbility>();
	static {
		for (PetAbility pa : PetAbility.values()) {
			map.put(pa.type, pa);
		}
	}
	public int type;

	PetAbility(int type) {
		this.type = type;
	}

	public static PetAbility valueOf(int type) {
		return map.get(type);
	}

}
