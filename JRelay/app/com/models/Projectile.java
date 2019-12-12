package com.models;

import java.util.HashMap;
import com.objects.ByteObject;

public class Projectile extends ByteObject {
	public int damage;

    public float speed;

    public int size;

    public int lifetime;

    public int maxDamage;
    public int minDamage;

    public float magnitude;
    public float amplitude;
    public float frequency;

    public boolean wavy;
    public boolean parametric;
    public boolean boomerang;
    public boolean armorPiercing;
    public boolean multiHit;
    public boolean passesCover;

    public HashMap<String, Float> StatusEffects;

	public Projectile(byte id, String name, int damage, float speed, int size,
			int lifetime, int maxDamage, int minDamage, float magnitude,
			float amplitude, float frequency, boolean wavy, boolean parametric,
			boolean boomerang, boolean armorPiercing, boolean multiHit,
			boolean passesCover, HashMap<String, Float> statusEffects) {
		super(id, name);
		this.damage = damage;
		this.speed = speed;
		this.size = size;
		this.lifetime = lifetime;
		this.maxDamage = maxDamage;
		this.minDamage = minDamage;
		this.magnitude = magnitude;
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.wavy = wavy;
		this.parametric = parametric;
		this.boomerang = boomerang;
		this.armorPiercing = armorPiercing;
		this.multiHit = multiHit;
		this.passesCover = passesCover;
		StatusEffects = statusEffects;
	}
    
}
