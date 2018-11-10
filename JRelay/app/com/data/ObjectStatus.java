package com.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.Location;
import com.data.shared.StatData;
import com.packets.client.CreatePacket;

public class ObjectStatus {

	public int objectType; // short
	public ObjectStatusData objStatData;

	public ObjectStatus(int objectType, ObjectStatusData objStatData) {
		this.objectType = objectType;
		this.objStatData = objStatData;
	}

	public ObjectStatus() {
		this.objectType = -1;
		this.objStatData = new ObjectStatusData();
	}

	public ObjectStatus(DataInput badi) throws IOException {
		parseFromInput(badi);
	}

	public boolean isPlayer() {
		return CreatePacket.getName(objectType) != null;
	}

	public void parseFromInput(DataInput badi) throws IOException {
		objectType = badi.readShort();
		objStatData = new ObjectStatusData(badi);
	}

	public void writeToOutput(DataOutput bado) throws IOException {
		bado.writeShort(objectType);
		objStatData.writeToOutput(bado);
	}

	public String toString() {
		return "objectStatus [" + objectType + ", " + objStatData + "]";
	}

	public void update(ObjectStatusData osd) {
		for (StatData sd : osd.statData) {
			objStatData.update(sd);
		}
		objStatData.position = osd.position;
	}

	public Location getPosition() {
		return objStatData.position;
	}

	public int getObjectId() {
		return objStatData.objectId;
	}

	public int getTypeId() {
		return objectType;
	}

	public int getMaxHealth() {
		if (objStatData.getStatData(0) != null)
			return objStatData.getStatData(0).numValue;
		return -1;
	}

	public int getHealth() {
		if (objStatData.getStatData(1) != null)
			return objStatData.getStatData(1).numValue;
		return -1;
	}

	public int getSize() {
		if (objStatData.getStatData(2) != null)
			return objStatData.getStatData(2).numValue;
		return -1;
	}

	public int getMaxMana() {
		if (objStatData.getStatData(3) != null)
			return objStatData.getStatData(3).numValue;
		return -1;
	}

	public int getMana() {
		if (objStatData.getStatData(4) != null)
			return objStatData.getStatData(4).numValue;
		return -1;
	}

	public int getCurrentLevel() {
		if (objStatData.getStatData(7) != null)
			return objStatData.getStatData(7).numValue;
		return -1;
	}

	public int getSlot1() {
		if (objStatData.getStatData(8) != null)
			return objStatData.getStatData(8).numValue;
		return -1;
	}

	public int getSlot2() {
		if (objStatData.getStatData(9) != null)
			return objStatData.getStatData(9).numValue;
		return -1;
	}

	public int getSlot3() {
		if (objStatData.getStatData(10) != null)
			return objStatData.getStatData(10).numValue;
		return -1;
	}

	public int getSlot4() {
		if (objStatData.getStatData(11) != null)
			return objStatData.getStatData(11).numValue;
		return -1;
	}

	public int getSlot5() {
		if (objStatData.getStatData(12) != null)
			return objStatData.getStatData(12).numValue;
		return -1;
	}

	public int getSlot6() {
		if (objStatData.getStatData(13) != null)
			return objStatData.getStatData(13).numValue;
		return -1;
	}

	public int getSlot7() {
		if (objStatData.getStatData(14) != null)
			return objStatData.getStatData(14).numValue;
		return -1;
	}

	public int getSlot8() {
		if (objStatData.getStatData(15) != null)
			return objStatData.getStatData(15).numValue;
		return -1;
	}

	public int getSlot9() {
		if (objStatData.getStatData(16) != null)
			return objStatData.getStatData(16).numValue;
		return -1;
	}

	public int getSlot10() {
		if (objStatData.getStatData(17) != null)
			return objStatData.getStatData(17).numValue;
		return -1;
	}

	public int getSlot11() {
		if (objStatData.getStatData(18) != null)
			return objStatData.getStatData(18).numValue;
		return -1;
	}

