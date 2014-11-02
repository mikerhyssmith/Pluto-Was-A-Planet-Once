package com.BeefGames.PlutoWasAPlanetOnce.Tokens;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class  Token 
{
	protected Vector2 position; //anything that extends this class
	protected float width;
	protected float height;
	protected  Rectangle bounds;
	protected float health;
	protected int ID;
	protected boolean checkCollect;
	protected float rotation;
	protected Vector2 velocity;
	
	protected Ship ship;
	
	protected float ROTATION_SPEED = 100;
	
	public Token(Vector2 position,float width,float height,Ship ship,float rotation, Vector2 velocity)
	{
		this.checkCollect = false;
		this.position = position;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		
		this.bounds = new Rectangle(position.x,position.y,width,height);
		
		this.velocity = velocity;
		
		this.ship = ship;
	}
	
	/**
	 * @return Returns the health of the entity
	 */
	public float gethealth(){
		return health;
	}
	/**
	 * @param newhealth The new health of the entity
	 */
	public void sethealth(float newhealth){
		health = newhealth;
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
	public float getRotation(){
		return rotation;
	}
	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public int getID(){
		
		return ID;
	}
	
	public  void update()
	{
		position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
	}
	
	public boolean checkCollect(){
		return checkCollect;
	}
	
}

	