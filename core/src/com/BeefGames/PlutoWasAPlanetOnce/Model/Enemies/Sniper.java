package com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BulletHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Sniper extends Enemy{
	
	private Circle shotRadius;
	private BulletHandler bulletHandler;

	private Boolean moving = true;
	private float shootTime,shootWait;
	
	public Sniper(Vector2 position,float width, float height,  float rotation, 
			float speed, float health, int value, float damage,World world, float texWidth, float texHeight) {
		super(speed,rotation, width, height, position, health, damage, texWidth, texHeight);
		
		shotRadius = new Circle(position.x,position.y,200);
		bulletHandler = new BulletHandler(world);
		
		shootTime = TimeUtils.nanoTime();
	}


	@Override
	public void advance(float delta, Ship ship) 
	{
		//double tempx = Double.parseDouble(Float.toString(ship.getPosition().x + ship.getWidth()/2));
		//double tempy = Double.parseDouble(Float.toString(ship.getPosition().y + ship.getHeight()/2));
		
		double tempx = (ship.getPosition().x + ship.getWidth()/2 - position.x -width/2);
		double tempy = (ship.getPosition().y + ship.getHeight()/2 - position.y -height/2);
		
		double angle = calculateAngle(tempx,tempy);
		
		float tempAngle = (float)angle;
		
		Vector2 calcForce = new Vector2((float)Math.sin(tempAngle), (float)Math.cos(tempAngle));
		force = calcForce.nor().scl(((SPEED*2) + forceMultiplier)*20);
		
		if(moving)
		{
	        momentum.add(force);
		}
        
        momentum.scl(drag);
        
        velocity = momentum.cpy().scl(1/mass);
        
        //Vector2 calcVelocity = velocity.cpy().mul(Gdx.graphics.getDeltaTime() );
		
		position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
        
		rotation = force.angle()-90;
		
		shotRadius.set(position.x,position.y,300);
		shootWait = 0.5f;
		
		super.advance(delta, ship);
	}

	public Circle getShotRadius(){
		
		return shotRadius;
	}
	
	@Override
	public String getType()
	{
		return "Sniper";
	}
	
	public void Fire(float x,float y)
	{
		float newTime = TimeUtils.nanoTime();
		if((newTime -shootTime)/1000000000 >= shootWait)
		{
			shootTime = newTime;
			bulletHandler.Fire(x, y,this);
		}
		}
		
	
	public Boolean getMoving()
	{
		return moving;
	}
	public void setMoving(Boolean move)
	{
		moving = move;
	}
	
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (53*scale.x), position.y +(45*scale.y),
				position.x + (63*scale.x), position.y +(28*scale.y),
				position.x + (44*scale.x), position.y +(10*scale.y),
				position.x + (21*scale.x), position.y +(10*scale.y),
				position.x + (2*scale.x), position.y +(28*scale.y),
				position.x + (12*scale.x), position.y +(45*scale.y),
				position.x + (13*scale.x), position.y +(68*scale.y),
				position.x + (52*scale.x), position.y +(68*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
}
