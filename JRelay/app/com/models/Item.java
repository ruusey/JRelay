package com.models;

import com.objects.IntObject;

public class Item extends IntObject{
	
	public Projectile projectile;

	public int numProjectiles;

	public int tier;

	public byte slotType;

	public float rateOfFire;

	public int feedPower;

	public byte bagType;

	public byte mpCost;

	public byte fameBonus;

	public boolean soulbound;

	public boolean usable;

	public boolean consumable;

	public Item(int id, String name, Projectile projectile,
			int numProjectiles, int tier, byte slotType, float rateOfFire,
			int feedPower, byte bagType, byte mpCost, byte fameBonus,
			boolean soulbound, boolean usable, boolean consumable) {
		super(id, name);
		this.projectile = projectile;
		this.numProjectiles = numProjectiles;
		this.tier = tier;
		this.slotType = slotType;
		this.rateOfFire = rateOfFire;
		this.feedPower = feedPower;
		this.bagType = bagType;
		this.mpCost = mpCost;
		this.fameBonus = fameBonus;
		this.soulbound = soulbound;
		this.usable = usable;
		this.consumable = consumable;
	}


	
}
