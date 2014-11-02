package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;


public class TouchPadHandler 
{
	private Touchpad touchpad;
	private Ship ship;
	
	public TouchPadHandler(Touchpad touchpad,Ship ship)
	{
		this.touchpad = touchpad;
		this.ship = ship;
	}
	
	/**
	 * Move the ship using the touchpad
	 */
	public void move()
	{
		if(Math.hypot(touchpad.getKnobPercentX(), touchpad.getKnobPercentY()) >= 0.3f)
		{
			ship.setMove(true);
		}
		else
		{
			ship.setMove(false);
		}
		
		ship.setForce(new Vector2(touchpad.getKnobPercentX()* ship.getForceMult(),
					touchpad.getKnobPercentY()* ship.getForceMult()));
	}
	/**
	 * Use to try to shoot a bullet
	 * @param handler
	 * @return Returns true if a bullet is fired
	 */
	public Boolean shoot(BulletHandler handler)
	{
		float dist = (float)Math.sqrt( Math.pow(touchpad.getKnobPercentX(), 2)
				+ Math.pow(touchpad.getKnobPercentY(), 2) );

		if(dist > 0.25f)
		{
			//handler.Fire(touchpad.getKnobX() - touchpad.getOriginX() , touchpad.getKnobY() - touchpad.getOriginY());
			
			handler.Fire(ship.getRotation());
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
