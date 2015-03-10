package com.BeefGames.PlutoWasAPlanetOnce.Wave;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Follower;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.PlanetScout;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Sniper;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Soldier;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.ParticleHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class WaveManager 
{
	float timeSinceCollision = 0;
	private SpawnManager spawnManager;
	private long currentTime,lastSpawnTime;

	private Array<Enemy> enemies = new Array<Enemy>(); // <> arrow type
	private Iterator<Enemy> eIter;
	
	private Array<Enemy> tempEnemies;
	private Enemy e;
	private int enemyNumbers;
	private int enemiesSpawned;
	private World world;
	private int waveNumber;
	private int eliteLevel;
	private boolean eliteSpawned;
	private ParticleHandler particleHandler;
	
	private WorldRenderer renderer;
	
	//Constructor
	public WaveManager(float Width,float Height,World world, WorldRenderer renderer)
	{
		spawnManager = new SpawnManager(Width,Height);
		lastSpawnTime =0;
		tempEnemies = new Array<Enemy>();
		this.world = world;
		eliteLevel = 4;
		this.renderer = renderer;
	}
	
	public void createWave(Vector2 planetPosition, int level, int difficulty, Boolean debug)
	{
		//Use this method to create more complicated scaling with level + difficulty
		//Initial scaling is v. simple
		enemiesSpawned = 0;
		eliteSpawned = false;
		System.out.println(waveNumber);
		System.out.println(eliteLevel);

		setEnemies(level*level, (float)Math.sqrt(level*4)*difficulty, 10, (float)Math.sqrt(level*3)*1.5f, planetPosition, debug);
		waveNumber++;
				

	}
	
	//Method for creating enemies
	public void setEnemies(int enemyNumber, float enemyhealth, int enemyvalue, float enemydamage, 
			Vector2 planetLocation, Boolean debug)
	{
		//For now all enemy types have the same health and value		
		enemyNumbers = enemyNumber;
		for (int i =0; i < enemyNumber; i++){
			
			Texture follower = renderer.getTexture("Follower");
			Follower f = new Follower(spawnManager.getPosition(),follower.getWidth(),follower.getHeight(), 0, 
					spawnManager.getSpeed(),enemyhealth, enemyvalue, enemydamage,follower.getWidth(),follower.getHeight());
			tempEnemies.add(f);
			
			Texture planetScout =  renderer.getTexture("PlanetScout");
			
			PlanetScout p = new PlanetScout(new Vector2(spawnManager.getPosition()), planetScout.getWidth(), planetScout.getHeight(), 0,
					spawnManager.getSpeed(),planetLocation,enemyhealth, enemyvalue, enemydamage,  planetScout.getWidth(), planetScout.getHeight());
			tempEnemies.add(p);		

			
			Texture soldier = renderer.getTexture("Soldier");
			
			Soldier s = new Soldier(spawnManager.getPosition(),soldier.getWidth(), soldier.getHeight(), 0,
					spawnManager.getSpeed(),planetLocation,enemyhealth, enemyvalue, enemydamage,soldier.getWidth(), soldier.getHeight());
			tempEnemies.add(s);

			
			Texture sniper = renderer.getTexture("Sniper");
			
			Sniper sn = new Sniper(spawnManager.getPosition(),sniper.getWidth(),sniper.getHeight(), 0, 
					spawnManager.getSpeed(),enemyhealth, enemyvalue, enemydamage,world,sniper.getWidth(),sniper.getHeight());
			tempEnemies.add(sn);
			
			
			if(debug)
			{
				/**
				renderer.addRect(f.getBounds());
				renderer.addRect(p.getBounds());
				renderer.addRect(s.getBounds());
				renderer.addRect(sn.getBounds());
				*/
			}
		}
	}
	

	/**
	 * Check if there are any enemies left
	 * @return 
	 */
	public boolean checkAlive()
	{
		if(enemies.size == 0 && (enemiesSpawned == (enemyNumbers*4)|| (eliteSpawned == true) ))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void update(Ship target, Vector2 planetPosition)
	{
		
		currentTime = TimeUtils.nanoTime();
		//Spawn enemies
		if(tempEnemies.size >0)
		{
			if(((currentTime - lastSpawnTime)/ 1000000000) > 0.5f){
				e = tempEnemies.first();
				renderer.addDrawn("enemy"+ e.hashCode(), e.getType(), e.getPosition(), new Vector2(e.getWidth()/2,e.getHeight()/2),
						new Vector2(e.getWidth(),e.getHeight()), new Vector2(1,1),e.getRotation(), new Vector2(0,0), 
						new Vector2(renderer.getTexture(e.getType()).getWidth(),renderer.getTexture(e.getType()).getHeight()),
						false, false, true);
				particleHandler.addExhaust(e);
				enemies.add(e);
				tempEnemies.removeIndex(0);
				lastSpawnTime = TimeUtils.nanoTime();
				enemiesSpawned++;

			}
		}
		
		//Update enemies	
		eIter = enemies.iterator();
		while (eIter.hasNext()) 
		{
				e = eIter.next();
				ParticleEmitter newExhaust;
				if(e.getType().equals("Follower")||e.getType().equals("Soldier")){
					float degAngle = e.getRotation();
					float angle = -((degAngle)/360) * 2 * (float)Math.PI;
					
					float x1 =- e.getHeight()/4 * (float)Math.sin(angle);
					float y1 =- e.getHeight()/4 * (float)Math.cos(angle);
					
					newExhaust = e.getEmitter();
					Vector2 position = new Vector2(e.getPosition().x + e.getWidth()/2 +x1,
							e.getPosition().y + e.getHeight()/2 + y1) ;
					newExhaust.setPosition(position.x, position.y);
					newExhaust.getAngle().setHigh(e.getRotation()-90);
					newExhaust.getAngle().setLow(e.getRotation()-90);
				}
				if(e.getType().equals("Sniper")){
					float degAngle = e.getRotation();
					float angle = -((degAngle)/360) * 2 * (float)Math.PI;
					float x1 =-3* e.getHeight()/8 * (float)Math.sin(angle);
					float y1 =-3* e.getHeight()/8 * (float)Math.cos(angle);
					
					newExhaust = e.getEmitter();

					Vector2 position = new Vector2(e.getPosition().x + e.getWidth()/2 +x1,
							e.getPosition().y + e.getHeight()/2 + y1) ;
					
					newExhaust.setPosition(position.x, position.y);
					newExhaust.getAngle().setHigh(e.getRotation()-90);
					newExhaust.getAngle().setLow(e.getRotation()-90);
				}
				if(e.getType().equals("PlanetScout")){
					
					float degAngle = e.getRotation();
					
					float angle = -((degAngle)/360) * 2 * (float)Math.PI;
					
					float x1 =-e.getHeight()/2 * (float)Math.sin(angle);
					float y1 =- e.getHeight()/2 * (float)Math.cos(angle);
					
					newExhaust = e.getEmitter();

					Vector2 position = new Vector2(e.getPosition().x + e.getWidth()/2 +x1,
							e.getPosition().y + e.getHeight()/2 + y1) ;
					
					newExhaust.setPosition(position.x, position.y);
					newExhaust.getAngle().setHigh(e.getRotation()-90);
					newExhaust.getAngle().setLow(e.getRotation()-90);
				}
				if(e.getType().equals("Elite")){
					
					float degAngle = e.getRotation();
					
					float angle = -((degAngle)/360) * 2 * (float)Math.PI;
					
					float x1 =-e.getHeight()/2 * (float)Math.sin(angle);
					float y1 =- e.getHeight()/2 * (float)Math.cos(angle);
					
					newExhaust = e.getEmitter();

					Vector2 position = new Vector2(e.getPosition().x + e.getWidth()/2 +x1,
							e.getPosition().y + e.getHeight()/2 + y1) ;
					
					newExhaust.setPosition(position.x, position.y);
					
					if(world.getDebug())
					{
						renderer.setPoints(e.getPolyBounds().getTransformedVertices());
						//System.out.println(e.getPolyBounds().getTransformedVertices()[0]);
					}
				}
				
				e.advance(Gdx.graphics.getDeltaTime(),target);

		}
    }

	public void dispose()
	{
		
	}
	
	//Arrays hold contents of wave
	public Array<Enemy> getenemies(){
		return this.enemies;
	}
	public void setenemies(Array<Enemy> enemies){
		this.enemies = enemies;
	}
	public void setParticleHandler(ParticleHandler handler)
	{
		this.particleHandler = handler;
	}
}
