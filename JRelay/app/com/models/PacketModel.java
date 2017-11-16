package com.models;

public class PacketModel {
	public byte id;
	public String name;
	
	
	public PacketModel(byte id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
