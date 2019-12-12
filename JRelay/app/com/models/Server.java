package com.models;

import com.objects.StringObject;

public class Server extends StringObject{
	
	public String abbreviation;
	
	public String address;

	public Server(String id, String name, String abbreviation, String address) {
		super(id, name);
		this.abbreviation = abbreviation;
		this.address = address;
	}
	
	
	
}
