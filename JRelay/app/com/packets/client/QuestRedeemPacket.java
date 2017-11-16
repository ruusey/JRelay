package com.packets.client;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.data.shared.SlotObject;
import com.models.Packet;

public class QuestRedeemPacket extends Packet{
	public String questId;
	public SlotObject[] slots = new SlotObject[0];
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.questId=in.readUTF();
		this.slots = new SlotObject[in.readShort()];
		
		for(int i=0;i<this.slots.length;i++){
			slots[i].parseFromInput(in);
		}
		
	}
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.questId);
		out.writeInt(this.slots.length);
		for(SlotObject s:this.slots){
			s.writeToOutput(out);
		}
		
	}
	
}
