package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.AudioHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen implements Screen 
{
	private Image beefGames;
	private Image steak;
	private Image barBorder;
	private Image loadingBar;
	private TextureRegion barPart;
	
	PlutoWasAPlanetOnce game;
	SpriteBatch batch;
	
	private float percent;
	private float fullBarWidth;
	private float fadeTime;
	private boolean startedLoading;
	
	private Stage loadingStage;
	
	private AudioHandler audioHandler;
	
	public SplashScreen(PlutoWasAPlanetOnce game)
	{
		this.game = game;
		fadeTime = 1f;
		startedLoading = false;
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0.54f, 0.54f, 0.54f, 1); //Set Clear Colour
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clear
		
		//Once images have faded in, start loading things
		if(loadingBar.getActions().size == 0 && !startedLoading)
		{
			LoadResources();
			startedLoading = true;
			loadingBar.setVisible(true);
		}
		
		//Resize the loading bar
		percent = Interpolation.linear.apply(percent, game.getAssetManager().getProgress(), 0.9f);
		int newWidth = (int)(percent * fullBarWidth);
		
	    barPart.setRegion(barPart.getRegionX(), barPart.getRegionY(),newWidth, barPart.getRegionHeight());
	    loadingBar.setWidth(newWidth);
	    loadingBar.invalidate();

	    //When loading is finished, make images fade out
		if(game.getAssetManager().update() 
				&& (loadingBar.getActions().size == 0) 
				&& (barPart.getRegionWidth() == fullBarWidth))
				
		{
			//If they are already faded out, change to main menu
			if(beefGames.getColor().equals(new Color(1,1,1,0)))
			{
				game.setAudioHandler(audioHandler);
				
				game.setScreen(new MainMenu(game));
			}
			audioHandler.load();
			beefGames.addAction(Actions.fadeOut(fadeTime));
			steak.addAction(Actions.fadeOut(fadeTime));
			barBorder.addAction(Actions.fadeOut(fadeTime));
			loadingBar.addAction(Actions.fadeOut(fadeTime));
		}

	    //Update & draw the screen
		loadingStage.act(delta);
		batch.begin();
		loadingStage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) 
	{
		//Re-set the positions of the images
		beefGames.setPosition(((1/2) * beefGames.getHeight()) , height - (3/2 * beefGames.getHeight()));
		
		steak.setPosition(width - ((3/2) *steak.getWidth()) ,height - (3/2 * beefGames.getHeight()) );
		
		barBorder.setPosition( (width - barBorder.getWidth())/2 ,
				(((height/2) - barBorder.getHeight() )/2) );
		
		loadingBar.setPosition( (width - loadingBar.getWidth())/2 , 
				(( (height/2) - loadingBar.getHeight() )/2) );
		
		beefGames.invalidate();
		steak.invalidate();
		barBorder.invalidate();
		loadingBar.invalidate();
	}

	@Override
	public void show()
	{
		batch = new SpriteBatch();
		
        // Tell the manager to load assets for the loading screen
		game.getAssetManager().load("data/loading/loading.atlas", TextureAtlas.class);
        // Wait until they are finished loading
		game.getAssetManager().finishLoading();
		
		loadingStage = new Stage();
		
		//Get resources needed for loading screen
        TextureAtlas atlas = game.getAssetManager().get("data/loading/loading.atlas", TextureAtlas.class);
		
        beefGames = new Image(atlas.findRegion("beefgames"));
        steak = new Image(atlas.findRegion("steak"));
        barBorder= new Image(atlas.findRegion("borderv1"));
		barPart = new TextureRegion(atlas.findRegion("barv1"));
		loadingBar = new Image(barPart);	
		
		fullBarWidth = barPart.getRegionWidth();
        
        //Make images transparent to begin with
        beefGames.setColor(1, 1, 1, 0);
        steak.setColor(1,1,1,0);
        barBorder.setColor(1,1,1,0);
        loadingBar.setColor(1,1,1,0);
        
        //Set images to fade in
        beefGames.addAction(Actions.fadeIn(fadeTime));
        steak.addAction(Actions.fadeIn(fadeTime));
        barBorder.addAction(Actions.fadeIn(fadeTime));
        loadingBar.addAction(Actions.fadeIn(fadeTime));
        
        //Add images to stage
        loadingStage.addActor(beefGames);
        loadingStage.addActor(steak);
        loadingStage.addActor(barBorder);
        loadingStage.addActor(loadingBar);
        
        //Loading bar is invisible to begin with
        loadingBar.setVisible(false);
	}
	
	@Override
	public void hide() 
	{
        game.getAssetManager().unload("data/loading/loading.atlas");
	}

	@Override
	public void pause() 
	{
		
	}

	@Override
	public void resume() 
	{
		
	}

	@Override
	public void dispose() 
	{
	}
	
	/**
	 * All resources needed by the game should be in here
	 */
	private void LoadResources()
	{
		AssetManager manager = game.getAssetManager();
		
		
		
		manager.load("data/ship.png", Texture.class);
		manager.load("data/enemies/Follower.png", Texture.class);
		manager.load("data/enemies/enemy1.png", Texture.class);
		manager.load("data/enemies/enemy2.png", Texture.class);
		manager.load("data/enemies/Sniper.png",Texture.class);
		manager.load("data/Bullet.png", Texture.class);
		manager.load("data/tokens/healthtoken.png", Texture.class);
		manager.load("data/tokens/armourtoken.png", Texture.class);
		manager.load("data/tokens/moneyToken.png", Texture.class);
		manager.load("data/CIRCLE.png", Texture.class);
		manager.load("data/world/planetfinal.png", Pixmap.class);
		manager.load("data/protectionBubble.png",Texture.class);
		manager.load("data/world/turret.png",Texture.class);
		manager.load("data/enemies/Elite.png",Texture.class);
		manager.load("data/world/turretbase.png",Texture.class);
		manager.load("data/upgrades/upgradetitle.png",Texture.class);
		manager.load("data/world/enemybullet.png",Texture.class);
		manager.load("data/world/enemybullet2.png",Texture.class);
		
		manager.load("data/enemies/eliteturret.png",Texture.class);
		
		//HUD
		manager.load("data/hud/HUDPack.pack", TextureAtlas.class);
		//UI
		manager.load("data/ui/button.pack", TextureAtlas.class);
		
		//Sounds
		manager.load("data/laser.mp3",Sound.class);
		manager.load("data/pickup.mp3",Sound.class);
		manager.load("data/turretFire.mp3",Sound.class);
		manager.load("data/Hit.mp3",Sound.class);
		manager.load("data/bullethit.mp3",Sound.class);
		manager.load("data/planetHit.mp3",Sound.class);
		manager.load("data/menuSelectionClick.mp3",Sound.class);
		
		//Upgrade textures -  need packing
		manager.load("data/upgrades/armourIcon.png", Texture.class);
		manager.load("data/upgrades/healthIcon.png", Texture.class);
		manager.load("data/upgrades/nukeIcon.png", Texture.class);
		manager.load("data/upgrades/shieldIcon.png", Texture.class);
		manager.load("data/upgrades/turretIcon.png", Texture.class);
		manager.load("data/upgrades/weaponIcon.png", Texture.class);
		
		  //Load Tutorial images
			manager.load("data/ui/slide1.png",Texture.class);
			manager.load("data/ui/slide2.png",Texture.class);
			manager.load("data/ui/slide3.png",Texture.class);
			manager.load("data/ui/slide4.png",Texture.class);
			manager.load("data/ui/slide5.png",Texture.class);
			manager.load("data/ui/slide6.png",Texture.class);
			manager.load("data/ui/slide7.png",Texture.class);
			 
		
		manager.load("data/world/star.png", Texture.class);
		manager.load("data/mainmenu/mainmenupart1.png", Texture.class);
		manager.load("data/mainmenu/mainmenupart2.png", Texture.class);
		
		audioHandler = new AudioHandler(manager,game);
		
		
	}
}
