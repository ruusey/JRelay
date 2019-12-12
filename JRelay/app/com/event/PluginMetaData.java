package com.event;

public class PluginMetaData {
	public String author;
	public String name;
	public String description;
	public String[] commands;
	public String[] packets;
	public PluginMetaData(String author, String name, String description,
			String[] commands,String[] packets) {
		super();
		this.author = author;
		this.name = name;
		this.description = description;
		this.commands = commands;
		this.packets=packets;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getCommands() {
		return commands;
	}
	public void setCommands(String[] commands) {
		this.commands = commands;
	}
	public String[] getPackets() {
		return packets;
	}
	public void setPackets(String[] packets) {
		this.packets = packets;
	}
	
	
}
