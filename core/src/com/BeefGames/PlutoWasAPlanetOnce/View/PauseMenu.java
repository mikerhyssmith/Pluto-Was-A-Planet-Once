package com.BeefGames.PlutoWasAPlanetOnce.View;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.SplashScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenu {
	
	private Skin uiSkin,skin;
	private TextureAtlas atlas;
	private Window pause;
	private TextButton resume,mainmenu,quit;
	private BitmapFont black;
	private TextButtonStyle style;
	private GameHUD gamehud;
	private PlutoWasAPlanetOnce game;

	public PauseMenu(float width,float height,GameHUD hud) {
		uiSkin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
		pause = new Window("Pause",uiSkin);
		gamehud = hud;
		skin = new Skin();
		this.game = hud.getGame();
		atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
		skin.addRegions(atlas);
		black = new BitmapFont(Gdx.files.internal("data/font/black.fnt"), false);
		style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
		style.down = skin.getDrawable("buttonpressed"); 
		style.font = black;
		
		resume = new TextButton("Resume",style);
		mainmenu = new TextButton("Main Menu",style);
		quit = new TextButton("Quit Game",style);
		
		final CheckBox musicCheckbox = new CheckBox("Play Music",uiSkin);
		musicCheckbox.setChecked(game.getPreferences().getMusic());
        musicCheckbox.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusic( enabled );
                if(!enabled){
                	
                	gamehud.getWorld().getAudioHandler().stopMusic();
                }
                else{
                	gamehud.getWorld().getAudioHandler().startMusic();
                }
            }
        } );
        final CheckBox soundEffectsCheckbox = new CheckBox("Play Sounds",uiSkin);
		soundEffectsCheckbox.setChecked(game.getPreferences().getSound());
        soundEffectsCheckbox.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSound( enabled ); 
            }
        } );
        
        
		pause.setSize(width/1.5f, height/1.5f);
		pause.setMovable(false);
		pause.add(musicCheckbox).row().space(10);
		pause.add(soundEffectsCheckbox).row().space(10);
		pause.add(resume).row().spaceTop(10);
		pause.add(mainmenu).row().space(10);
		pause.add(quit);
		
		quit.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
			{
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				
				Gdx.app.exit();

			}

	});
		
	resume.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				
				gamehud.setPause(false);

			}

	});
	
	mainmenu.addListener(new InputListener(){
		
		public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
			
			
			return true;
		}
		public void touchUp(InputEvent event, float x,float y , int pointer, int button){
			game.getScreen().dispose();
			game.getAudioHandler().stopMusic();
			game.setScreen(new SplashScreen(game));
		}
});
	
	
		
	}
	
	public Window getPauseWindow(){
		
		//newPause.setSkin(uiSkin);
		
		return pause;
	}

}
