package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class BitmapData implements IData {
	
	public int width;
	public int height;
	public byte[] bytes = new byte[0];

	@Override
	public void parseFromInput(DataInput in) throws IOException {
		this.width = in.readInt();
		this.height = in.readInt();
		this.bytes = new byte[this.width * this.height * 4];
		in.readFully(this.bytes);
	}

	@Override
	public void writeToOutput(DataOutput out) throws IOException {
		out.writeInt(this.width);
		out.writeInt(this.height);
		out.write(this.bytes);
	}

}
