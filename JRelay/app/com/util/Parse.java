package com.util;

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
}
