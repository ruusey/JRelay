package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.QuestData;
import com.models.Packet;

public class QuestFetchResponsePacket extends Packet{
	 public QuestData[] Quests = new QuestData[0];
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		Quests = new QuestData[in.readShort()];
		for (int i = 0; i < Quests.length; i++)
            Quests[i].parseFromInput(in);
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeShort(this.Quests.length);
		for (int i = 0; i < Quests.length; i++)
            Quests[i].writeToOutput(out);
		
	}
	
	
}
