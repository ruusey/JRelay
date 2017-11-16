package com.models;

import java.util.ArrayList;

public class PacketMeta {
	public String name;
	public ArrayList<String> entities;
	public PacketMeta(String name, ArrayList<String> entities) {
		super();
		this.name = name;
		this.entities = entities;
	}
	public void addEntity(String entity) {
		entities.add(entity);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<String> entities) {
		this.entities = entities;
	}
	
}
