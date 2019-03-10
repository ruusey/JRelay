package com.crypto;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

import com.relay.JRelay;

public class RSA {

	private final static String serverPublicKey = JRelay.RSA_PUBLIC_KEY;

	private static PublicKey key;

	static {
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(RSA.serverPublicKey));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSA.key = kf.generatePublic(spec);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*public static String createGuestGUID() {
		long timestamp = System.currentTimeMillis();
		double random = Math.random() * Double.MAX_VALUE;
		return RSA.sha1string(timestamp + "" + random + "" + 1).toUpperCase();
	}**/

	public static String encrypt(String string) {

		if (string == null) {
			System.err.println("ERROR : string is null!");
		}

		byte[] buf = string.getBytes(Charset.forName("UTF-8"));

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, RSA.key);

			buf = cipher.doFinal(buf);

			return DatatypeConverter.printBase64Binary(buf);
		} catch (Exception ex) {
			RSA.key = null;
			ex.printStackTrace();
		}
		return null;
	}

	private static String hexString(byte[] buf) {
		char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		char[] hexChars = new char[buf.length * 2];
		int v;
		for (int j = 0; j < buf.length; j++) {
			v = buf[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static byte[] sha1(byte[] buf) {
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			sha1.digest(buf);
			return buf;
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static String sha1string(String string) {
		return RSA.hexString(RSA.sha1(RSA.stringToBytes(string)));
	}

	private static byte[] stringToBytes(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
