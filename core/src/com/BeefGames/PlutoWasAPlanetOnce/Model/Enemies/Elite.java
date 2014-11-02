package com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Planet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BulletHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Elite extends Enemy 
{
	private Planet planet;

	
	//Spiraling variables
	private float r;
	private Vector2 eliteToPlanet;
	private float spiralFactor;
	private double t;
	private double completeCircles;

	//Shooting variables
	private BulletHandler bulletHandler;
	private float shotRadius;
	private Circle shotCircle;
	private float shootTime, shootWait;
	
	private Vector2 turretPosition;
	private float turretRotation;
	private Vector2 turretSize;
	private Vector2 turretOrigin;
	
	//Spawning variables
	private float spawnTime;
	private float spawnWait;
	
	public Elite(Vector2 position,float width, float height,
			float speed, float health, int value, float damage, World world, 
			float b, Vector2 worldSize, float spawnWait, float shootRange,
			float shootRate, float texWidth, float texHeight)
	{
		super(speed, 0f, width, height, position, health, value, damage, texWidth, texHeight);
		
		this.planet = world.getPlanet();

		//Work out vector from elite to planet centre
		eliteToPlanet = new Vector2(planet.getPosition().x - this.position.x, planet.getPosition().y - this.position.y);
		r =(float) Math.sqrt( Math.pow(eliteToPlanet.x, 2) + Math.pow(eliteToPlanet.y, 2) ) ;
		
		//Set up spiralling variables
		this.spiralFactor = b;
		t = r/spiralFactor;
		double remainder = Math.IEEEremainder(t, Math.PI *2);
		if(remainder != t)
		{			
			completeCircles = t - remainder;
		}
		else 
		{
			completeCircles = 0;
		}
		
		//Set up spawning variables
		this.spawnWait = spawnWait;
		spawnTime = TimeUtils.nanoTime();
		
		//Set up shooting variables
		shotCircle = new Circle(position.x,position.y,shotRadius);
		bulletHandler = new BulletHandler(world);
		this.shotRadius = shootRange;
		this.shootWait = shootRate;
		shootTime = TimeUtils.nanoTime();
		
		turretRotation = this.rotation;
		turretPosition = new Vector2(this.position.x +this.width/2, this.position.y + this.height/2);
	}

	@Override
	public void advance(float delta, Ship ship)
	{	
		double prevAngle = (eliteToPlanet.angle() / 360f) *( Math.PI*2) - Math.PI ;
		
		//Other possible spirals
		//double x = spiralFactor * Math.exp(-t) * ( -Math.cos(t) - Math.sin(t));
		//double y = spiralFactor * Math.exp(-t) * ( -Math.sin(t) + Math.cos(t));
		//double x = spiralFactor * (1/t) * ( - (1/t) * Math.cos(t) - Math.sin(t));
		//double y = spiralFactor * (1/t) * ( - (1/t) * Math.sin(t) + Math.cos(t));
		
		double x = ( (spiralFactor * Math.cos(t)) - (spiralFactor * t * Math.sin(t)) );
		double y = ( (spiralFactor * Math.sin(t)) + (spiralFactor * t * Math.cos(t)) );
		
		//Put them in to a vector
		Vector2 calcVelocity = new Vector2((float)x,(float)y);
		//Normalise
		velocity = calcVelocity.nor().scl(Gdx.graphics.getDeltaTime()*(SPEED*100));

		//Set rotation of image
		rotation = velocity.angle()+90;
		//Update position
		position.sub(velocity);
			
		//Vector pointing from enemy to centre of the planet
		eliteToPlanet = new Vector2(planet.getPosition().x - this.position.x, planet.getPosition().y - this.position.y);
		
		double currentAngle = (eliteToPlanet.angle() / 360f) *( Math.PI*2) - Math.PI ;
		
		if((currentAngle - prevAngle) > Math.PI)
		{
			completeCircles -= (2*Math.PI);
		}
		t = completeCircles + currentAngle;

		//Update shooting range
		shotCircle.set(position.x,position.y,shotRadius);
		
		//turretRotation = this.rotation;
		turretPosition = new Vector2(this.position.x +this.width/2 - turretOrigin.x, this.position.y + this.height/2 - turretOrigin.y);
		
		super.advance(delta, ship);
		
	}
	
	/**
	 * Returns true if the elite has waited long enough to spawn an enemy
	 */
	@Override 
	public Boolean SpawnEnemy()
	{
		float newTime = TimeUtils.nanoTime();
		if((newTime -spawnTime)/1000000000 >= spawnWait)
		{
			spawnTime = newTime;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Fire a bullet from the elite
	 * @param x
	 * @param y
	 */
	public void Fire(float x,float y)
	{
		float newTime = TimeUtils.nanoTime();
		if((newTime -shootTime)/1000000000 >= shootWait)
		{
			shootTime = newTime;
			Vector2 firePosition = new Vector2(turretPosition.x +turretOrigin.x, turretPosition.y + turretOrigin.y);
			
			bulletHandler.Fire(x, y,firePosition, this);
		}
	}
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (113*scale.x), position.y +(260*scale.y),
				position.x + (139*scale.x), position.y +(234*scale.y),
				position.x + (139*scale.x), position.y +(207*scale.y),
				//position.x + (163*scale.x), position.y +(182*scale.y),
				position.x + (139*scale.x), position.y +(182*scale.y),
				position.x + (139*scale.x), position.y +(55*scale.y),
				//position.x + (171*scale.x), position.y +(76*scale.y),
				position.x + (139*scale.x), position.y +(9*scale.y),
				position.x + (34*scale.x), position.y +(9*scale.y),
				//position.x + (2*scale.x), position.y +(76*scale.y),
				position.x + (34*scale.x), position.y +(55*scale.y),
				position.x + (34*scale.x), position.y +(182*scale.y),
				//position.x + (10*scale.x), position.y +(182*scale.y),
				position.x + (34*scale.x), position.y +(207*scale.y),
				position.x + (34*scale.x), position.y +(234*scale.y),
				position.x + (60*scale.x), position.y +(260*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
	
	@Override
	public String getType() 
	{		
		return "Elite";	
	}
	public Circle getShotCircle(){
		
		return shotCircle;
	}
	public Vector2 getTurretPosition()
	{
		return turretPosition;
	}
	public float getTurretRotation()
	{
		return turretRotation;
	}
	public Vector2 getTurretSize()
	{
		return turretSize;
	}
	public Vector2 getTurretOrigin()
	{
		return turretOrigin;
	}
	public void setTurretOrigin(Vector2 origin)
	{
		turretOrigin = origin;
	}
	public void setTurretSize(Vector2 size)
	{
		turretSize = size;
	}
	public void setTurretRotation(float rotation)
	{
		turretRotation = rotation;
	}
	
}
