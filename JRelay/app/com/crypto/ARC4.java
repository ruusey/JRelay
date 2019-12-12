package com.crypto;

import java.util.ArrayList;

public class ARC4 {
    private byte[] S = new byte[256];
    private byte[] T = new byte[256];
    private int keylen;
    public ARC4(String hex) {
    	byte[] key = RC4.hexStringToByteArray(hex);
    	init(key);
    }
    public void init(byte[] key) {
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            keylen = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keylen];
            }
            int j = 0;
            byte tmp;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + T[i]) & 0xFF;
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
            }
        }
    }

    public byte[] encrypt(final byte[] plaintext) {
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }
    public static byte[] hexStringToByteArray(String key)
    {
    	ArrayList<Byte> bytes = new ArrayList<Byte>();
       int index = 0;
      
       while(index < key.length())
       {
    	   
    	   int e = Integer.parseInt(key.substring(index, index+2),16);
    	   bytes.add((byte)e);
          
          index = index + 2;
       }
       byte[] result = new byte[bytes.size()];
       for(int i = 0; i < bytes.size() ; i ++) {
    	   result[i]=bytes.get(i);
       }
       return result;
    }
}
