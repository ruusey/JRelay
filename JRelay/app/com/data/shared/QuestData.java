package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class QuestData implements IData{
	public String id;
    public String name;
    public String description;
    public int[] requirements = new int[0];
    public int[] rewards = new int[0];
    public boolean completed;
    public boolean itemOfChoice;
    public boolean repeatable;
    public int category;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		id = in.readUTF();
        name = in.readUTF();
        description = in.readUTF();
        category = in.readInt();
        requirements = new int[in.readShort()];
        for (int i = 0; i < requirements.length; i++)
        	requirements[i] = in.readInt();
        rewards = new int[in.readShort()];
        for (int i = 0; i < rewards.length; i++)
        	rewards[i] = in.readInt();
        completed = in.readBoolean();
        itemOfChoice = in.readBoolean();
        repeatable = in.readBoolean();

		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeUTF(this.id);
		out.writeUTF(this.name);
		out.writeUTF(this.description);
		out.writeInt(this.category);
		out.writeShort(this.requirements.length);
		for (int i = 0; i < requirements.length; i++)
        	out.write(requirements[i]);
		out.writeShort(this.rewards.length);
		for (int i = 0; i < rewards.length; i++)
			out.write(rewards[i]);
		out.writeBoolean(this.completed);
		out.writeBoolean(this.itemOfChoice);
		out.writeBoolean(this.repeatable);
	}

}
