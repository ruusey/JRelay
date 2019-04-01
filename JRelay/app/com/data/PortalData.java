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

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
