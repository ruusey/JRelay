package com.data;

import java.util.HashMap;
import java.util.Map;

public enum CharacterClass
{
	Rogue(0x0300),
	Archer(0x0307),
	Wizard(0x030e),
	Priest(0x0310),
	Warrior(0x031d),
	Knight(0x031e),
	Paladin(0x031f),
	Assassin(0x0320),
	Necromancer(0x0321),
	Huntress(0x0322),
	Mystic(0x0323),
	Trickster(0x0324),
	Sorcerer(0x0325),
	Ninja(0x0326);
	private static Map<Integer, CharacterClass> map = new HashMap<Integer, CharacterClass>();
	static {
        for (CharacterClass cc : CharacterClass.values()) {
            map.put(cc.type, cc);
        }
    }
	public int type;
	CharacterClass(int type){
		this.type=type;
	}
	public static CharacterClass valueOf(int type) {
        return map.get(type);
    }
   
}
