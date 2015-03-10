package com.BeefGames.PlutoWasAPlanetOnce.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends MoveableEntity 
{

	private float damage;
	
	public float getDamage(){
		return damage;
	}
	public void setDamage(float damage){
		this.damage = damage;
	}
	
	
	public Bullet(Vector2 position, float speed, float rotation, float width,float height,Vector2 velocity,
			float damage, float texWidth, float texHeight) {
		super(position, speed, rotation, width, height,  texWidth,  texHeight);
		this.velocity = velocity;
		this.damage = damage;
		this.rotation = velocity.angle()-90;
	}
	
	
	@Override
	public void update()
	{
		position.add(velocity.nor().scl(Gdx.graphics.getDeltaTime()*SPEED));
		//rotation = velocity.angle()-90;
		
		super.update();
	}
	public float getSpeed(){
		return SPEED;
	}
	@Override
	public void updatePolyBounds()
	{
		polyBounds = new Polygon(new float[]{
				position.x + (6*scale.x), position.y +(14*scale.y),
				position.x + (9*scale.x), position.y +(10*scale.y),
				position.x + (6*scale.x), position.y +(3*scale.y),
				position.x + (2*scale.x), position.y +(10*scale.y),});
		
		polyBounds.setOrigin(position.x + width/2, position.y + height/2);
		polyBounds.setRotation(rotation);
	}
}
