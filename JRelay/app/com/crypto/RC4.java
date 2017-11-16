package com.crypto;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

public class RC4 {
	
	private final StreamCipher rc4;
	
	public RC4(String key) {
		this(hexStringToByteArray(key));
	}
	
	public RC4(byte[] bytes) {
		this.rc4 = new RC4Engine();
		KeyParameter keyParam = new KeyParameter(bytes);
		this.rc4.init(true, keyParam);
	}
	
	/**
	 * Cipher bytes and update cipher
	 * 
	 * @param bytes
	 */
	public void cipher(byte[] bytes) {
		this.rc4.processBytes(bytes, 0, bytes.length, bytes, 0);
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
}
