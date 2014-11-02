package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Elite;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Sniper;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.Turret;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class BulletHandler {

	private WorldRenderer worldRenderer;
	private Ship ship;
	private Vector2 touch;
	private Vector3 touch3;
	private AudioHandler audioHandler;
	private World world;
	
	
	public BulletHandler(World world)
	{
		this.worldRenderer = world.getRenderer();
		this.ship = world.getShip();
		this.world = world;
		touch = new Vector2();
		touch3 = new Vector3();
		audioHandler = world.getAudioHandler();
	}
	
	/**
	 * Use to fire a bullet using a touchpad - NOT USED
	 * @param x
	 * @param y
	 */
	public void Fire(float x , float y)
	{
		touch3.set(x,y,0);
		//worldRenderer.getCamera().unproject(touch3);
		touch.set(touch3.x,touch3.y);
		
		float bulletSpeed = 600f;
		
		//double tempx = Double.parseDouble(Float.toString(touch.x));
		//double tempy = Double.parseDouble(Float.toString(touch.y));
		double tempx = touch.x;
		double tempy = touch.y;
		
		double angle = calculateAngle(tempx ,tempy);
		//float tempAngle = (float)angle;
		
		Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle)).scl(Gdx.graphics.getDeltaTime()*bulletSpeed);
		
		Texture bullet = worldRenderer.getTexture("playerbullet");
		
		Bullet b = new Bullet(new Vector2(ship.getPosition().x + ship.getWidth()/2,
				ship.getPosition().y + ship.getHeight()/2), bulletSpeed, 
				(float)angle, bullet.getWidth(),bullet.getHeight(),
				calcVelocity, ship.getDamage(), bullet.getWidth(),bullet.getHeight());

		//Add bullet to draw list
		worldRenderer.addDrawn("bullet"+b.hashCode(), "playerbullet", b.getPosition(), new Vector2(b.getWidth()/2,b.getHeight()/2),
				new Vector2(b.getWidth(),b.getHeight()), new Vector2(1,1),b.getRotation(), new Vector2(0,0), 
				new Vector2(worldRenderer.getTexture("playerbullet").getWidth(),worldRenderer.getTexture("playerbullet").getHeight()),
				false, false, true);
		
		ship.addBullet(b);
		
		audioHandler.shipFire();
	}
	
	/**
	 * Used to fire a bullet at a given angle
	 * @param angle
	 */
	public void Fire(float angle)
	{
		float bulletSpeed = 800f;
		//Convert angle to radians
		angle = - (( (angle) /360f)* 2 * (float)Math.PI);
		//Calculate bullet velocity
		Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle)).scl(Gdx.graphics.getDeltaTime()*bulletSpeed);
		//Get the texture to have its width, height on-hand
		Texture bullet = worldRenderer.getTexture("playerbullet");
		
		//These move the bullet towards the front of the ship
		float x = ship.getWidth()/4 * (float)Math.sin(angle);
		float y = ship.getWidth()/4 * (float)Math.cos(angle);

		float displacement = 3 * ship.getWidth()/8;
		//Decide which side to shoot from
		if(ship.getShootSide())
		{
			displacement *= -1;
			ship.setShootSide(false);
		}
		else
		{
			ship.setShootSide(true);
		}
		//These displace the bullet to one side of the ship
		float x2 = (float)Math.cos( - angle) * displacement;
		float y2 = (float)Math.sin( - angle) * displacement;
		
		Vector2 position = new Vector2(ship.getPosition().x + ship.getWidth()/2 + x -bullet.getWidth()/2 + x2,
				ship.getPosition().y + ship.getHeight()/2 + y - bullet.getHeight()/2 + y2);

		Bullet b = new Bullet(position, bulletSpeed, angle, bullet.getWidth(),bullet.getHeight(),
				calcVelocity, ship.getDamage(), bullet.getWidth(),bullet.getHeight());

		//Add bullet to draw list
		worldRenderer.addDrawn("bullet"+b.hashCode(), "playerbullet", b.getPosition(), new Vector2(b.getWidth()/2,b.getHeight()/2),
				new Vector2(b.getWidth(),b.getHeight()), new Vector2(1,1),b.getRotation(), new Vector2(0,0), 
				new Vector2(bullet.getWidth(),bullet.getHeight()),
				false, false, true);
		
		ship.addBullet(b);
		
		audioHandler.shipFire();
	}
	
	
	public void Fire(float x , float y,Turret turret){

		float bulletSpeed = 1000f;
		//double tempx = Double.parseDouble(Float.toString(x));
		//double tempy = Double.parseDouble(Float.toString(y));
		double tempx = x - turret.getPosition().x -turret.getWidth()/2;
		double tempy = y - turret.getPosition().y -turret.getHeight()/2;
		
		double angle = calculateAngle(tempx ,tempy);
		//float tempAngle = (float)angle;
		
		Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle)).scl(Gdx.graphics.getDeltaTime()*bulletSpeed);
		
		Texture bullet = worldRenderer.getTexture("playerbullet");
		//These move the bullet to the end of the turret barrel
		float x1 = turret.getHeight()/2 * (float)Math.sin(angle);
		float y1 = turret.getHeight()/2 * (float)Math.cos(angle);

		Vector2 position = new Vector2(turret.getPosition().x + turret.getWidth()/2 + x1 -bullet.getWidth()/2,
				turret.getPosition().y + turret.getHeight()/2 + y1 - bullet.getHeight()/2);

		Bullet b = new Bullet(position, bulletSpeed, (float)angle, bullet.getWidth(),bullet.getHeight(),
				calcVelocity,ship.getDamage()*3, bullet.getWidth(),bullet.getHeight());

		//Add bullet to draw list
		worldRenderer.addDrawn("bullet"+b.hashCode(), "playerbullet", b.getPosition(), new Vector2(b.getWidth()/2,b.getHeight()/2),
				new Vector2(b.getWidth(),b.getHeight()), new Vector2(1,1),b.getRotation(), new Vector2(0,0), 
				new Vector2(bullet.getWidth(),bullet.getHeight()),
				false, false, true);
		
		
		turret.addBullet(b);
		turret.setAmmo(turret.getAmmo()- 1);
		audioHandler.turretFire();
	}
	
	public void Fire(float x , float y,Sniper sniper)
	{
		float bulletSpeed = 600f;
		//double tempx = Double.parseDouble(Float.toString(x));
		//double tempy = Double.parseDouble(Float.toString(y));	
		double tempx = x - sniper.getPosition().x -sniper.getWidth()/2;
		double tempy = y - sniper.getPosition().y -sniper.getHeight()/2 ;
		
		double angle = calculateAngle(tempx ,tempy);
		//float tempAngle = (float)angle;
		
		Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle)).scl(Gdx.graphics.getDeltaTime()*bulletSpeed);
		
		Texture bullet = worldRenderer.getTexture("playerbullet");
		//These move the bullet to the end of the turret barrel
		float x1 = sniper.getHeight()/2 * (float)Math.sin(angle);
		float y1 = sniper.getHeight()/2 * (float)Math.cos(angle);

		Vector2 position = new Vector2(sniper.getPosition().x + sniper.getWidth()/2 + x1 -bullet.getWidth()/2,
				sniper.getPosition().y + sniper.getHeight()/2 + y1 - bullet.getHeight()/2);

		Bullet b = new Bullet(position, bulletSpeed, (float)angle, bullet.getWidth(),bullet.getHeight(),
				calcVelocity,3f, bullet.getWidth(),bullet.getHeight());
		
		//Add bullet to draw list
		worldRenderer.addDrawn("bullet"+b.hashCode(), "sniperBullet", b.getPosition(), new Vector2(b.getWidth()/2,b.getHeight()/2),
				new Vector2(b.getWidth(),b.getHeight()), new Vector2(1,1),b.getRotation(), new Vector2(0,0), 
				new Vector2(worldRenderer.getTexture("playerbullet").getWidth(),worldRenderer.getTexture("playerbullet").getHeight()),
				false, false, true);
		
		world.addEnemyBullet(b);
		
		audioHandler.turretFire();
	}
	
	/**
	 * 
	 * Fire an enemy bullet
	 * @param x X position of target
	 * @param y Y position of target
	 * @param firePosition Position being fired from
	 * @return returns the angle fired at
	 */
	public void Fire(float x , float y, Vector2 firePosition, Elite el)
	{
		float bulletSpeed = 600f;
		//double tempx = Double.parseDouble(Float.toString(x));
		//double tempy = Double.parseDouble(Float.toString(y));
		double tempx = x - firePosition.x;
		double tempy = y - firePosition.y;	
		
		double angle = calculateAngle(tempx, tempy);
		//float tempAngle = (float)angle;
		
		Vector2 calcVelocity = new Vector2((float)Math.sin(angle), (float)Math.cos(angle)).scl(Gdx.graphics.getDeltaTime()*bulletSpeed);
		
		Texture bullet = worldRenderer.getTexture("playerbullet");
		Vector2 bulletSize = new Vector2(bullet.getWidth(), bullet.getHeight());
		
		//these move the bullet to the front of the turret
		float x1 = el.getTurretSize().y/2 * (float)Math.sin(angle);
		float y1 = el.getTurretSize().y/2 * (float)Math.cos(angle);
		
		Bullet b = new Bullet(new Vector2(firePosition.x - bulletSize.x/2 + x1, firePosition.y-bulletSize.y/2 + y1), bulletSpeed, 
				(float)angle-90, bulletSize.x , bulletSize.y,calcVelocity,5f, bullet.getWidth(),bullet.getHeight());
		
		//Add bullet to draw list
		worldRenderer.addDrawn("bullet"+b.hashCode(), "eliteBullet", b.getPosition(), new Vector2(b.getWidth()/2,b.getHeight()/2),
				new Vector2(b.getWidth(),b.getHeight()), new Vector2(1,1),b.getRotation(), new Vector2(0,0), 
				bulletSize,	false, false, true);

		world.addEnemyBullet(b);
		
		audioHandler.turretFire();
		el.setTurretRotation(calcVelocity.angle()-90);
		
	}
	
	public double calculateAngle(double x, double y)
	{
		double tempValue = Math.atan2(x,y);
		return tempValue; 
	}
}