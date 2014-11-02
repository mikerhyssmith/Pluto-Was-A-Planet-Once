package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Preferences.Preferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioHandler {
	private Sound buttonClick,shipFire,turretFire,tokenPickup,bulletHit,enemyDestroy,planetHit;
	private Music textureMusic;
	private float volume;
	private Preferences preferences;
	private AssetManager assetManager;
	private boolean music;
	
	public AudioHandler(AssetManager assetManager,PlutoWasAPlanetOnce game){
		
		this.assetManager = assetManager;
		volume = 1f;
		preferences = game.getPreferences();
		music = false;
	}
	
	public void load(){
		shipFire = assetManager.get("data/laser.mp3");
		turretFire = assetManager.get("data/turretFire.mp3");
		textureMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Texture.mp3"));
		textureMusic.setLooping(true);
		tokenPickup = assetManager.get("data/pickup.mp3");
		bulletHit = assetManager.get("data/bullethit.mp3");
		enemyDestroy = assetManager.get("data/Hit.mp3");
		planetHit = assetManager.get("data/planetHit.mp3");
		buttonClick = assetManager.get("data/menuSelectionClick.mp3");
	}
	
	public void shipFire(){
		if(preferences.getSound()){
		shipFire.play(preferences.getSoundVolume());
		}
	}
	
	public void enemyHit(){
		
		if(preferences.getSound()){
		bulletHit.play(preferences.getSoundVolume());
		}
	}
	public void enemyDestroy(){
		if(preferences.getSound()){
		enemyDestroy.play(preferences.getSoundVolume());
		
		}
		}
	public void turretFire(){
		if(preferences.getSound()){
		
		turretFire.play(preferences.getSoundVolume());
		
		}
	}
	public void buttonClick(){
		
		if(preferences.getSound()){
			buttonClick.play();
			
		}
	}
	public void pickupToken(){
		
		if(preferences.getSound()){
		tokenPickup.play(preferences.getSoundVolume());
		}
	}
	public void planetHit(){
		if(preferences.getSound()){
		planetHit.play(preferences.getSoundVolume());
		
		}
	}
	public void startMusic(){
		System.out.println("StartMusic" + textureMusic.isPlaying());
	
		if(preferences.getMusic()){
			if(!music){
					textureMusic.play();
					System.out.println(textureMusic.isPlaying());
					textureMusic.setVolume(preferences.getMusicVolume());
					music = true;
			}
		System.out.println(textureMusic.isPlaying());
		}
	}
	public void stopMusic(){
		
		textureMusic.stop();
		music = false;
	}
}
