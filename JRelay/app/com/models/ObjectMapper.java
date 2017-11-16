package com.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.app.JRelayGUI;
import com.data.GameData;
import com.relay.JRelay;
import com.util.Parse;

@SuppressWarnings("unchecked")
public class ObjectMapper {
	public static HashMap<ArrayList<String>, ArrayList<String>> tiles = new HashMap<ArrayList<String>, ArrayList<String>>();
	public static HashMap<ArrayList<String>, ArrayList<String>> objects = new HashMap<ArrayList<String>, ArrayList<String>>();

	public static final String ALL_SELECTOR = "*";
	public static final String OR_SELECTOR = "#";
	public static final String AND_SELECTOR = ":";

	public static void buildMap() {
		SAXReader reader = new SAXReader();
		try {

			Document maps = reader.read(JRelay.RES_LOC + "xml\\maps.xml");
			parseMaps(maps);
		} catch (Exception e) {
			JRelayGUI.error(e.getMessage());
		}
		// System.out.println("Mapped User Tiles: "+JRelay.gen.serialize(tiles));
	}

	public static void parseMaps(Document mapDoc) throws Exception {
		List<Node> list = mapDoc.selectNodes("//Map");
		for (Node node : list) {
			String mapId = Parse.attrDefault(node, "id", "");
			boolean tileMap = Parse.hasElement(node, "TileMap");
			JRelayGUI.log("Loading object map " + mapId);
			if (tileMap) {

				List<Node> entries = node.selectNodes("Entry");
				for (Node entry : entries) {
					String start = Parse.attrDefault(entry, "start", "");
					String end = Parse.attrDefault(entry, "end", "");
					ArrayList<String> startArr = null;
					ArrayList<String> endArr = null;
					try {
						startArr = parseStart(start);
						endArr = parseEnd(end);
						String thisObj = null;
						try {

							for (String s : startArr) {
								thisObj = s;
								GameData.nameToTile.get(thisObj);
							}
						} catch (Exception e) {
							throw new Exception("Error in map " + mapId + "... no such tile '" + thisObj + "'");
						}
					} catch (Exception e) {
						throw e;
					}
					tiles.put(startArr, endArr);
				}
			} else {
				List<Node> entries = node.selectNodes("Entry");
				for (Node entry : entries) {
					String start = Parse.attrDefault(entry, "start", "");
					String end = Parse.attrDefault(entry, "end", "");
					ArrayList<String> startArr = null;
					ArrayList<String> endArr = null;
					try {
						startArr = parseStart(start);
						endArr = parseStart(end);
						String thisObj = null;
						try {

							for (String s : startArr) {
								thisObj = s;
								GameData.nameToObject.get(thisObj);
							}
						} catch (Exception e) {
							throw new Exception("Error in map " + mapId + "... no such object '" + thisObj + "'");
						}
					} catch (Exception e) {
						throw e;
					}
					objects.put(startArr, endArr);

				}
			}
		}
	}

	public static ArrayList<String> parseStart(String startString) throws Exception {
		try {
			ArrayList<String> res = new ArrayList<String>();
			if (startString.equals(ALL_SELECTOR)) {
				res.add("*");
			} else {
				String[] elements = startString.split(AND_SELECTOR);
				for (String element : elements) {

					res.add(element.trim());
				}
			}

			return res;
		} catch (Exception e) {
			throw new Exception("Syntax error in object map '" + startString + "'");
		}

	}

	public static ArrayList<String> parseEnd(String endString) throws Exception {
		try {
			ArrayList<String> res = new ArrayList<String>();

			String[] elements = endString.split(OR_SELECTOR);
			for (String element : elements) {
				res.add(element.trim());
			}

			return res;
		} catch (Exception e) {
			throw new Exception("Syntax error in object map '" + endString + "'");
		}

	}
}
