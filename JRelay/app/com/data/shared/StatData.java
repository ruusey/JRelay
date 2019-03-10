package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.constants.TextKey;


public class StatData implements IData {
	public static final int MAX_HP_STAT = 0;
	public static final int HP_STAT = 1;
	public static final int SIZE_STAT = 2;
	public static final int MAX_MP_STAT = 3;
	public static final int MP_STAT = 4;
	public static final int NEXT_LEVEL_EXP_STAT = 5;
	public static final int EXP_STAT = 6;
	public static final int LEVEL_STAT = 7;
	public static final int ATTACK_STAT = 20;
	public static final int DEFENSE_STAT = 21;
	public static final int SPEED_STAT = 22;
	public static final int INVENTORY_0_STAT = 8;
	public static final int INVENTORY_1_STAT = 9;
	public static final int INVENTORY_2_STAT = 10;
	public static final int INVENTORY_3_STAT = 11;
	public static final int INVENTORY_4_STAT = 12;
	public static final int INVENTORY_5_STAT = 13;
	public static final int INVENTORY_6_STAT = 14;
	public static final int INVENTORY_7_STAT = 15;
	public static final int INVENTORY_8_STAT = 16;
	public static final int INVENTORY_9_STAT = 17;
	public static final int INVENTORY_10_STAT = 18;
	public static final int INVENTORY_11_STAT = 19;
	public static final int VITALITY_STAT = 26;
	public static final int WISDOM_STAT = 27;
	public static final int DEXTERITY_STAT = 28;
	public static final int CONDITION_STAT = 29;
	public static final int NUM_STARS_STAT = 30;
	public static final int NAME_STAT = 31;
	public static final int TEX1_STAT = 32;
	public static final int TEX2_STAT = 33;
	public static final int MERCHANDISE_TYPE_STAT = 34;
	public static final int CREDITS_STAT = 35;
	public static final int MERCHANDISE_PRICE_STAT = 36;
	public static final int ACTIVE_STAT = 37;
	public static final int ACCOUNT_ID_STAT = 38;
	public static final int FAME_STAT = 39;
	public static final int MERCHANDISE_CURRENCY_STAT = 40;
	public static final int CONNECT_STAT = 41;
	public static final int MERCHANDISE_COUNT_STAT = 42;
	public static final int MERCHANDISE_MINS_LEFT_STAT = 43;
	public static final int MERCHANDISE_DISCOUNT_STAT = 44;
	public static final int MERCHANDISE_RANK_REQ_STAT = 45;
	public static final int MAX_HP_BOOST_STAT = 46;
	public static final int MAX_MP_BOOST_STAT = 47;
	public static final int ATTACK_BOOST_STAT = 48;
	public static final int DEFENSE_BOOST_STAT = 49;
	public static final int SPEED_BOOST_STAT = 50;
	public static final int VITALITY_BOOST_STAT = 51;
	public static final int WISDOM_BOOST_STAT = 52;
	public static final int DEXTERITY_BOOST_STAT = 53;
	public static final int OWNER_ACCOUNT_ID_STAT = 54;
	public static final int RANK_REQUIRED_STAT = 55;
	public static final int NAME_CHOSEN_STAT = 56;
	public static final int CURR_FAME_STAT = 57;
	public static final int NEXT_CLASS_QUEST_FAME_STAT = 58;
	public static final int LEGENDARY_RANK_STAT = 59;
	public static final int SINK_LEVEL_STAT = 60;
	public static final int ALT_TEXTURE_STAT = 61;
	public static final int GUILD_NAME_STAT = 62;
	public static final int GUILD_RANK_STAT = 63;
	public static final int BREATH_STAT = 64;
	public static final int XP_BOOSTED_STAT = 65;
	public static final int XP_TIMER_STAT = 66;
	public static final int LD_TIMER_STAT = 67;
	public static final int LT_TIMER_STAT = 68;
	public static final int HEALTH_POTION_STACK_STAT = 69;
	public static final int MAGIC_POTION_STACK_STAT = 70;
	public static final int BACKPACK_0_STAT = 71;
	public static final int BACKPACK_1_STAT = 72;
	public static final int BACKPACK_2_STAT = 73;
	public static final int BACKPACK_3_STAT = 74;
	public static final int BACKPACK_4_STAT = 75;
	public static final int BACKPACK_5_STAT = 76;
	public static final int BACKPACK_6_STAT = 77;
	public static final int BACKPACK_7_STAT = 78;
	public static final int HASBACKPACK_STAT = 79;
	public static final int TEXTURE_STAT = 80;
	public static final int PET_INSTANCEID_STAT = 81;
	public static final int PET_NAME_STAT = 82;
	public static final int PET_TYPE_STAT = 83;
	public static final int PET_RARITY_STAT = 84;
	public static final int PET_MAXABILITYPOWER_STAT = 85;
	public static final int PET_FAMILY_STAT = 86;
	public static final int PET_FIRSTABILITY_POINT_STAT = 87;
	public static final int PET_SECONDABILITY_POINT_STAT = 88;
	public static final int PET_THIRDABILITY_POINT_STAT = 89;
	public static final int PET_FIRSTABILITY_POWER_STAT = 90;
	public static final int PET_SECONDABILITY_POWER_STAT = 91;
	public static final int PET_THIRDABILITY_POWER_STAT = 92;
	public static final int PET_FIRSTABILITY_TYPE_STAT = 93;
	public static final int PET_SECONDABILITY_TYPE_STAT = 94;
	public static final int PET_THIRDABILITY_TYPE_STAT = 95;
	public static final int NEW_CON_STAT = 96;
	public static final int FORTUNE_TOKEN_STAT = 97;
	
