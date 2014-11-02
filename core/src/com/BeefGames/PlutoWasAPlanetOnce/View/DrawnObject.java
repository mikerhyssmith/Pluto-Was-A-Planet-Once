package com.BeefGames.PlutoWasAPlanetOnce.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DrawnObject 
{
	private String name;

	private int type;
	
	private Boolean visible;
	
	//These are the different types of things that can be drawn
	private Sprite sprite;
	private ParticleEmitter emitter;
	
	private Boolean needsRemoving;
	
	/**
	 * Create a sprite to be drawn
	 * @param name
	 * @param textureName
	 * @param position
	 * @param origin
	 * @param dimensions
	 * @param scale
	 * @param rotation
	 * @param srcPosition
	 * @param srcSize
	 * @param flipX
	 * @param flipY
	 * @param visible
	 */
	public DrawnObject(String name, Texture texture, Vector2 position, Vector2 origin, Vector2 dimensions,
			Vector2 scale, Float rotation, Vector2 srcPosition, Vector2 srcSize, Boolean flipX, Boolean flipY, Boolean visible)
	{
		this.name = name;
		//Sprite is type 1
		this.type = 1;

		this.visible = visible;
		
		sprite = new Sprite(texture, (int)srcPosition.x, (int)srcPosition.y, (int)srcSize.x, (int)srcSize.y);
		
		sprite.setRotation(rotation);
		sprite.setOrigin(origin.x, origin.y);
		sprite.setScale(scale.x, scale.y);
		
		sprite.setBounds(position.x, position.y, dimensions.x, dimensions.y);
		
		this.needsRemoving = false;
	}

	/**
	 * Create an emitter to be drawn
	 * @param name
	 * @param emitter
	 */
	public DrawnObject(String name, ParticleEmitter emitter, Boolean visible)
	{
		this.name = name;
		this.type = 2;
		this.visible = visible;
		
		this.emitter = emitter;
		
		this.needsRemoving = false;
	}
	
	/**
	 * Update method for sprite
	 * @param position
	 * @param origin
	 * @param dimensions
	 * @param scale
	 * @param rotation
	 */
	public void updateDrawn(Vector2 position, Vector2 origin, Vector2 dimensions,
			Vector2 scale, float rotation)
	{	
		sprite.setOrigin(origin.x, origin.y);
		sprite.setRotation(rotation);
		sprite.setScale(scale.x, scale.y);
		sprite.setBounds(position.x, position.y, dimensions.x, dimensions.y);
	}

	/**
	 * Update method for emitter 
	 */
	public void updateDrawn()
	{
		if(type ==2)
		{
			if(emitter.isComplete())	
			{	
				needsRemoving = true;
			}
		}
	}
	
	
	public void draw(SpriteBatch batch, float delta)
	{
		try
		{
			switch(type){
				case 1: sprite.draw(batch);
				break;
				
				case 2: emitter.draw(batch, delta);	
				break;
				
				default: throw new IndexOutOfBoundsException();
			}
		}catch(IndexOutOfBoundsException e)
		{
			System.out.println("Cannot draw " + name + " - invalid drawing type");
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setTexture(Texture texture) {
		this.sprite.setRegion(texture);
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Boolean needsRemoval(){
		return needsRemoving;
	}
}
