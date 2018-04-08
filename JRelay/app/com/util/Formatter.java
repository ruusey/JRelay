package com.util;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.app.JRelayGUI;
@SuppressWarnings("unchecked")
public class Formatter {
	public static void main(String[] args) {
		String s = "6a39570cc9de4ec71d64821894c79332b197f92ba85ed281a023";
		System.out.println(s.substring(0,26));
		System.out.println(s.substring(26,s.length()));
//		try {
//			String workingDir = JRelayGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//			System.out.println(workingDir);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		createPacketTypes();
		
		
		
		
	}
	
	//USED TO BUILD PACKETTYPE ENUM ON GAME UPDATES
	public static void createPacketTypes() {
		try {
			SAXReader reader = new SAXReader();
			Document packets = reader.read("resources\\xml\\packets.xml");
			List<Node> list = packets.selectNodes("//Packet");
			
			for(Node node: list){
				byte id = (byte) Parse.parseInt(Parse.elemDefault(node, "PacketID", "0"));
				int baseId = Parse.parseInt(Parse.elemDefault(node, "PacketID", "0"));
				String name = Parse.elemDefault(node, "PacketName", "");
				name = name.replaceAll("_", "");
				System.out.println(name+"("+id+"),");
				
						
			}
		}catch(Exception e) {
			
		}
		
	}
	public static void test() {
		
	}
	
}
