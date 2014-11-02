package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public abstract class MoveableEntity extends Entity {
	
	//vector2 holds x and a y position
	protected Vector2 velocity;
	protected float SPEED;
	protected float rotation;
	
    //variables used for motion
    protected Vector2 momentum, force;
    protected float mass = 10.0f, drag = 0.95f, forceMultiplier = 3.0f;
	
	
	public MoveableEntity(Vector2 position, float speed , float rotation,float width,float height, float texWidth, float texHeight){
		
		super(position,width,height, texWidth, texHeight);
		this.SPEED = speed;
		this.rotation = rotation;
		velocity = new Vector2(0,0);
		momentum = new Vector2(0,0);
		force = new Vector2(0,0);
		polyBounds.setRotation(rotation);
	}
	
	//abstract void advance(float delta); //call when we want to move object
	
	public float getForceMult(){
		return forceMultiplier;
	}
	
	public Vector2 getForce()
	{
		return force;
	}
	
	public void setForce(Vector2 newForce)
	{
		this.force = newForce;
	}
	public Vector2 getMomentum()
	{
		return momentum;
	}
	
	public void setMomentum(Vector2 newMomentum)
	{
		this.momentum = newMomentum;
	}
	
	public Vector2 getVelocity()
	{	
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity)
	{	
		this.velocity = velocity;
	}
	
	public float getRotation()
	{
		return rotation;
	}
	
	public void setRotation(float rotation){
		
		this.rotation = rotation;
	}
	
	public void update()
	{
		bounds.x = position.x;
		bounds.y = position.y;
		
		updatePolyBounds();
	}
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{position.x, position.y,
				position.x + width, position.y,
				position.x + width, position.y + height,
				position.x, position.y + height });
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
}
