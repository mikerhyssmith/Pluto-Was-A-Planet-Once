package com.BeefGames.PlutoWasAPlanetOnce.Wave;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Elite;
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
	private int waveLimit,waveNumber;
	private int gameMode;
	private int eliteLevel;
	private boolean eliteSpawned;
	private ParticleHandler particleHandler;
	
	private WorldRenderer renderer;
	
	//Constructor
	public WaveManager(int Difficulty,float Width,float Height,World world,int gameMode, WorldRenderer renderer)
	{
		this.gameMode = gameMode;
		spawnManager = new SpawnManager(Width,Height);
		lastSpawnTime =0;
		waveLimit =9;
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
		if(gameMode ==1)
		{
			if(waveNumber <= waveLimit)
			{
				if(waveNumber == eliteLevel)
				{
					createEliteWave(renderer, 300 + (level*10),5 + (5*level), 50 + level);
					waveNumber ++;
					eliteLevel +=5;
				}
				else
				{
					setEnemies(2+level*difficulty, (float)Math.sqrt(level*4)*difficulty, 10, (float)Math.sqrt(level*3)*1.5f, planetPosition, debug);
					waveNumber++;
				}	
			}
			else
			{
				world.getGameScreen().setStatus(true,false);
			}
		}
		else if(gameMode ==2)
		{
			if(waveNumber == eliteLevel)
			{
				createEliteWave(renderer, 1000 + (level*20),10 + (10*level), 50 + level);
				waveNumber ++;
				eliteLevel +=5;
			}
			else
			{
				setEnemies(2+2*level*difficulty, 2 + level*difficulty, 10, 5 + level, planetPosition, debug);
				waveNumber++;
			}	
		}
		else
		{
			if(waveNumber == eliteLevel)
			{
				createEliteWave(renderer, 400 + (level*9),2 + (4*level), 50 + level);
				waveNumber ++;
				eliteLevel +=5;
			}
			else
			{
				setEnemies(2+level*difficulty, (float)Math.sqrt(level*4)*difficulty, 10, (float)Math.sqrt(level*3)*1.5f, planetPosition, debug);
				waveNumber++;
			}	

		}
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
				renderer.addRect(f.getBounds());
				renderer.addRect(p.getBounds());
				renderer.addRect(s.getBounds());
				renderer.addRect(sn.getBounds());
			}
		}
	}
	
	public void createEliteWave(WorldRenderer renderer, float health, float damage, int moneyValue)
	{
		float spiralFactor = 150;
		
		Texture elite = renderer.getTexture("Elite");
		
		Vector2 position = new Vector2(0, spawnManager.getWorldHeight()/2);
		
		Elite  el = new Elite(position, elite.getWidth(),elite.getHeight(), spawnManager.getSpeed(),
				health, moneyValue, damage, world, spiralFactor, world.getWorldSize(), 5, 1000,1, elite.getWidth(),elite.getHeight());
		
		renderer.addDrawn("enemy"+ el.hashCode(), "Elite", el.getPosition(), new Vector2(el.getWidth()/2,el.getHeight()/2),
				new Vector2(el.getWidth(),el.getHeight()), new Vector2(1,1),el.getRotation(), new Vector2(0,0), 
				new Vector2(elite.getWidth(),elite.getHeight()),
				false, false, true);
		
		Texture turret = renderer.getTexture("eliteTurret");
		el.setTurretSize(new Vector2(turret.getWidth(), turret.getHeight()));
		el.setTurretOrigin(new Vector2(turret.getWidth()/2, turret.getHeight()/2));
		
		renderer.addDrawn("enemyturret" + el.hashCode(), "eliteTurret", el.getTurretPosition(),
				el.getTurretOrigin(), el.getTurretSize(),
				new Vector2(1,1), el.getTurretRotation(), new Vector2(0,0), el.getTurretSize(),
				false, false, true);

		particleHandler.addExhaust(el);
		enemies.add(el);
		eliteSpawned = true;
	}
	
	public void setNightmareEnemies(int enemyNumber, float enemyhealth, int enemyvalue, float enemydamage, 
			Vector2 planetLocation, Boolean debug)
	{
		//Decide probabilities for spawning
		int mostCommon = spawnManager.randInt(4);
		float probs[] = new float[4];
		for(int i = 0; i <4; i++)
		{
			if(i ==mostCommon)
			{
				probs[i] = 0.4f; 
			}
			else probs[i] = 0.2f;
		}
		
		//0: follower, 1: planetscout, 2; sniper, 3: soldier
		for (int i =0; i < 4*enemyNumber; i++)
		{
			float choice = spawnManager.randFloat();
			
			if(choice < probs[0])
			{
				Texture follower = renderer.getTexture("follower");
				Follower f = new Follower(spawnManager.getPosition(),follower.getWidth(),follower.getHeight(), 0, 
							spawnManager.getSpeed(),enemyhealth, enemyvalue, enemydamage,follower.getWidth(),follower.getHeight());
				tempEnemies.add(f);

				
			}
			else if(choice < (probs[0] + probs[1]))
			{
				Texture planetScout =  renderer.getTexture("planetscout");
				
				PlanetScout p = new PlanetScout(new Vector2(spawnManager.getPosition()), planetScout.getWidth(), planetScout.getHeight(), 0,
						spawnManager.getSpeed(),planetLocation,enemyhealth, enemyvalue, enemydamage,  planetScout.getWidth(), planetScout.getHeight());
				tempEnemies.add(p);		

				
			}
			else if(choice < (probs[0] + probs[1] + probs[2]))
			{
				Texture sniper = renderer.getTexture("Sniper");
				
				Sniper sn = new Sniper(spawnManager.getPosition(),sniper.getWidth(),sniper.getHeight(), 0, 
						spawnManager.getSpeed(),enemyhealth, enemyvalue, enemydamage,world,sniper.getWidth(),sniper.getHeight());
				tempEnemies.add(sn);

			
			}
			else
			{
				Texture soldier = renderer.getTexture("soldier");
				
				Soldier s = new Soldier(spawnManager.getPosition(),soldier.getWidth(), soldier.getHeight(), 0,
						spawnManager.getSpeed(),planetLocation,enemyhealth, enemyvalue, enemydamage,soldier.getWidth(), soldier.getHeight());
				tempEnemies.add(s);

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

				
				if(e.getType() == "Elite")
				{
					if(e.SpawnEnemy())
					{
						float ehealth = 5;
						int evalue = 0;
						float edamage = 5 + e.getDamage()/5;
						
						Texture soldier = renderer.getTexture("Soldier");
						
						Vector2 position = new Vector2(e.getPosition().x + e.getWidth()/2,
								e.getPosition().y + e.getHeight()/2);
						
						Soldier s = new Soldier(position, soldier.getWidth()/2,soldier.getHeight()/2, 0,
								spawnManager.getSpeed(), planetPosition ,ehealth, evalue, edamage, soldier.getWidth(),
								soldier.getHeight());
						
						particleHandler.addExhaust(s);
						
						enemies.add(s);
						particleHandler.addEliteSpawn(s);
						renderer.addDrawn("enemy"+ s.hashCode(), "Soldier", s.getPosition(), new Vector2(s.getWidth()/2,s.getHeight()/2),
								new Vector2(s.getWidth(),s.getHeight()), new Vector2(1,1),s.getRotation(), new Vector2(0,0), 
								new Vector2(renderer.getTexture("Soldier").getWidth(),renderer.getTexture("Soldier").getHeight()),
								false, false, true);
					}
				}
				
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
