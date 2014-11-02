package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Planet extends Entity{

	private int initialPixels;
	private int currentPixels;
	private Circle coreCircle;
	
	public Planet(Vector2 position, float width, float height, float health, float texWidth, float texHeight) {
		super(position, width, height, texWidth, texHeight);
		// TODO Auto-generated constructor stub
		this.health = health;
		
		bounds = new Rectangle(position.x- width/2, position.y - height/2,width,height);
		
		if(width<=height)
		{
			circleBounds = new Circle(position.x, position.y, height/2);
			coreCircle = new Circle(position.x, position.y, height/4);
		}
		else
		{
			circleBounds = new Circle(position.x, position.y, width/2);
			coreCircle = new Circle(position.x, position.y, width/4);
		}
	}
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public int getCurrentPixels()
	{
		return currentPixels;
	}
	public void setCurrentPixels(int pixelCount)
	{
		currentPixels = pixelCount;
	}
	public int getInitialPixels()
	{
		return initialPixels;
	}
	public void setInitialPixels(int pixelCount)
	{
		initialPixels = pixelCount;
	}
	/**
	 * If an enemy hits within the core circle, the game is lost
	 * @return
	 */
	public Circle getCoreCircle()
	{
		return coreCircle;
	}
}
