package com.BeefGames.PlutoWasAPlanetOnce.Wave;

import java.util.Random;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class SpawnManager {
	
	private int xPosition;
	private int yPosition;
	private float worldWidth;
	private float worldHeight;
	private float speed;
	private Random randx;
	private Random randy;
	private int x;
	private int y;
	private Vector2 position;
	private Circle spawnRadius;
	private float angle;
	
	public SpawnManager(float screenWidth,float screenHeight){
		
		this.worldWidth = screenWidth;
		this.worldHeight = screenHeight;
		
		x = (int)Math.round(screenWidth/2);
		y = (int)Math.round(screenHeight/2);
		
		
		//Generate enemies
		randx = new Random();
		randy = new Random();
	
		spawnRadius = new Circle(0,0,x+x/4);
		 
	}
	
	public int getXPosition(){
		randx.nextInt(x);
		xPosition = randx.nextInt(x/2);
		
		if(xPosition > x/4){
			
			xPosition = xPosition + 1080;
		}
		else{
			xPosition = xPosition -1080;
		}
		
		
		return xPosition;
	}
	public int getYPosition(){
		randy.nextInt(y/2);
		
		yPosition = randy.nextInt(y/2);
		if(yPosition > y/4){
			
			yPosition = yPosition +720;
			
		}
		else{
			yPosition = yPosition -720;
		}
	
		return yPosition;
	}
	
	public Vector2 getPosition(){
		angle = (float) (randx.nextFloat() * Math.PI * 2); 
		double x = (worldWidth/2 + spawnRadius.radius *Math.cos(angle))-worldWidth/2;
	
		double y = worldHeight/2 + spawnRadius.radius *Math.sin(angle)-worldHeight/2;
		
		position = new Vector2((float)x,(float)y);
		return position;
	}
	
	public Vector2 getElitePosition()
	{
		int side = randx.nextInt(4);
		float x,y;
		
		switch(side){
			case 0:	//left side
				x = -worldWidth/2;
				y = randx.nextInt((int)worldHeight) - worldHeight/2;
				break;
			case 1: //top side
				x = randx.nextInt((int)worldWidth) - worldWidth/2;
				y = worldHeight/2;
				break;
			case 2: //right side
				x = worldWidth/2;
				y = randx.nextInt((int)worldHeight) - worldHeight/2;
				break;
			case 3: //bottom side
				x = randx.nextInt((int)worldWidth) - worldWidth/2;
				y = -worldHeight/2;
				break;
			default: //This should never be called
				x = 0;
				y = 0;				
		}
		Vector2 position = new Vector2(x,y);
		
		return position;
	}

	public float getSpeed(){
		Random ran = new Random(2);
		
		speed = ran.nextFloat()+0.4f;
		return speed;
		
	}
	public float getWorldWidth()
	{
		return worldWidth;
	}
	public float getWorldHeight()
	{
		return worldHeight;
	}
	public int randInt(int bounds)
	{
		return randx.nextInt(bounds);
	}
	public float randFloat()
	{
		return randx.nextFloat();
	}
}
