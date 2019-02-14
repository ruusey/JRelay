package com.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.dom4j.Node;

public class Parse {
	public static boolean hasElement(Node node, String name) {
		return node.selectSingleNode(name) != null;
	}

	public static String attrDefault(Node node, String name, String def) {
		def="0";
		String value = node.valueOf("@"+name);
		if(value!=null && value.length()!=0){
			
			return value;
		}else{
			return def;
		}
		
	}

	public static String elemDefault(Node node, String name, String def) {
		Node n = node.selectSingleNode(name);
		if(n!=null){
			return n.getText();
		}else{
			return def;
		}
	}

	public static int parseHex(String input) {
		return Integer.decode(input);
	}

	public static int parseInt(String input) {
		return Integer.parseInt(input);
	}

	public static float parseFloat(String input) {
		return Float.parseFloat(input);
	}
	public static String decodeKey(byte[] key) {
		CharsetDecoder newDecoder = Charset.forName("UTF8").newDecoder();
		try {
			return newDecoder.decode(ByteBuffer.wrap(key)).toString();
		} catch (CharacterCodingException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] encodeKey(String key) {
		CharsetEncoder newDecoder = Charset.forName("UTF8").newEncoder();
		try {
			return newDecoder.encode(CharBuffer.wrap(key.toCharArray())).array();
		} catch (CharacterCodingException e) {
			
			e.printStackTrace();
			return null;
		}
	}
}
