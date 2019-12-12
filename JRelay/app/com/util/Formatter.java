package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
@SuppressWarnings("unchecked")
public class Formatter {
	public static void main(String[] args) {
		String s = "6a39570cc9de4ec71d64821894c79332b197f92ba85ed281a023";
		
//		String[] ss =s1.split("\n");
//		for(String string : ss) {
//		    System.out.println(string);
//		}
		System.out.println(s.substring(0,26));
		System.out.println(s.substring(26,s.length()));
		//return;
		
//		try {
//			String workingDir = JRelayGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//			System.out.println(workingDir);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		createPacketTypes();
		//test();
		
		
		
	}
	
	//USED TO BUILD PACKETTYPE ENUM ON GAME UPDATES
	public static void createPacketTypes() {
		try {
			SAXReader reader = new SAXReader();
			Document packets = reader.read("resources\\xml\\packets.xml");
			List<Node> list = packets.selectNodes("//Packet");
			
			for(Node node: list){
				byte id = (byte) Parse.parseInt(Parse.elemDefault(node, "PacketId", "0"));
				Parse.parseInt(Parse.elemDefault(node, "PacketId", "0"));
				String name = Parse.elemDefault(node, "PacketName", "");
				name = name.replaceAll("_", "");
				System.out.println(name+"("+id+"),");
				
					
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void test() {
		Scanner s = null;
		try {
			s = new Scanner(new File("resources\\xml\\packets.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(s.hasNextLine()) {
			String line = s.nextLine();
			if(line.length()<7)continue;
			line = line.substring(line.indexOf("const"), line.indexOf(";"));
			line = line.substring(6);
			//System.out.println(line);
			String pack = line.substring(0,line.indexOf(":"));
			String id = line.substring(line.indexOf("=")+2,line.length());
			System.out.println("<Packet>");
			System.out.println("\t<PacketName>"+pack+"</PacketName>");
			System.out.println("\t<PacketID>"+id+"</PacketID>");
			System.out.println("</Packet>");
			//System.out.println(pack+"("+id+"),");
		}
	}
	public static void enumerate() {
		Scanner s = null;
		try {
			s = new Scanner(new File("resources\\xml\\enum.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(s.hasNextLine()) {
			String line = s.nextLine();
			String[] splits = line.split(" ");
			
			System.out.println(splits[0]+"("+splits[2].replace(",", "")+"),");
			
		}
	}
	
}
