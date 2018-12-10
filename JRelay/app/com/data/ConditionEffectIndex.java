package com.data;

import java.util.HashMap;
import java.util.Map;

public enum ConditionEffectIndex {

	Dead(0),
	Quiet(1),
	Weak(2),
	Slowed(3),
	Sick(4),
	Dazed(5),
	Stunned(6),
	Blind(7),
	Hallucinating(8),
	Drunk(9),
	Confused(10),
	StunImmume(11),
	Invisible(12),
	Paralyzed(13),
	Speedy(14),
	Bleeding(15),
	NotUsed(16),
	Healing(17),
	Damaging(18),
	Berserk(19),
	Paused(20),
	Stasis(21),
	StasisImmune(22),
	Invincible(23),
	Invulnerable(24),
	Armored(25),
	ArmorBroken(26),
	Hexed(27),
	AnotherSpeedy(28),
	Unstable(29),
	Darkness(30),
	SlowedImmune(31),
	DazedImmune(32),
	ParalyzedImmune(33),
	Petrified(34),
	PetrifiedImmune(35),
	PetEffectIcon(36),
	Curse(37),
	CurseImmune(38),
	HPBoost(39),
	MPBoost(40),
	AttBoost(41),
	DefBoost(42),
	SpdBoost(43),
	VitBoost(44),
	WisBoost(45),
	DexBoost(46),
	Silenced(47),
	Exposed(59),
	GroundDamage(98);
	
	public static Map<Integer, ConditionEffectIndex> map = new HashMap<Integer, ConditionEffectIndex>();
	static {
        for (ConditionEffectIndex ce : ConditionEffectIndex.values()) {
            map.put(ce.index, ce);
        }
    }
	
	public int index;

	ConditionEffectIndex(int index) {
		this.index = index;
	}
	public static ConditionEffectIndex valueOf(int index) {
        return map.get(index);
    }
}
