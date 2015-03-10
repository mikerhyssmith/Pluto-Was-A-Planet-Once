package com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Soldier extends Enemy 
{
	private Vector2 planetPosition;
	
	public Soldier(Vector2 position,float width, float height,  float rotation,float speed 
			,Vector2 planetPosition, float health, int value, float damage, float texWidth, float texHeight) {
		super(speed, rotation, width, height, position, health, damage, texWidth, texHeight);
		
		this.planetPosition = planetPosition;
	}

	
	@Override
	public void advance(float delta, Ship ship) 
	{
		
		if(calculateDistance(planetPosition.x,planetPosition.y, position.x - width/2, position.y - height/2) < 
				calculateDistance(ship.getPosition().x + ship.getWidth()/2,ship.getPosition().y + ship.getHeight()/2,position.x - width/2,position.y - height/2))
		{
			
			//double tempx = Double.parseDouble(Float.toString(planetPosition.x));
			//double tempy = Double.parseDouble(Float.toString(planetPosition.y));
			double tempx = (planetPosition.x- position.x -width/2);
			double tempy = (planetPosition.y -position.y -height/2);
			
			
			double angle = calculateAngle(tempx, tempy);
			//float tempAngle = (float)angle;
			
			Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
			
			velocity = calcVelocity.nor().scl((SPEED*200));
			
			rotation = velocity.angle()-90;
			
			//calcVelocity = velocity.cpy().mul(Gdx.graphics.getDeltaTime());
			
			position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
			
			super.advance(delta, ship);
		}
		else{
			
			//double tempx = Double.parseDouble(Float.toString(ship.getPosition().x + ship.getWidth()/2));
			//double tempy = Double.parseDouble(Float.toString(ship.getPosition().y + ship.getHeight()/2));
			
			double tempx = (planetPosition.x- position.x -width/2);
			double tempy = (planetPosition.y -position.y -height/2);
			
			double angle = calculateAngle(tempx, tempy);
			//float tempAngle = (float)angle;
			
			Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
			velocity = calcVelocity.nor().scl((SPEED*200));
			
			rotation = velocity.angle()-90;
			
			//calcVelocity = velocity.cpy().mul(Gdx.graphics.getDeltaTime());
			
			position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
		}		
		super.advance(delta, ship);
	}
	
	@Override
	public String getType() {
			
			
		return "Soldier";
		
	}
	
	public float calculateDistance(float x1,float y1,float x2,float y2){
		
		double tempx =  Math.pow(x1-x2, 2);
		double tempy =  Math.pow(y1-y2, 2);
		float sqrtAns = (float) Math.sqrt(tempx + tempy);
		return sqrtAns;
	}
	
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (33*scale.x), position.y +(68*scale.y),
				position.x + (42*scale.x), position.y +(34*scale.y),
				position.x + (62*scale.x), position.y +(17*scale.y),
				position.x + (48*scale.x), position.y +(11*scale.y),
				position.x + (17*scale.x), position.y +(11*scale.y),
				position.x + (3*scale.x), position.y +(17*scale.y),
				position.x + (23*scale.x), position.y +(34*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}

}
