package com.BeefGames.PlutoWasAPlanetOnce.Tokens;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class MoneyToken extends Token
{
	public MoneyToken(Vector2 position, float width, float height,Ship ship,float rotation, Vector2 velocity) {
		super(position, width, height,ship,rotation, velocity);
	
		ID = 1;
	}

	@Override
	public void update()
	{
		super.update();
		
		bounds.x = position.x;
		bounds.y = position.y; 
		
		if(ship.getBounds().overlaps(bounds) && checkCollect == false)
		{
			int shipmoney = ship.getMoney();
			int tempmoney = shipmoney += 5;
			ship.setMoney(tempmoney);
			checkCollect = true;
		}
		
		rotation += Gdx.graphics.getDeltaTime() * ROTATION_SPEED; //adds the rotation every frame frame independent (delta time) spin no matter how many fps
		if(rotation > 360)
		{
			rotation -= 360;
		}
	}
}
