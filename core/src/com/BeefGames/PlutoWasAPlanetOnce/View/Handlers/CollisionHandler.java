package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Planet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.TokenManager;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.Turret;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class CollisionHandler 
{
	private PixmapHandler pixHandler;
	private Iterator<Enemy> eIter;
	private Iterator<Bullet>bIter;
	private Enemy e;
	private Bullet b;
	private World world;
	private Color pixelColour;
	private TokenManager tokenManager;

	private WorldRenderer worldRenderer;
	private ParticleHandler particleHandler;
	
	private Boolean planetChange;
	
	private AudioHandler audioHandler;
	private Ship ship;
	Vector2 worldSize;
	private Boolean android = false;
	private PlutoWasAPlanetOnce game;
	private boolean lost = false;
	

	
	
	public CollisionHandler(PlutoWasAPlanetOnce game,Pixmap planetMap, Array<Enemy> enemies, Planet planet, WorldRenderer worldRenderer,World world,TokenManager tm,ParticleHandler particleHandler,Ship ship)
	{
		pixHandler = new PixmapHandler(planetMap);
		
		this.worldRenderer = worldRenderer;
		pixelColour = new Color();
		this.world = world;
		tokenManager = tm;
		this.game = game;
		
		this.particleHandler = particleHandler;
		audioHandler = world.getAudioHandler();
		this.ship = ship;
		
		this.worldSize = world.getWorldSize();
		//planet.setInitialPixels(pixHandler.countPixels());
		//planet.setCurrentPixels(planet.getInitialPixels());
		
		//planet.setMaxHealth(((float)planet.getCurrentPixels()/(float)planet.getInitialPixels())*100f);
		//planet.setHealth(planet.getMaxHealth());
		planetChange = false;
		//game.getAssetManager().get("data/planetv1.png", Pixmap.class)
	}
	
	public void checkEnemyCollisions(Array<Enemy> es,Array<Bullet> bs){
		// Collision detection for bullets
		eIter = es.iterator();
		while (eIter.hasNext()) 
		{
			e = eIter.next();
			float eHealth = e.getHealth();

			bIter = bs.iterator();
			while (bIter.hasNext()) 
			{
				b = bIter.next();
				
				//Delete bullets outside the world
				if(Math.abs(b.getPosition().x) > worldSize.x/2 || Math.abs(b.getPosition().y) > worldSize.y/2)
				{
					worldRenderer.removeDrawn("bullet" + b.hashCode());
					bIter.remove();
				}
				else
				{
				
					float bDamage = b.getDamage();

					// If enemy and bullet overlap:
					if(Intersector.overlapConvexPolygons(e.getPolyBounds(), b.getPolyBounds())){
						// Damage enemy
						eHealth -= bDamage;
						e.setHealth(eHealth);
						particleHandler.addBulletHit(b);
						audioHandler.enemyHit();
						// Delete bullet
						worldRenderer.removeDrawn("bullet" + b.hashCode());
						bIter.remove();

						// Delete enemy if it is dead
						if (e.getHealth() <= 0) 
						{
							e.setDestroyed(true);
							eIter.remove();
							worldRenderer.removeDrawn("enemy" + e.hashCode());	
							worldRenderer.removeDrawn("newFollowerExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newBombTrails"+e.hashCode());
							worldRenderer.removeDrawn("newSniperExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newEliteExhaust"+e.hashCode());

							audioHandler.enemyDestroy();
							world.addKill();
							
							if(e.getType() == "Elite")
							{
								worldRenderer.removeDrawn("enemyturret"+e.hashCode());
								world.addEliteKill();
							}
						
						
						
							if(e.getType() != "Elite")
							{
								tokenManager.addToken(new Vector2(e.getPosition().x + e.getWidth()/2, e.getPosition().y + e.getHeight()/2),
										world.getShip(), e.getVelocity().cpy());
							}
							ship.setMoney(ship.getMoney()+ e.getValue());
							particleHandler.addExplosion(e);
						
							world.setActionBeginTime(TimeUtils.nanoTime());
						
							break;
						}
					}	
				}
			}
		}
	}
	
	public void checkPlanetCollisions(Array<Enemy> es, Planet planet)
	{
		float[] points;	
		
		if(world.getProtectionBubble())	
		{
			eIter = es.iterator();
			
			while(eIter.hasNext())
			{
				e = eIter.next();	

				//Points describing hit-shape of enemy
				points = e.getPolyBounds().getTransformedVertices();

				for(int i = 0; i < points.length; i += 2)
				{
					if(world.getBubble().getCircleBounds().contains(new Vector2(points[i], points[1+i])))
					{
						if(e.getType() == "Elite"){
							
							
							worldRenderer.removeDrawn("enemyturret"+e.hashCode());
							world.addEliteKill();
						}
						e.setDestroyed(true);
						eIter.remove();
						particleHandler.addCrash(e);
						worldRenderer.removeDrawn("enemy" + e.hashCode());
						worldRenderer.removeDrawn("newFollowerExhaust"+e.hashCode());
						worldRenderer.removeDrawn("newBombTrails"+e.hashCode());
						worldRenderer.removeDrawn("newSniperExhaust"+e.hashCode());
						worldRenderer.removeDrawn("newEliteExhaust"+e.hashCode());
						world.getBubble().hit();
						break;
					}
				}
				if(world.getBubble().getHealth()<=0)
				{
					particleHandler.addProtection(world.getBubble().getPosition().x + world.getBubble().getWidth()/2, world.getBubble().getPosition().y + world.getBubble().getHeight()/2);
					worldRenderer.removeDrawn("protectionBubble");
					world.setProtection(false);
				}		
			}
		}
		
		planetChange = false;
		
		//Collision detection for planet
		if(!world.getProtectionBubble())	
		{
			eIter = es.iterator();
			
			while (eIter.hasNext()) 
			{
				e = eIter.next();	

				//Points describing hit-shape of enemy
				points = e.getPolyBounds().getTransformedVertices();

				for(int i = 0; i < points.length; i += 2)
				{
					if(planet.getCircleBounds().contains(new Vector2(points[i], points[1+i])))
					{
						Vector2 newPosition = new Vector2(0,0);
						//pixHandler.project(newPosition, e.getPosition().x + e.getWidth()/2, e.getPosition().y +e.getHeight()/2 );
						pixHandler.project(newPosition, points[i], points[1+i] );
						Color.rgba8888ToColor(pixelColour, pixHandler.getPixel(newPosition));  
						if(pixelColour.a != 0)
						{
							pixHandler.eraseCircle(newPosition.x, newPosition.y, e.getCircleBounds().radius);
							audioHandler.planetHit();
							if(!lost){
							particleHandler.addPlanetHit(e);

							// Delete enemy
							worldRenderer.removeDrawn("enemy" + e.hashCode());
							worldRenderer.removeDrawn("newFollowerExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newBombTrails"+e.hashCode());
							worldRenderer.removeDrawn("newSniperExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newEliteExhaust"+e.hashCode());

							if(e.getType() == "Elite")
							{
								worldRenderer.removeDrawn("enemyturret"+e.hashCode());
							}
							e.setDestroyed(true);
							eIter.remove();
							world.setActionBeginTime(TimeUtils.nanoTime());
							planetChange = true;
							}
							if(planet.getCoreCircle().contains(points[i], points[1+i]))
							{
								
								
								if(!lost){
									world.getGameScreen().setStatus(true,true);
									particleHandler.addPlanetDestruction(0, 0);
									worldRenderer.setVisible("planet", false);
									lost = true;
									
								}
									planetChange = false;
								
								
								
							}
							break;
						}
					}
				}
			}
		}
		
		if(planetChange)
		{
			//Update edited texture
			worldRenderer.addTexture("planet", pixHandler.update(), TextureFilter.Linear, TextureFilter.Linear);
			worldRenderer.setDrawableTexture("planet", "planet");
		
			//Count number of non-transparent pixels in planet texture
			//planet.setCurrentPixels(pixHandler.countPixels());
			
			float newHealth = ((float)planet.getCurrentPixels()/(float)planet.getInitialPixels())*100f;
			newHealth = Math.round(newHealth);
			
			planet.setHealth(newHealth);
		}
	}
	
	public void checkTurretCollisions(Turret turret,Array<Enemy> es,Array<Bullet> bs)
	{
		eIter = es.iterator();
		while (eIter.hasNext()) {
			e = eIter.next();
			float eHealth = e.getHealth();

			bIter = bs.iterator();
			while (bIter.hasNext())
			{
				b = bIter.next();
				
				//Delete bullets outside the world
				if(Math.abs(b.getPosition().x) > worldSize.x/2 || Math.abs(b.getPosition().y) > worldSize.y/2)
				{
					worldRenderer.removeDrawn("bullet" + b.hashCode());
					bIter.remove();
				}
				else
				{
					float bDamage = b.getDamage();

					// If enemy and bullet overlap:
					if(Intersector.overlapConvexPolygons(e.getPolyBounds(), b.getPolyBounds()))
					{
						audioHandler.enemyHit();
						// Damage enemy
						eHealth -= bDamage;
						e.setHealth(eHealth);

						// Delete bullet
						worldRenderer.removeDrawn("bullet" + b.hashCode());
						bIter.remove();

						// Delete enemy if it is dead
						if (e.getHealth() <= 0) {
							worldRenderer.removeDrawn("enemy" + e.hashCode());
							worldRenderer.removeDrawn("newFollowerExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newBombTrails"+e.hashCode());
							worldRenderer.removeDrawn("newSniperExhaust"+e.hashCode());
							worldRenderer.removeDrawn("newEliteExhaust"+e.hashCode());

							if(e.getType() == "Elite")
							{
								worldRenderer.removeDrawn("enemyturret"+e.hashCode());
								world.addEliteKill();
							}
							e.setDestroyed(true);
							world.addTurretKill();
							eIter.remove();
							audioHandler.enemyDestroy();
							world.addKill();
							ship.setMoney(ship.getMoney()+ e.getValue()/2);
							//tokenManager.addToken(e.getPosition(), ship);
							
							particleHandler.addExplosion(e);
						}
						world.setActionBeginTime(TimeUtils.nanoTime());

						// to clean up array that expands once bullet is a certain
						// distance remove array and iterator
					}
				}
			}
		}
	}
	public void checkShipCollision(Ship ship,Array<Enemy>es)
	{
		float collisiondamage = 10f;

		// Collision detection for ship
		eIter = es.iterator();
		while (eIter.hasNext()) {
			e = eIter.next();
			collisiondamage = e.getDamage();
			float sHealth = ship.getHealth();
			float sArmour = ship.getArmour();
			if(Intersector.overlapConvexPolygons(e.getPolyBounds(), ship.getPolyBounds()) && e.getType()!= "Elite") 
			{
				audioHandler.enemyHit();
				particleHandler.addCrash(e);
				// Damage ship
				//Damage armour first
				if(sArmour != 0){
					sArmour -= collisiondamage;
					//If damage does more than the remaining armour
					if(sArmour < 0){
						sHealth += sArmour;
						ship.setArmour(0);
					}
					else{
						ship.setArmour(sArmour);
					}
				}
				else{
					sHealth -= collisiondamage;
				}
				// Check if the game has been lost
				if (sHealth <= 0) {
					sHealth = 0;
					world.getGameScreen().setStatus(true,false);
				}
				// Update ship health
				ship.setHealth(sHealth);

				// Delete enemy
				worldRenderer.removeDrawn("enemy" + e.hashCode());
				worldRenderer.removeDrawn("newFollowerExhaust"+e.hashCode());
				worldRenderer.removeDrawn("newBombTrails"+e.hashCode());
				worldRenderer.removeDrawn("newSniperExhaust"+e.hashCode());
				worldRenderer.removeDrawn("newEliteExhaust"+e.hashCode());
				e.setDestroyed(true);
				if(e.getType() == "Elite")
				{
					worldRenderer.removeDrawn("enemyturret"+e.hashCode());
				}
				eIter.remove();
				//kills++;

				world.setActionBeginTime(TimeUtils.nanoTime());
			}
		}
	}
	
public void checkSniperCollision(Ship ship,Array<Bullet>bs)
{
		// Collision detection for ship
		bIter = bs.iterator();
		while (bIter.hasNext()) 
		{
			b = bIter.next();
			
			//Delete bullets outside the world
			if(Math.abs(b.getPosition().x) > worldSize.x/2 || Math.abs(b.getPosition().y) > worldSize.y/2)
			{
				worldRenderer.removeDrawn("bullet" + b.hashCode());
				bIter.remove();
			}
			else
			{
				float sHealth = ship.getHealth();
				float sArmour = ship.getArmour();
				if (Intersector.overlapConvexPolygons(b.getPolyBounds(), ship.getPolyBounds())) {
					audioHandler.enemyHit();
					particleHandler.addShipHit(b);
					// Damage ship
					//Damage armour first
					if(sArmour != 0){
						sArmour -= b.getDamage();
						//If damage does more than the remaining armour
						if(sArmour < 0){
							sHealth += sArmour;
							ship.setArmour(0);
						}
						else{
							ship.setArmour(sArmour);
						}	
					}
					else{
						sHealth -= b.getDamage();
					}
					// Check if the game has been lost
					if (sHealth <= 0) {
						sHealth = 0;
						world.getGameScreen().setStatus(true,false);
					}
					// Update ship health
					ship.setHealth(sHealth);

					// Delete bullet
					worldRenderer.removeDrawn("bullet" + b.hashCode());
					bIter.remove();
				}
			}
		}
	}
}