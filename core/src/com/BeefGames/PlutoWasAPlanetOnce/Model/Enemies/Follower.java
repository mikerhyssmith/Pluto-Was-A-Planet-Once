package com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Follower extends Enemy{
	
	float ROTATION_SPEED=500;
	

	public Follower(Vector2 position,float width, float height,  float rotation, 
			float speed, float health, int value, float damage, float texWidth, float texHeight) {
		super(speed,rotation, width, height, position, health, value, damage, texWidth, texHeight);
		
	}

	@Override
	public void advance(float delta, Ship ship) 
	{
		double tempx = (ship.getPosition().x + ship.getWidth()/2 - position.x -width/2);
		double tempy = (ship.getPosition().y + ship.getHeight()/2 - position.y - height/2);
		//double tempx = Double.parseDouble(Float.toString(ship.getPosition().x + ship.getWidth()/2));
		//double tempy = Double.parseDouble(Float.toString(ship.getPosition().y + ship.getHeight()/2));
		
		double tempAngle = calculateAngle(tempx, tempy);
		//float tempAngle = (float)angle;
		
		Vector2 calcVelocity = new Vector2((float)Math.sin(tempAngle), (float)Math.cos(tempAngle));
		
		velocity = calcVelocity.nor().scl((SPEED*200));
		
		rotation = velocity.angle()-90;
		
		//calcVelocity = velocity.cpy().mul(Gdx.graphics.getDeltaTime());
		
		position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
		
		
		super.advance(delta, ship);
	}
	
	@Override
	public String getType() {
			
			
		return "Follower";
		
	}
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (19*scale.x), position.y +(49*scale.y),
				position.x + (35*scale.x), position.y +(18*scale.y),
				position.x + (35*scale.x), position.y +(2*scale.y),
				position.x + (19*scale.x), position.y +(6*scale.y),
				position.x + (2*scale.x), position.y +(2*scale.y),
				position.x + (2*scale.x), position.y +(18*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}

}
