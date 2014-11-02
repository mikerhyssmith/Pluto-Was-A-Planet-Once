package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;

public class AccellerometerHandler {
	private Ship ship;
	
	
	public AccellerometerHandler(World world)
	{
		ship = world.getShip();
	}
	
	
	public boolean checkAccellerometer()
	{
		if(Gdx.input.isPeripheralAvailable( Peripheral.Accelerometer ))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void moveShip(float delta)
	{
		float adjustedX = ( Gdx.input.getAccelerometerX()  );
		if( adjustedX < - 2f ) adjustedX = - 2f; else if( adjustedX > 2f ) adjustedX = 2f;
		 
		
		float adjustedY = Gdx.input.getAccelerometerY();
		if( adjustedY < - 2f ) adjustedY = - 2f; else if( adjustedY > 2f ) adjustedY = 2f;
		 
		adjustedX /= 2;
		adjustedY /= 2;
		 
		ship.getVelocity().x += ( adjustedY * 0.5 * delta );
	}
}
