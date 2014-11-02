package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class  Entity {
	
	protected Vector2 position; //anything that extends this class
	protected float width;
	protected float height;
	protected  Rectangle bounds;
	protected float health;
	protected float maxHealth;
	protected Circle circleBounds;
	
	protected Vector2 scale;
	protected Polygon polyBounds;
	
	public Entity(Vector2 position,float width,float height, float texWidth, float texHeight){
		this.position = position;
		this.width = width;
		this.height = height;
		
		this.scale = new Vector2(width/texWidth, height/texHeight);
		
		bounds = new Rectangle(position.x,position.y,width,height);
		
		polyBounds = new Polygon(new float[]{this.position.x, this.position.y,
				this.position.x + this.width, this.position.y,
				this.position.x + this.width, this.position.y + this.height,
				this.position.x, this.position.y + this.height });
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		
		
		if(width<=height)
		{
			circleBounds = new Circle(position.x + width/2, position.y + height/2, height/2);
		}
		else
		{
			circleBounds = new Circle(position.x + width/2, position.y + height/2, width/2);
		}
	}
	
	/**
	 * @return Returns the health of the entity
	 */
	public float getHealth(){
		return health;
	}
	/**
	 * @param newhealth The new health of the entity
	 */
	public void setHealth(float newHealth){
		if(newHealth <= maxHealth)
		{
			health = newHealth;
		
		}
		else if(newHealth >= maxHealth)
		{
			health = maxHealth;
		}
	}
	
	/**
	 * @return Returns the max health of the entity
	 */
	public float getMaxHealth(){
		return maxHealth;
	}
	/**
	 * @param newhealth The new health of the entity
	 */
	public void setMaxHealth(float newMaxHealth){
		maxHealth = newMaxHealth;
	}
	
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}
	public Circle getCircleBounds(){
		return circleBounds;
	}
	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public Polygon getPolyBounds()
	{
		return polyBounds;
	}
}