	public int getSlot12() {
		if (objStatData.getStatData(19) != null)
			return objStatData.getStatData(19).numValue;
		return -1;
	}

	public int getTotalAttack() {
		if (objStatData.getStatData(20) != null)
			return objStatData.getStatData(20).numValue;
		return -1;
	}

	public int getTotalDefence() {
		if (objStatData.getStatData(21) != null)
			return objStatData.getStatData(21).numValue;
		return -1;
	}

	public int getTotalSpeed() {
		if (objStatData.getStatData(22) != null)
			return objStatData.getStatData(22).numValue;
		return -1;
	}

	public int getTotalDexterity() {
		if (objStatData.getStatData(26) != null)
			return objStatData.getStatData(26).numValue;
		return -1;
	}

	public int getTotalVitality() {
		if (objStatData.getStatData(27) != null)
			return objStatData.getStatData(27).numValue;
		return -1;
	}

	public int getTotalWisdom() {
		if (objStatData.getStatData(28) != null)
			return objStatData.getStatData(28).numValue;
		return -1;
	}

	public String getName() {
		if (objStatData.getStatData(31) != null)
			return objStatData.getStatData(31).stringValue;
		return null;
	}

	public int getRealmGold() {
		if (objStatData.getStatData(35) != null)
			return objStatData.getStatData(35).numValue;
		return -1;
	}

	public int getTotalFame() {
		if (objStatData.getStatData(39) != null)
			return objStatData.getStatData(39).numValue;
		return -1;
	}

	public int getBonusHealth() {
		if (objStatData.getStatData(46) != null)
			return objStatData.getStatData(46).numValue;
		return -1;
	}

	public int getBonusMana() {
		if (objStatData.getStatData(47) != null)
			return objStatData.getStatData(47).numValue;
		return -1;
	}

	public int getBonusAttack() {
		if (objStatData.getStatData(48) != null)
			return objStatData.getStatData(48).numValue;
		return -1;
	}

	public int getBonusDefence() {
		if (objStatData.getStatData(49) != null)
			return objStatData.getStatData(49).numValue;
		return -1;
	}

	public int getBonusWisdom() {
		if (objStatData.getStatData(52) != null)
			return objStatData.getStatData(52).numValue;
		return -1;
	}

	public int getBonusDexterity() {
		if (objStatData.getStatData(53) != null)
			return objStatData.getStatData(53).numValue;
		return -1;
	}

	public int getFame() {
		if (objStatData.getStatData(57) != null)
			return objStatData.getStatData(57).numValue;
		return -1;
	}

	public int getFameGoal() {
		if (objStatData.getStatData(58) != null)
			return objStatData.getStatData(58).numValue;
		return -1;
	}

	public String getGuild() {
		if (objStatData.getStatData(62) != null)
			return objStatData.getStatData(62).stringValue;
		return null;
	}

	public int getGuildRank() {
		if (objStatData.getStatData(63) != null)
			return objStatData.getStatData(63).numValue;
		return -1;
	}

	public int getSlot(int i) {
		if (i < 1 && i > 12) {
			System.err.println("Invalid getSlot " + i);
			return -99999;
		}
		return this.objStatData.getStatData(StatData.SLOT_1 - 1 + i).numValue;
	}

	public void setSlot(int i, int val) {
		if (i < 1 && i > 12) {
			System.err.println("Invalid getSlot " + i);
			return;
		}
		System.out.println(i + " | " + val + " || " + (StatData.SLOT_1 - 1 + i) + " | " + this.objStatData.getStatData(StatData.SLOT_1 - 1 + i));
		this.objStatData.getStatData(StatData.SLOT_1 - 1 + i).numValue = val;
	}

	public int getSlotThatContains(int type) {
		for (int i = 1; i <= 12; i++) {
			if (getSlot(i) == type) {
				return i;
			}
		}
		return -1;
	}
}
