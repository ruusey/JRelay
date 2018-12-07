package com.constants;


public class ItemConstants {

	public static final int NO_ITEM = -1;
	public static final int ALL_TYPE = 0;
	public static final int SWORD_TYPE = 1;
	public static final int DAGGER_TYPE = 2;
	public static final int BOW_TYPE = 3;
	public static final int TOME_TYPE = 4;
	public static final int SHIELD_TYPE = 5;
	public static final int LEATHER_TYPE = 6;
	public static final int PLATE_TYPE = 7;
	public static final int WAND_TYPE = 8;
	public static final int RING_TYPE = 9;
	public static final int POTION_TYPE = 10;
	public static final int SPELL_TYPE = 11;
	public static final int SEAL_TYPE = 12;
	public static final int CLOAK_TYPE = 13;
	public static final int ROBE_TYPE = 14;
	public static final int QUIVER_TYPE = 15;
	public static final int HELM_TYPE = 16;
	public static final int STAFF_TYPE = 17;
	public static final int POISON_TYPE = 18;
	public static final int SKULL_TYPE = 19;
	public static final int TRAP_TYPE = 20;
	public static final int ORB_TYPE = 21;
	public static final int PRISM_TYPE = 22;
	public static final int SCEPTER_TYPE = 23;
	public static final int KATANA_TYPE = 24;
	public static final int SHURIKEN_TYPE = 25;
	public static final int EGG_TYPE = 26;

	public static String itemTypeToName(int param1) {
		switch (param1) {
			case ALL_TYPE:
				return "EquipmentType.Any";
			case SWORD_TYPE:
				return "EquipmentType.Sword";
			case DAGGER_TYPE:
				return "EquipmentType.Dagger";
			case BOW_TYPE:
				return "EquipmentType.Bow";
			case TOME_TYPE:
				return "EquipmentType.Tome";
			case SHIELD_TYPE:
				return "EquipmentType.Shield";
			case LEATHER_TYPE:
				return "EquipmentType.LeatherArmor";
			case PLATE_TYPE:
				return "EquipmentType.Armor";
			case WAND_TYPE:
				return "EquipmentType.Wand";
			case RING_TYPE:
				return "EquipmentType.Accessory";
			case POTION_TYPE:
				return "EquipmentType.Potion";
			case SPELL_TYPE:
				return "EquipmentType.Spell";
			case SEAL_TYPE:
				return "EquipmentType.HolySeal";
			case CLOAK_TYPE:
				return "EquipmentType.Cloak";
			case ROBE_TYPE:
				return "EquipmentType.Robe";
			case QUIVER_TYPE:
				return "EquipmentType.Quiver";
			case HELM_TYPE:
				return "EquipmentType.Helm";
			case STAFF_TYPE:
				return "EquipmentType.Staff";
			case POISON_TYPE:
				return "EquipmentType.Poison";
			case SKULL_TYPE:
				return "EquipmentType.Skull";
			case TRAP_TYPE:
				return "EquipmentType.Trap";
			case ORB_TYPE:
				return "EquipmentType.Orb";
			case PRISM_TYPE:
				return "EquipmentType.Prism";
			case SCEPTER_TYPE:
				return "EquipmentType.Scepter";
			case KATANA_TYPE:
				return "EquipmentType.Katana";
			case SHURIKEN_TYPE:
				return "EquipmentType.Shuriken";
			case EGG_TYPE:
				return "EquipmentType.Any";
			default:
				return "EquipmentType.InvalidType";
		}
	}



}
