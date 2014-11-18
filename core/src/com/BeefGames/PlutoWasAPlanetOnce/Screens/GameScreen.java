package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import aurelienribon.tweenengine.TweenManager;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.View.GameHUD;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;


public class GameScreen implements Screen {
	
	//Create variables
	private PlutoWasAPlanetOnce game;
	World world;
	GameHUD hud;
	private long actionBeginTime;
	
	private boolean gameLost,gameLostTimer;
	
	/**
	 * Returns whether the game has been lost or not
	 * @return 
	 */
	public boolean getStatus()
	{
		return gameLost;
	}
	/**
	 * Sets whether the game has been lost or not
	 * @param status
	 */
	public void setStatus(boolean status,boolean planet)
	{
		if(!planet){
		this.gameLost = status;
		}
		else{
			
			gameLostTimer = true;
			actionBeginTime = TimeUtils.nanoTime();
		}
	}
	
	//Constructor
	public GameScreen(PlutoWasAPlanetOnce game ){
		
		this.game = game;
		world = new World(game,this);
		hud = new GameHUD(world,game,this);
		this.gameLost = false;
		System.out.println(world.getShip().getHealth());
	}

	//Render method
	@Override
	public void render(float delta) {
		
		if(gameLostTimer){
			
			
	    	long elapsedTime = ((actionBeginTime - TimeUtils.nanoTime()) / 1000000000);

	    	if(elapsedTime < -2)
	    	{
	    		System.out.println(elapsedTime);
	    		gameLost = true;
	    	}
		}
		
		if(!hud.getPause())
		{
			hud.update(delta);
        
			if(!gameLost)
			{
				world.update();
			}
		}
		
        world.render();
        hud.render(delta);
	}

	@Override
	public void resize(int width, int height) {	
	}

	@Override
	public void show() 
	{
		hud.updateNukes();
	}
	

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() 
	{
		hud.setPause(true);
	}

	@Override
	public void resume() {
		
		hud.setPause(false);
	}

	@Override
	public void dispose() {
		
		world.dispose();
		hud.dispose();
		
	}

}
