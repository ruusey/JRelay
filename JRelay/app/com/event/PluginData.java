package com.event;

public interface PluginData {
	public void attach();
	public String getAuthor();
	public String getName();
	public String getDescription();
	public String[] getCommands();
	public String[] getPackets();
}
