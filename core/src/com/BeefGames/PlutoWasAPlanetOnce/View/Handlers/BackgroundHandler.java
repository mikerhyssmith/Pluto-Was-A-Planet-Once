package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import java.util.Iterator;
import java.util.Random;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Star;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class BackgroundHandler extends Actor
{
	private int starNumber;
	private Vector2 worldSize;
	private Texture star;
	private Array<Star> stars;
	private Random random;
	
	
	private Iterator<Star> starIter;
	private Star s;
	private Boolean nightmareMode;
	private Boolean onStage;
	
	public BackgroundHandler(int starNumber, Vector2 worldSize, Texture star, Boolean nightmare, Boolean onStage)
	{
		this.starNumber = starNumber;
		this.worldSize = worldSize;
		this.star = star;
		this.nightmareMode = nightmare;
		this.onStage = onStage;
		this.star.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		random = new Random();
		this.stars = new Array<Star>(starNumber);

		generateStars();
	}
	
	/**
	 * Used to draw the background
	 */
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		starIter = stars.iterator();
		while(starIter.hasNext())
		{
			s = starIter.next();
			s.draw(batch, star, parentAlpha);
		}
		Color starColour = Color.WHITE;
		starColour.set(starColour.r, starColour.g, starColour.b, 1);
		batch.setColor(starColour);
	}
	
	/**
	 * Call once per game, to generate the background
	 */
	public void generateStars()
	{
		Vector2 newPosition;
		Color newColour = new Color(Color.RED);
		float newScale;
		
		for(int i = 0; i < starNumber; i++)
		{
			if(!onStage)
			{
				newPosition = new Vector2(random.nextInt((int)worldSize.x) - worldSize.x/2, 
						random.nextInt((int)worldSize.y) - worldSize.y/2);
			}
			else
			{
				newPosition = new Vector2(random.nextInt((int)worldSize.x), 
						random.nextInt((int)worldSize.y));
			}
			newScale = random.nextFloat();
			
			if(newScale > 0.75)
				newScale -= 0.75;
			if(newScale < 0.25)
				newScale += 0.25;

			int col = random.nextInt(5);
			
			if(!nightmareMode)
			{
				switch(col){
					case 0: newColour = Color.WHITE;
						break;
					case 1: newColour = Color.RED;
						break;
					case 2: newColour = Color.ORANGE;
						break;
					case 3: newColour = Color.YELLOW;
						break;
					case 4: newColour = Color.CYAN;
						break;
				}
			}
			
			stars.add(new Star(newPosition, newColour, newScale));
		}
	}
}
