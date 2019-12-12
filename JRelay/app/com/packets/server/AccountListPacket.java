package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;


public class AccountListPacket extends Packet {
	
	public int accountListId;
	public String[] accountIds = new String[0];
	public int lockAction = -1;
	
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.accountListId = in.readInt();
		this.accountIds = new String[in.readShort()];
		for (int i = 0; i < accountIds.length; i++) {
			accountIds[i] = in.readUTF();
		}
		lockAction = in.readInt();
	}
	
	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(accountListId);
		out.writeShort(accountIds.length);
		for (String accountId : accountIds) {
			out.writeUTF(accountId);
		}
		out.writeInt(lockAction);
	}
	
}
