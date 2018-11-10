package com.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import com.data.shared.Location;
import com.data.shared.StatData;

public class ObjectStatusData {
	public static final int NEXUS_PORTAL = 0x712;
	public int objectId; // int
	public transient Location position; // _-Tq
	public ArrayList<StatData> statData;
	public int owner_id;

	public ObjectStatusData(int objectId, Location position, ArrayList<StatData> statData) {
		this.objectId = objectId;
		this.position = position;
		this.statData = statData;
	}

	public ObjectStatusData(DataInput badi) throws IOException {
		parseFromInput(badi);
	}

	public ObjectStatusData() {
		this.statData = new ArrayList<StatData>();
	}

	public void parseFromInput(DataInput badi) throws IOException {
		objectId = badi.readInt();
		position = new Location();
		position.parseFromInput(badi);

		short size = badi.readShort();
		statData = new ArrayList<StatData>();
		
		for (int i = 0; i < size; i++) {
			StatData sd = new StatData(badi);
			statData.add(sd);
		}
	}

	public void writeToOutput(DataOutput bado) throws IOException {
		bado.writeInt(objectId);
		position.writeToOutput(bado);
		bado.writeShort((short) statData.size());
		for (int i = 0; i < statData.size(); i++) {
			statData.get(i).writeToOutput(bado);
		}
	}

	public String toString() {
		return "objectStatusdata [" + objectId + ", " + position + ", " + statData + "]";
	}

	public StatData getStatData(int num) {
		for (StatData sd : statData) {
			if (sd.type == num)
				return sd;
		}
		return null;
	}

	public void update(StatData sd) {
		for (StatData cur : statData) {
			if (cur.type == sd.type) {
				cur.numValue = sd.numValue;
				cur.stringValue = sd.stringValue;
				return;
			}
		}
		statData.add(sd);
	}

	public int getHealthPercent() {
		if (getStatData(StatData.MAX_HEALTH) == null || getStatData(StatData.HEALTH) == null) {
			return 900;
		}
		return (getStatData(StatData.HEALTH).numValue * 100) / getStatData(StatData.MAX_HEALTH).numValue;
	}
}
