package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Planet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.Token;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class MinimapHandler extends Actor
{
	//Minimap images
	TextureRegion background;
	TextureRegion border;
	TextureRegion playerDot;
	TextureRegion enemyDot;
	TextureRegion tokenDot;
	TextureRegion planetDot;
	
	//Stuff to draw
	Array<Enemy> es;
	Ship player;
	Array<Token> ts;
	Planet planet;
	
	//Location of minimap on screen
	Vector2 mapPosition;
	
	//Temporary variables
	Iterator<Enemy> eIter;
	Iterator<Token> tIter;
	Enemy e;
	Token t;
	
	//Size of world and scaling for minimap
	Vector2 worldSize;
	float scalex;
	float scaley;
	
	
	public MinimapHandler(TextureRegion background, TextureRegion border, TextureRegion playerDot, TextureRegion enemyDot,
			TextureRegion tokenDot, TextureRegion planetDot, Array<Enemy> enemies, Ship player, Array<Token> tokens,
			Planet planet, Vector2 mapPosition, Vector2 worldSize)
	{
		this.background = background;
		this.border = border;
		this.playerDot = playerDot;
		this.enemyDot = enemyDot;
		this.tokenDot = tokenDot;
		this.planetDot = planetDot;
		this.es = enemies;
		this.player = player;
		this.ts = tokens;
		this.planet = planet;
		this.mapPosition = mapPosition;
		this.setX(mapPosition.x);
		this.setY(mapPosition.y);
		
		
		this.worldSize = worldSize;
		scalex = worldSize.x / background.getRegionWidth();
		scaley = worldSize.y / background.getRegionHeight();
	}
	
	/**
	 * Updates the minimaps info
	 * @param enemies
	 * @param player
	 * @param tokens
	 */
	public void update(Array<Enemy> enemies, Ship player, Array<Token> tokens)
	{
		this.es = enemies;
		this.ts = tokens;
		this.player = player;
	}
	
	/**
	 * Draws the minimap (called by the stage the minimap is part of)
	 */
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		batch.draw(background, mapPosition.x, mapPosition.y, background.getRegionWidth(), background.getRegionHeight());

		eIter = es.iterator();
		while(eIter.hasNext())
		{
			e = eIter.next();
			
			if(Math.abs(e.getPosition().x + e.getWidth()/2) <= worldSize.x/2 && Math.abs(e.getPosition().y + e.getHeight()/2) <= worldSize.y/2)
			{
				batch.draw(enemyDot, ((e.getPosition().x + e.getWidth()/2 + worldSize.x/2)/scalex) + mapPosition.x - enemyDot.getRegionWidth()/2,
						((e.getPosition().y + e.getHeight()/2 + worldSize.y/2)/scaley) + mapPosition.y - enemyDot.getRegionHeight()/2,
						enemyDot.getRegionWidth(), enemyDot.getRegionHeight());
			}
		}
		
		tIter = ts.iterator();
		while(tIter.hasNext())
		{
			t = tIter.next();
			
			if(Math.abs(t.getPosition().x) <= worldSize.x/2 && Math.abs(t.getPosition().y) <= worldSize.y/2)
			{
				batch.draw(tokenDot, ((t.getPosition().x + t.getWidth()/2 + worldSize.x/2)/scalex) + mapPosition.x - tokenDot.getRegionWidth()/2, 
						((t.getPosition().y + t.getHeight()/2 + worldSize.y/2)/scaley) + mapPosition.y - tokenDot.getRegionHeight()/2,
						tokenDot.getRegionWidth(), tokenDot.getRegionHeight());
			}
		}
		
		batch.draw(planetDot, ((planet.getPosition().x + worldSize.x/2)/scalex) + mapPosition.x - planetDot.getRegionWidth()/2,
		((planet.getPosition().y + worldSize.y/2)/scaley) + mapPosition.y - planetDot.getRegionHeight()/2,
		planetDot.getRegionWidth(), planetDot.getRegionHeight());
		
		batch.draw(playerDot, ((player.getPosition().x + player.getWidth()/2 + worldSize.x/2)/scalex) + mapPosition.x - playerDot.getRegionWidth()/2,
		((player.getPosition().y +player.getHeight()/2 + worldSize.y/2)/scaley) + mapPosition.y - playerDot.getRegionHeight()/2,
		playerDot.getRegionWidth(), playerDot.getRegionHeight());
		
		batch.draw(border, mapPosition.x - ((border.getRegionWidth() - background.getRegionWidth())/2),
				mapPosition.y - ((border.getRegionHeight() - background.getRegionHeight())/2),
				border.getRegionWidth(), border.getRegionHeight());
	}
}
