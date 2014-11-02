package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends MoveableEntity {

	private int value = 0;
	private float damage = 0f;
	private ParticleEmitter emitter;
	private Boolean destroyed = false;
	/**
	 * 
	 * @param speed Max speed
	 * @param rotation Initial rotation
	 * @param width Width of image
	 * @param height Height of image
	 * @param position Initial position
	 * @param health Starting health
	 * @param value Money value given on kill
	 * @param damage Damage dealt on impact
	 */
	public Enemy(float speed, float rotation, float width, float height,
			Vector2 position, float health, int value, float damage, float texWidth, float texHeight) {
		super(position,speed,  rotation, width, height, texWidth, texHeight);

		//Start at full health
		this.health = health;
		this.maxHealth = health;
		this.damage = damage;
		this.value = value;
		this.velocity = new Vector2(0,0);
	}
	
	public Boolean SpawnEnemy()
	{
		return false;
	}
	
	public String getType(){
		return "Enemy";
	}
	/**
	 * Returns the cash value of the enemy
	 * @return
	 */
	public int getValue(){
		return this.value;
	}
	/**
	 * Sets the cash value of the enemy
	 * @param value The new value
	 */
	public void setValue(int value){
		this.value = value;
	}
	
	/**
	 * Returns the damage done by the enemy
	 * @return
	 */
	public float getDamage(){
		return this.damage;
	}
	/**
	 * Sets the damage done by the enemy
	 * @param value The new value
	 */
	public void setDamage(float newdamage){
		this.damage = newdamage;
	}
	
	public double calculateAngle(double x, double y)
	{
		double tempValue = Math.atan2(x,y);
		return tempValue; 
	}
	public void setEmitter(ParticleEmitter emitter){
		
		this.emitter = emitter;
	}
	public ParticleEmitter getEmitter()
	{
		return emitter;
	}
	public void advance(float delta,Ship ship)
	{
		super.update();
	}
	public void setDestroyed(Boolean killed)
	{
		this.destroyed = killed;
	}
	public Boolean getDestroyed()
	{
		return destroyed;
	}
	
}
