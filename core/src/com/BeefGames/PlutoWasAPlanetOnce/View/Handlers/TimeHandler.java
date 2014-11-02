package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import com.badlogic.gdx.utils.TimeUtils;

public class TimeHandler {

	private long startTime;
	public TimeHandler() {

		startTime = TimeUtils.nanoTime();
	}
	
	public long getTime(){
		
		return TimeUtils.nanoTime() - startTime;
	}
	
	public String getFormattedTime(long time){
		
		long currentTime = time;
		

		long seconds = currentTime / 1000000000;
		int minutes = (int) seconds / 60;
		int remainder = (int) seconds%60;
		
		
		
		String timeReturn = ( minutes + ":" + remainder);
		return timeReturn;
		
		
	}
	
	public int getNumberOfSeconds(){
		long currentTime = getTime();
		int seconds = (int) (currentTime / 1000000000);
		return seconds;
		
	}
	
}
