package com.BeefGames.PlutoWasAPlanetOnce.Preferences;

public class Preferences {

	private Boolean sound,music, particles;
	private float soundVolume,musicVolume;
	
	public Preferences(){
		
		sound = true;
		music = true;
		particles = true;
		soundVolume = 1f;
		musicVolume = 1f;
		
	}
	
	public boolean getSound(){
		
		return sound;
	}
	public boolean getMusic(){
		
		return music;

	}
	public boolean getParticles(){
		return particles;
	}
	
	public float getSoundVolume(){
		return soundVolume;
	}
	public float getMusicVolume(){
		
		return musicVolume;
	}
	
	public void setSound(boolean sbool){
		sound = sbool;
	}
	public void setMusic(boolean mbool){
		
		music = mbool;

	}
	
	public void setParticles(boolean pbool){
		particles = pbool;
	}
	
	public void setSoundVolume(float volume){
		if(volume <= 1f && volume>= 0f){
			
			soundVolume = volume;
		}
	}
	public void setMusicVolume(float volume){
		if(volume <= 1f && volume>= 0f){
			
			musicVolume = volume;
		}
	}
}
