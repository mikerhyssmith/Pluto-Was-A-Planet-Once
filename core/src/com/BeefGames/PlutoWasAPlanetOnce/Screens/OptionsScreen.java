package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BackgroundHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsScreen implements Screen {

	
	private PlutoWasAPlanetOnce game;
	private Stage stage;
	private BitmapFont white;
	private BitmapFont black;
	private TextureAtlas atlas;
	private TextButtonStyle style;
	private Skin skin,uiSkin;
	private SpriteBatch batch;
	private Label titleLabel,soundVolumeLabel,musicVolumeLabel;
	private Table container;
	private TextButton mainMenuButton;
	private LabelStyle ls;
	private BackgroundHandler background;
	
	public OptionsScreen (PlutoWasAPlanetOnce game)
	{
		this.game = game;
	}
	
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) 
	{
		if(stage == null)
		{
			stage = new Stage(); // bool scale to screen
			stage.clear();
			
			style = new TextButtonStyle();
			style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
			style.down = skin.getDrawable("buttonpressed"); 
			style.font = black;
			
			ls = new LabelStyle(white, Color.WHITE);
			
            background = new BackgroundHandler(30, new Vector2(width,height), game.getAssetManager().get("data/world/star.png",Texture.class), false, true);
            stage.addActor(background);
		}
		Gdx.input.setInputProcessor(stage);
		titleLabel = new Label("Options", ls);
		titleLabel.setX(0);
		titleLabel.setY(Gdx.graphics.getHeight() / 2 + 200);
		titleLabel.setWidth(width);
		titleLabel.setAlignment(Align.center);
		stage.addActor(titleLabel);
		soundVolumeLabel = new Label("Sounds Volume: ",uiSkin);
		musicVolumeLabel = new Label("Music Volume",uiSkin);
		container = new Table();
		container.setFillParent(false);
		container.setPosition(width/2 , height/2);
		container.align(Align.center);
		mainMenuButton = new TextButton("Back to Main Menu",style);
		mainMenuButton.setX(width/10);
		mainMenuButton.setY(Gdx.graphics.getHeight() / 2 + 200);
		stage.addActor(mainMenuButton);
		mainMenuButton.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
			{
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				System.out.println("Up");
				game.getAudioHandler().buttonClick();
				game.setScreen(new MainMenu(game));
			}
		});
			
		final CheckBox soundEffectsCheckbox = new CheckBox("Play Sounds",uiSkin);
		
		soundEffectsCheckbox.setChecked(game.getPreferences().getSound());
	    
		soundEffectsCheckbox.addListener( new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				boolean enabled = soundEffectsCheckbox.isChecked();
				game.getPreferences().setSound( enabled );
			}
		} );
	       
		final CheckBox musicCheckbox = new CheckBox("Play Music",uiSkin);
		musicCheckbox.setChecked(game.getPreferences().getMusic());
		musicCheckbox.addListener( new ClickListener() 
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				boolean enabled = musicCheckbox.isChecked();
				game.getPreferences().setMusic( enabled );
			}
		} );
		final Slider volumeSlider = new Slider( 0f, 1f, 0.1f, false, uiSkin );
		volumeSlider.setValue( game.getPreferences().getSoundVolume() );
		volumeSlider.addListener( new ChangeListener() 
		{
			@Override
			public void changed(ChangeEvent event,Actor actor)
	            {
	                game.getPreferences().setSoundVolume(volumeSlider.getValue() );
	            }
		} );
	       
		final Slider musicSlider = new Slider( 0f, 1f, 0.1f, false, uiSkin );
		musicSlider.setValue( game.getPreferences().getMusicVolume() );
		musicSlider.addListener( new ChangeListener() 
		{
			@Override
			public void changed(ChangeEvent event,Actor actor)
			{
				game.getPreferences().setMusicVolume(musicSlider.getValue() );
			}
		} );
		container.add(soundEffectsCheckbox).colspan(2).space(10);
		container.row();
		container.add(musicCheckbox).colspan(2).space(10);
		container.row();
		container.add(soundVolumeLabel).align(Align.center);
		container.add(volumeSlider).space(10);
		container.row();
		container.add(musicVolumeLabel).align(Align.center);
		container.add(musicSlider).space(10);
		container.setVisible(true);
		stage.addActor(container);
	}
	

	@Override
	public void show() 
	{
		batch = new SpriteBatch();
		skin = new Skin();
		
		atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
		skin.addRegions(atlas);
		white = new BitmapFont(Gdx.files.internal("data/ui/whiteNew2.fnt"),false);
		
		black = new BitmapFont(Gdx.files.internal("data/ui/whiteSmall.fnt"), false);
		uiSkin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
	}

	@Override
	public void hide() 
	{
		dispose();
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
		batch.dispose();
	}

}
