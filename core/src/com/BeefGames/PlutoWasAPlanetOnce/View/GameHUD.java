package com.BeefGames.PlutoWasAPlanetOnce.View;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.EndGameScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.GameScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.UpgradesScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.Nuke;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.InputHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.MinimapHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.TimeHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameHUD {
	private PlutoWasAPlanetOnce game;
	private float width, height;
	private World gameWorld;
	private Ship ship;
	private WorldRenderer worldRenderer;
	private boolean lost;
	
	private Stage hudStage;
	private TextureAtlas hudAtlas;
	
	//Variables for the health bar
	private float shipHealth, maxHealth, hbarLength;
	private Image healthBar;
	private TextureRegion hbarPart;

	//Variables for the armour bar
	private float shipArmour, maxArmour, abarLength;
	private Image armourBar;
	private TextureRegion abarPart;
	
	//Variables for the charge bar
	private float shipCooldown, currentCooldown, wbarLength;
	private Image weaponBar;
	private TextureRegion wbarPart;
	
	//Shooting
	private Boolean shoot = false;
	
	//Variables for text on HUD
	private BitmapFont hudFont;
	private Label turretAmmo,time, gameLost, endofWave, killsLabel, moneyLabel, timeLabel, cooldownLabel;


	private InputHandler inputHandler;
	
	private int kills;
	private long actionBeginTime;
	private boolean timeSet;
	private int moneyBefore;
	private int moneyAfter;

	//Input control
	private InputMultiplexer multiInput;
	private MinimapHandler minimap;
	
	//Pausing variables
	private Window pause;
	private PauseMenu pauseMenu;
	private boolean paused;
	private Image pauseButton;

	//Nuke icon variables
	private Image nukeButton;
	private TimeHandler timeHandler;
	
	private boolean pauseCreated = false;
	
	public GameHUD(World world, PlutoWasAPlanetOnce game, GameScreen gamescreen)
	{
		//Get all the info required from world
		this.gameWorld = world;
		this.game = game;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		timeHandler = gameWorld.getTimeHandler();
		paused = false;
		worldRenderer = gameWorld.getRenderer();
		hudAtlas = game.getAssetManager().get("data/hud/HUDPack.pack", TextureAtlas.class);
		
		ship = gameWorld.getShip();
		shipHealth = ship.getHealth();
		maxHealth = ship.getMaxHealth();
		shipArmour = ship.getArmour();
		maxArmour = ship.getMaxArmour();
		shipCooldown = ship.getCooldown();
		currentCooldown = shipCooldown;
		
		//Set up the HUD font
		hudFont = new BitmapFont(Gdx.files.internal("data/ui/whiteSmall.fnt"),false);
		
		//Set up the stage
		
		hudStage = new Stage(); // bool scale to screen
		hudStage.clear();
		
		//Create + set position for health bar
		hbarPart = hudAtlas.findRegion("healthBar");
		hbarLength  = hbarPart.getRegionWidth();
		healthBar = new Image( new TextureRegionDrawable(hbarPart));	
		healthBar.setPosition(width/2 - hbarLength/2, height - (2.5f * hbarPart.getRegionHeight()));
		hudStage.addActor(healthBar);
		
		System.out.println("length: " + hbarLength);
		
		//Create + set position for armour bar
		abarPart = hudAtlas.findRegion("armourBar");
		abarLength = abarPart.getRegionWidth();
		armourBar = new Image(new TextureRegionDrawable(abarPart));	
		armourBar.setPosition(healthBar.getX(), healthBar.getY());
		hudStage.addActor(armourBar);	
		
		//Create labels
		LabelStyle ls = new LabelStyle(hudFont, Color.WHITE);
		
		gameLost = new Label("You lose!", ls);
		gameLost.setX(Gdx.graphics.getWidth() / 4);
		gameLost.setY(Gdx.graphics.getHeight() / 2 );
		gameLost.setWidth(width);
		gameLost.setAlignment(Align.center);
		gameLost.setVisible(false);
		hudStage.addActor(gameLost);
		
		turretAmmo = new Label("Turret Ammo",ls);
		turretAmmo.setVisible(false);
		hudStage.addActor(turretAmmo);
		
		time = new Label("Time Elapse" + timeHandler.getFormattedTime(timeHandler.getTime()),ls);
		time.setX(width - width/4);
		time.setY(Gdx.graphics.getHeight() - height/10 );
		time.setAlignment(Align.center);
		time.setVisible(false);
		hudStage.addActor(time);
		
		endofWave = new Label("End of Wave",ls);
		endofWave.setX(Gdx.graphics.getWidth()/4);
		endofWave.setY(Gdx.graphics.getHeight()/2);
		endofWave.setWidth(width);
		endofWave.setAlignment(Align.center);
		endofWave.setVisible(false);
		hudStage.addActor(endofWave);
		
		killsLabel = new Label("",ls);
		killsLabel.setX(Gdx.graphics.getWidth()/4);
		killsLabel.setY(Gdx.graphics.getHeight()/2 - 10);
		killsLabel.setWidth(width);
		killsLabel.setAlignment(Align.center);
		killsLabel.setVisible(false);
		hudStage.addActor(killsLabel);
		
		moneyLabel = new Label("",ls);
		moneyLabel.setX(Gdx.graphics.getWidth()/4);
		moneyLabel.setY(Gdx.graphics.getHeight()/2 - killsLabel.getHeight() -30);
		moneyLabel.setWidth(width);
		moneyLabel.setAlignment(Align.center);
		moneyLabel.setVisible(false);
		hudStage.addActor(moneyLabel);
		
		timeLabel = new Label("Time till next Wave:",ls);
		timeLabel.setX(0);
		timeLabel.setY(healthBar.getY() - (healthBar.getHeight() *2));
		timeLabel.setWidth(width);
		timeLabel.setAlignment(Align.center);
		timeLabel.setVisible(false);
		hudStage.addActor(timeLabel);
		 
	
	    inputHandler = new InputHandler(world);

	    SetInput(world.getInputHandler());
	    
	    timeSet = false;
	    lost = gameWorld.getStatus();
	
	    //Set up minimap
	    minimap = new MinimapHandler(hudAtlas.findRegion("mapBackground"), hudAtlas.findRegion("mapBorder"),
	    		hudAtlas.findRegion("playerDot"), hudAtlas.findRegion("enemyDot"),hudAtlas.findRegion("tokenDot"),
	    		hudAtlas.findRegion("planetDot"), gameWorld.getEnemies(), gameWorld.getShip(), gameWorld.getTokens(),
	    		gameWorld.getPlanet(),
	    		new Vector2(hudAtlas.findRegion("mapBackground").getRegionWidth()/4 ,
	    				height - (1.25f * hudAtlas.findRegion("mapBackground").getRegionHeight())),
	    				world.getWorldSize());	    
	    hudStage.addActor(minimap);   
	    
		//Create + set position for cooldown bar
		wbarPart = hudAtlas.findRegion("weaponBar");
		wbarLength = wbarPart.getRegionWidth();
		weaponBar = new Image(new TextureRegionDrawable(wbarPart));	
		weaponBar.setPosition(minimap.getX() + (0.5f*wbarLength), minimap.getY() - (6*wbarPart.getRegionHeight()));
		hudStage.addActor(weaponBar);	
		cooldownLabel = new Label("Charge:",ls);
		cooldownLabel.setPosition((1.5f *wbarLength) -width/2, weaponBar.getY() +(2* weaponBar.getHeight()));
		cooldownLabel.setWidth(width);
		cooldownLabel.setAlignment(Align.center);
		hudStage.addActor(cooldownLabel);
	    
	    //Set up pause button
	    pauseButton = new Image(hudAtlas.findRegion("pause"));
	    pauseButton.setPosition(width-(2* pauseButton.getWidth()), height-(2*pauseButton.getHeight()));
	    hudStage.addActor(pauseButton);
	    pauseButton.addListener(new InputListener()
	    {
	    	public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
	    	{
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				
				
				paused = true;
				//pause = pauseMenu.getPauseWindow();
				pause.setPosition(hudStage.getWidth()/2-pause.getWidth()/2, hudStage.getHeight()/2-pause.getHeight()/2);
				pause.padTop(16);
				hudStage.addActor(pause);
				pause.setVisible(true);
			}
	    });

	    
	    //Set up nuke button
	    nukeButton = new Image(new TextureRegionDrawable(hudAtlas.findRegion("nuke#0")));
	    updateNukes();
	    nukeButton.setPosition(width -(2* nukeButton.getWidth()), height/2);
	    hudStage.addActor(nukeButton);
	    nukeButton.addListener(new InputListener()
	    {
	    	public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
	    	{ return true; }
	    	
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				Array<Nuke> nukeArray = gameWorld.getInputHandler().getNukes();
				
				if(nukeArray.size > 0)
				{
					Nuke n = nukeArray.first();
					n.detonate(ship);
					System.out.println("NUKE");
					gameWorld.getParticleHandler().addNuke(ship);
					nukeArray.removeIndex(0);
					updateNukes();
				}
			}
	    });
	    
		turretAmmo.setPosition(width -(1.5f* turretAmmo.getWidth()), height/2 + nukeButton.getHeight() + (2*turretAmmo.getHeight()));

	}
	
	/**
	 * Update the HUD
	 */
	public void update(float delta)
	{
		if(!paused && pause != null){
			
			pause.setVisible(false);
		}
		
		timeLabel.setVisible(false);
		
		if(gameWorld.getGameMode() == 1){
		time.setVisible(true);
		time.setText("Elapsed Time: " + timeHandler.getFormattedTime(timeHandler.getTime()));
		}
		//Print losing text when the game ends
	    lost = gameWorld.getStatus();
	    if(lost && gameLost.isVisible() == false ){

	    	//game.getScreen().dispose();
	    	if(gameWorld.getGameMode() ==1){
		    	game.setScreen(new EndGameScreen(game.getScreen(),game,gameWorld.getTotalKills(),gameWorld.getLevel(),gameWorld.getMoneySpent(),timeHandler.getFormattedTime(timeHandler.getTime()),gameWorld.getLevel()));

	    		
	    	}else{
	    	game.setScreen(new EndGameScreen(game.getScreen(),game,gameWorld.getTotalKills(),gameWorld.getLevel(),gameWorld.getMoneySpent(),timeHandler.getFormattedTime(timeHandler.getTime()),calculateScore()));
	    	}
	    	}
	    	
	    
	    //Get the ships health and armour
	    shipHealth = ship.getHealth();
	    maxHealth = ship.getMaxHealth();
	    shipArmour = ship.getArmour();
	    maxArmour = ship.getMaxArmour();
	    shipCooldown = ship.getCooldown();
	    
	    //Update the size of the health bar
	    float newHbWidth = (shipHealth/maxHealth) * hbarLength;
	    //hbarPart.setRegionWidth((int)newHbWidth);
	    healthBar.setWidth(newHbWidth);   
	    healthBar.invalidate();
	    
	    //Update the size of the armour bar
	    float newAbWidth = (shipArmour/maxArmour) * abarLength;
	    //abarPart.setRegionWidth((int)newAbWidth);
	    armourBar.setWidth(newAbWidth);
	    armourBar.invalidate();  
	    
	    //Update the size of the cooldown bar
	    float newWbWidth = (currentCooldown/shipCooldown) * wbarLength;
	    wbarPart.setRegionWidth((int)newWbWidth);
	    weaponBar.setWidth(newWbWidth);
	    weaponBar.invalidate();  
	    
	    //Print stats at the end of the wave
	    if(gameWorld.getWaveStatus() == 1)
	    {
	    	setActionBeginTime();
	    	moneyAfter = ship.getMoney();
	    	moneyBefore = gameWorld.getMoneyBefore();
	    	timeSet = true;
	    	kills = gameWorld.getKills();
	    	endofWave.setText("End of Wave: " + gameWorld.getLevel());
	    	killsLabel.setText("Enemies Killed: " + kills);
	    	moneyLabel.setText("Money Earned " + (moneyAfter - moneyBefore));
	    	killsLabel.setVisible(true);
	    	endofWave.setVisible(true);
	    	moneyLabel.setVisible(true);

	    	long elapsedTime = ((actionBeginTime - TimeUtils.nanoTime()) / 1000000000);
	    	
	    	if(elapsedTime < -2)
	    	{
	    		timeSet = false;
	    		killsLabel.setVisible(false);
	    		endofWave.setVisible(false);
	    		moneyLabel.setVisible(false);
	    		
	    		ship.setForce(new Vector2(0,0));
				ship.setMomentum(new Vector2(0,0) );
	    		ship.setVelocity(new Vector2(0, 0));

	    		hudStage.cancelTouchFocus();
	    		
	    		gameWorld.removeAllBullets();
	    		
	    		game.setScreen(new UpgradesScreen(game,game.getScreen(),gameWorld));
	    	}
	    }
	    
	    if(gameWorld.checkDelay())
	    {
	    	timeLabel.setText("Time till next Wave: " + (gameWorld.getElapsedTime()+5));
	    	timeLabel.setVisible(true);
	    }
	    else{
	    	timeLabel.setVisible(false);
	    }
	    
	    if(gameWorld.getTurretActive()){
	    	
	    	turretAmmo.invalidate();
	    	turretAmmo.setText("Turret Ammo: "+ gameWorld.getTurret().getAmmo());
	    	turretAmmo.setVisible(true);
	    }
	    else {
	    	turretAmmo.setVisible(false);
	    }
		
	   //Check for shooting
	    inputHandler.move();
	    if(currentCooldown >= shipCooldown)
	    {
	    	currentCooldown = shipCooldown;
	    	if(shoot)
	    	{
		    	currentCooldown = 0f;
		    	gameWorld.getInputHandler().getBulletHandler().Fire(ship.getRotation());
	    	}
	    }
	    if(currentCooldown < shipCooldown)
	    {
	    	float temp = currentCooldown + Gdx.graphics.getDeltaTime();
	    	
	    	if(temp >= shipCooldown)
	    	{
	    		currentCooldown = shipCooldown;
	    	}
	    	else
	    	{
	    		currentCooldown = temp;
	    	}
	    }
	    
		//Update stage
		hudStage.act(delta);
		//System.out.println("health length: " + hbarPart.getRegionWidth() + " armour length: " +abarPart.getRegionWidth() );
	}

	public void render(float delta)
	{
		
		if(!pauseCreated){
			pauseMenu = new PauseMenu(width,height,this);
		
			pause = pauseMenu.getPauseWindow();
			pauseCreated = true;
		}
		
		//Draw the HUD
		worldRenderer.getSpriteBatch().begin();
	    minimap.update(gameWorld.getEnemies(), gameWorld.getShip(), gameWorld.getTokens());
	    
		hudStage.draw();
		worldRenderer.getSpriteBatch().end();
	}
	
	/**
	 * Updates the nuke icon on the HUD
	 */
	public void updateNukes()
	{
		int nukes = gameWorld.getInputHandler().getNukes().size;
		switch(nukes){
		case 0: nukeButton.setDrawable(new TextureRegionDrawable(hudAtlas.findRegion("nuke#0")));
		nukeButton.invalidate();
		break;
		case 1: nukeButton.setDrawable(new TextureRegionDrawable(hudAtlas.findRegion("nuke#1")));
		nukeButton.invalidate();
		break;
		case 2: nukeButton.setDrawable(new TextureRegionDrawable(hudAtlas.findRegion("nuke#2")));
		nukeButton.invalidate();
		break;
		case 3: nukeButton.setDrawable(new TextureRegionDrawable(hudAtlas.findRegion("nuke#3")));
		nukeButton.invalidate();
		break;
		default: System.out.println("ERROR-more than max number of nukes");
		
		}
		
	}
	
	public void dispose()
	{
		
		hudStage.dispose();
		hudFont.dispose();
	}
	
	//Getters and setters
	public World getWorld()
	{
		return gameWorld;
	}	
	public void setPause(boolean p){
		
		paused = p;
		
	}
	public boolean getPause(){
		return paused;
	}
	public void setWorld(World world)
	{
		this.gameWorld = world;
	}
	public void SetInput(InputProcessor input)
	{
	    multiInput = new InputMultiplexer(hudStage);
	    multiInput.addProcessor(input);
	    Gdx.input.setInputProcessor(multiInput);
	}
	public PlutoWasAPlanetOnce getGame(){
		
		return game;
	}
	
	public int calculateScore(){
		int kills = gameWorld.getTotalKills();
		int level = gameWorld.getLevel();
		int turretKills = gameWorld.getTurretKills();
		int eliteKills = gameWorld.getEliteKills();
		int upgradeNumber = gameWorld.getUpgrade();
		
		int score = (kills*2) + (level+3) + turretKills + (eliteKills*4) + upgradeNumber;
		
		return score;
		
	}
	public void setActionBeginTime(){
		
		if(timeSet == false){
			actionBeginTime = TimeUtils.nanoTime();
		}
	}
}
