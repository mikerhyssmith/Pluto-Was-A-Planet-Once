package com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PlanetScout extends Enemy 
{
	float ROTATION_SPEED=500;
	private Vector2 calcVelocity;
	private double tempx,tempy;
	private double angle;
	
	public PlanetScout(Vector2 position,float width, float height,  float rotation,float speed 
			,Vector2 target, float health, int value, float damage, float texWidth, float texHeight) 
	{
		super(speed, rotation, width, height, position, health, value, damage, texWidth, texHeight);
		
		tempx = (target.x - position.x -width/2);
		tempy = (target.y - position.y -height/2);
		
		angle = calculateAngle(tempx,tempy);
		//floatAngle = (float)angle;
		calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
		velocity = calcVelocity.nor().scl((SPEED*200));
		this.rotation = calcVelocity.angle() -90;

	}

	
	@Override
	public void advance(float delta, Ship ship) {

		//calcVelocity = new Vector2((float)Math.sin(floatAngle), (float)Math.cos(floatAngle));
		//velocity = calcVelocity.nor().mul((SPEED*200));
		
		//rotation = velocity.angle()-90;
		
		//calcVelocity = velocity.tmp().mul(Gdx.graphics.getDeltaTime());
		
		position.add(velocity.scl(Gdx.graphics.getDeltaTime()));
		//position.add(new Vector2(1,1));
		super.advance(delta, ship);
	}
	@Override
	public String getType() 
	{
		return "PlanetScout";
	}
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (33*scale.x), position.y +(81*scale.y),
				position.x + (50*scale.x), position.y +(74*scale.y),
				position.x + (54*scale.x), position.y +(29*scale.y),
				position.x + (62*scale.x), position.y +(3*scale.y),
				position.x + (32*scale.x), position.y +(13*scale.y),
				position.x + (3*scale.x), position.y +(3*scale.y),
				position.x + (11*scale.x), position.y +(29*scale.y),
				position.x + (15*scale.x), position.y +(74*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
}
