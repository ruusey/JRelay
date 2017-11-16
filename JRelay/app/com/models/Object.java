package com.models;

import com.objects.IntObject;

public class Object extends IntObject{
	public String objectClass;

    
    public int maxHP;

    
    public float xpMult;

    
    public boolean statc;

   
    public boolean occupySquare;

    
    public boolean enemyOccupySquare;

  
    public boolean fullOccupy;

   
    public boolean blocksSight;

    
    public boolean enemy;

   
    public boolean player;

   
    public boolean drawOnGround;

   
    public int size;

    
    public int shadowSize;

    
    public int defense;

    
    public boolean flying;

    
    public boolean god;

 
    public Projectile[] Projectiles;


	public Object(int id, String name, String objectClass, int maxHP,
			float xpMult, boolean statc, boolean occupySquare,
			boolean enemyOccupySquare, boolean fullOccupy, boolean blocksSight,
			boolean enemy, boolean player, boolean drawOnGround, int size,
			int shadowSize, int defense, boolean flying, boolean god,
			Projectile[] projectiles) {
		super(id, name);
		this.objectClass = objectClass;
		this.maxHP = maxHP;
		this.xpMult = xpMult;
		this.statc = statc;
		this.occupySquare = occupySquare;
		this.enemyOccupySquare = enemyOccupySquare;
		this.fullOccupy = fullOccupy;
		this.blocksSight = blocksSight;
		this.enemy = enemy;
		this.player = player;
		this.drawOnGround = drawOnGround;
		this.size = size;
		this.shadowSize = shadowSize;
		this.defense = defense;
		this.flying = flying;
		this.god = god;
		Projectiles = projectiles;
	}
    
    
}
