package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Star 
{
	private Vector2 position;
	private Color colour;
	private float scale;


	public Star(Vector2 position, Color colour, float scale)
	{
		this.position = position;
		this.colour = colour;
		this.scale = scale;
	}
	
	public void draw(SpriteBatch batch, Texture star, float a)
	{
		colour.set(colour.r, colour.g, colour.b, a);
		batch.setColor(colour);
		batch.draw(star, position.x, position.y, scale*star.getWidth(), scale*star.getHeight());
	}
	
	
}
