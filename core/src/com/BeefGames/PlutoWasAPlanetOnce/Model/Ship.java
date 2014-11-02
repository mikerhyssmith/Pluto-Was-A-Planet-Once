package com.BeefGames.PlutoWasAPlanetOnce.Model;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Ship extends MoveableEntity{

	private Array<Bullet> shipBullets = new Array<Bullet>(); // <> arrow type
	private int playerMoney =0;
	private float playerArmour = 0;
	private float maxArmour = 0;
	private float weaponCooldown;
	private float weaponDamage;
	private Boolean move;
	
	private Boolean shootSide = false;
	
	Iterator<Bullet> BIter;
	Bullet b;
	
	public Ship(Vector2 position,float width, float height,float rotation,float SPEED, float health, 
			float maxHealth, float maxArmour, float cooldown, float damage, float texWidth, float texHeight, float forceMultiplier) 
	{
		super( position,SPEED,rotation,width,height,  texWidth,  texHeight);
		
		//Ship starts at full health initially
		this.health = health;
		this.maxHealth = maxHealth;
		this.maxArmour = maxArmour;
		this.weaponCooldown = cooldown;
		this.weaponDamage = damage;
		this.forceMultiplier = forceMultiplier;
		this.move = false;
	}
	
	public void update(Vector2 worldSize) 
	{
		updateMotion();
		
		//Limit the ships motion to within the worlds boundaries
		if(position.x <= (-worldSize.x/2) )  
		{
			position.x = -worldSize.x/2;
			velocity.x = 0;
			momentum.x = 0;
		}
		if(position.x + width >= (worldSize.x/2))
		{
			position.x = worldSize.x/2 - width;
			velocity.x = 0;
			momentum.x = 0;
		}
		if(position.y <= (-worldSize.y/2) )  
		{
			position.y = -worldSize.y/2;
			velocity.y = 0;
			momentum.y = 0;
		}
		if(position.y + height >= (worldSize.y/2))
		{
			position.y = worldSize.y/2 - height;
			velocity.y = 0;
			momentum.y = 0;
		}
		
		bounds.x = position.x;
		bounds.y = position.y; 
		
		if((force.x !=0) || (force.y != 0))
		{
			rotation = force.angle()-90;
		}
		
	    //Update bullets
		BIter = shipBullets.iterator();
		while (BIter.hasNext()) 
		{
			b = BIter.next();
			b.update();
		}
		
		super.update();
	}
	
	public void updateMotion()
	{
        if(move)
        {
        	momentum.add(force);
        }
        
        momentum.scl(drag);
        
        
        velocity = momentum.cpy().scl(1/mass);

        position.add(velocity.scl(Gdx.graphics.getDeltaTime() * 50 ));
        
	}
	@Override
	public void updatePolyBounds()
	{
		
		
		polyBounds = new Polygon(new float[]{
				position.x + (26*scale.x), position.y +(37*scale.y),
				position.x + (26*scale.x), position.y +(28*scale.y),
				position.x + (37*scale.x), position.y +(28*scale.y),
				position.x + (37*scale.x), position.y +(5*scale.y),
				position.x + (5*scale.x), position.y +(5*scale.y),
				position.x + (5*scale.x), position.y +(28*scale.y),
				position.x + (15*scale.x), position.y +(28*scale.y),
				position.x + (15*scale.x), position.y +(37*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
	
	public void addBullet(Bullet b) 
	{
		shipBullets.add(b);
	}
	
	public int getMoney(){
		return this.playerMoney;
	}
	public void setMoney(int money){
		this.playerMoney = money;
	}
	public float getArmour(){
		return this.playerArmour;
	}
	public void setArmour(float newArmour)
	{
		if(newArmour <= maxArmour)
		{
			playerArmour = newArmour;
		
		}
		else if(newArmour >= maxArmour)
		{
			playerArmour = maxArmour;
		}
	}
	public float getMaxArmour(){
		return this.maxArmour;
	}
	public void setMaxArmour(float maxArmour){
		this.maxArmour = maxArmour;
	}
	public Array<Bullet> getBullets(){
		return shipBullets;
	}
	public void setBullets(Array<Bullet> bullets){
		this.shipBullets = bullets;
	}
	public float getCooldown()
	{
		return weaponCooldown;
	}
	public void setCooldown(float cooldown)
	{
		this.weaponCooldown = cooldown;
	}
	public float getDamage()
	{
		return weaponDamage;
	}
	public void setDamage(float damage)
	{
		weaponDamage = damage;
	}
	public Boolean getShootSide()
	{
		return shootSide;
	}
	public void setShootSide(Boolean side)
	{
		shootSide = side;
	}
	public Boolean getMove()
	{
		return move;
	}
	public void setMove(Boolean move)
	{
		this.move = move;
	}
}
