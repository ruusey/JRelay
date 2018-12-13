package com.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PacketBuffer extends ByteArrayInputStream{
	public PacketBuffer() {
		super(new byte[4]);
		
	}
	public void resize(int newSize) throws IOException {
		if (newSize > 1048576){
            throw new IOException("New buffer size is too large");
        }
		byte[] old = super.buf;
		super.buf=new byte[newSize];
		super.buf[0]=old[0];
		super.buf[1]=old[1];
		super.buf[2]=old[2];
		super.buf[3]=old[3];
		
	}
	public void advance(int n) {
		super.skip(n);
	}
	public void reset() {
		
		super.buf=(new byte[4]);
		super.reset();
	}
	public int remainingBytes() {
		return super.available();
	}
	public void dispose() {
		super.buf=null;
	}
	
	
	
	
}
