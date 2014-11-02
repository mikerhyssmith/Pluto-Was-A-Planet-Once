package com.BeefGames.PlutoWasAPlanetOnce;

import com.BeefGames.PlutoWasAPlanetOnce.Preferences.Preferences;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.SplashScreen;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.AudioHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;

public class PlutoWasAPlanetOnce extends Game {
	
	public static final String VERSION = "0.0.2";
	public static final String LOG = "DefendThePlanet";
	public static final boolean DEBUG = true;
	//all images must be power of 2
	FPSLogger log;
	//Use to load all data used by the game
	private AssetManager assetManager;
	private Preferences preferences = new Preferences();
	private AudioHandler audioHandler;

	public AssetManager getAssetManager()
	{
		return assetManager;
	}
	
	
	@Override
	public void create() {
		log = new FPSLogger();
		assetManager = new AssetManager();
		preferences = new Preferences();
		setScreen(new SplashScreen(this));
		
		
	}
	
	public Preferences getPreferences(){
		
		return preferences;
	}
	public void setAudioHandler(AudioHandler handler){
		
		audioHandler = handler;
	}
	public AudioHandler getAudioHandler(){
		
		return audioHandler;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		log.log();
		
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width,height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
