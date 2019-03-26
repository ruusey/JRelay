package com.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.dom4j.Node;

public class Parse {
	public static String attrDefault(final Node node, final String name, String def) {
		def = "0";
		final String value = node.valueOf("@" + name);
		if ((value != null) && (value.length() != 0)) {
			return value;
		} else {
			return def;
		}
	}

	public static String decodeKey(final byte[] key) {
		final CharsetDecoder newDecoder = Charset.forName("UTF8").newDecoder();
		try {
			return newDecoder.decode(ByteBuffer.wrap(key)).toString();
		} catch (final CharacterCodingException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static String elemDefault(final Node node, final String name, final String def) {
		final Node n = node.selectSingleNode(name);
		if (n != null) {
			return n.getText();
		} else {
			return def;
		}
	}

	public static byte[] encodeKey(final String key) {
		final CharsetEncoder newDecoder = Charset.forName("UTF8").newEncoder();
		try {
			return newDecoder.encode(CharBuffer.wrap(key.toCharArray())).array();
		} catch (final CharacterCodingException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static boolean hasElement(final Node node, final String name) {
		return node.selectSingleNode(name) != null;
	}

	public static float parseFloat(final String input) {
		return Float.parseFloat(input);
	}

	public static int parseHex(final String input) {
		return Integer.decode(input);
	}

	public static int parseInt(final String input) {
		return Integer.parseInt(input);
	}
}