	public int id;
	public int intValue;
	public String stringValue;
	
	public static String statToName(int stat) {
		switch (stat) {
			case MAX_HP_STAT:
				return TextKey.STAT_DATA_MAXHP;
			case HP_STAT:
				return TextKey.STATUS_BAR_HEALTH_POINTS;
			case SIZE_STAT:
				return TextKey.STAT_DATA_SIZE;
			case MAX_MP_STAT:
				return TextKey.STAT_DATA_MAXMP;
			case MP_STAT:
				return TextKey.STATUS_BAR_MANA_POINTS;
			case EXP_STAT:
				return TextKey.STAT_DATA_XP;
			case LEVEL_STAT:
				return TextKey.STAT_DATA_LEVEL;
			case ATTACK_STAT:
				return TextKey.STAT_MODEL_ATTACK_LONG;
			case DEFENSE_STAT:
				return TextKey.STAT_MODEL_DEFENSE_LONG;
			case SPEED_STAT:
				return TextKey.STAT_MODEL_SPEED_LONG;
			case VITALITY_STAT:
				return TextKey.STAT_MODEL_VITALITY_LONG;
			case WISDOM_STAT:
				return TextKey.STAT_MODEL_WISDOM_LONG;
			case DEXTERITY_STAT:
				return TextKey.STAT_MODEL_DEXTERITY_LONG;
			default:
				return TextKey.STAT_DATA_UNKNOWN_STAT;
		}
	}
	
	
	public boolean isUTFData() {
		switch (this.id) {
		case NAME_STAT:
		case GUILD_NAME_STAT:
		case PET_NAME_STAT:
		case ACCOUNT_ID_STAT:
		case OWNER_ACCOUNT_ID_STAT:
			return true;
		default:
			return false;
		}

	}
	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		id = in.readUnsignedByte();
		if (isUTFData()) {
			stringValue = in.readUTF();
		} else {
			intValue = in.readInt();
		}
	}
	
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeByte(id);
		if (isUTFData()) {
			out.writeUTF(stringValue);
		} else {
			out.writeInt(intValue);
		}
	}
	
}