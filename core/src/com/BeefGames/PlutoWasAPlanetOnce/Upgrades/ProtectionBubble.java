package com.BeefGames.PlutoWasAPlanetOnce.Upgrades;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class ProtectionBubble extends Upgrade {
	
	private int bubbleLevel;
	private int health;
	private Circle circleBounds;

	public ProtectionBubble(Vector2 position, float width, float height,int health) {
		super(position, width, height);
		level =0;
		this.health = health;
		
		if(width<=height)
		{
			circleBounds = new Circle(position.x + width/2, position.y + height/2, height/2);
		}
		else
		{
			circleBounds = new Circle(position.x + width/2, position.y + height/2, width/2);
		}
	}

	
	public void hit(){
		health = health -5;
		
	}
	
	public int getHealth(){
		return health;
	}
	
	public Circle getCircleBounds(){
		
		return circleBounds;
	}
	public boolean isAlive(){
		
		return isAlive;
	}
	public void purchase(){
		
		
	}
}
