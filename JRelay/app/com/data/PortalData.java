package com.data;

import com.data.shared.Location;

public final class PortalData {
	
	public int population;
	public int id;
	public Location loc;
	public String name;
	
	public PortalData(int population, int id, Location loc, String name) {
		this.population = population;
		this.id = id;
		this.loc = loc;
		this.name = name;
	}
	
	public PortalData() {
	}
	
}
