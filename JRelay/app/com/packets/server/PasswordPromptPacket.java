package com.packets.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.models.Packet;

public class PasswordPromptPacket extends Packet{
	public int passwordStatus;
	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.passwordStatus=in.readInt();
		
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.passwordStatus);
		
	}

}
