package com.BeefGames.PlutoWasAPlanetOnce.Transitions;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.MainMenu;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.UpgradesScreen2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ScreenTransition {
	
	private Stage upgradeStage,mainStage;

	public ScreenTransition() {
		// TODO Auto-generated constructor stub
	}
	
	public void transitionToUpgrade(UpgradesScreen2 screen2,PlutoWasAPlanetOnce game){
		
		upgradeStage = screen2.getStage();
		upgradeStage.addAction(Actions.fadeIn(0.5f));
		
		
		
	}
	public void transitionToGame(MainMenu screen1){
		mainStage = screen1.getStage();
		mainStage.addAction(Actions.fadeOut(0.5f));
		
		
	}

}
