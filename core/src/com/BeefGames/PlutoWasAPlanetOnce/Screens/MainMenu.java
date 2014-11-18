package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MainMenu implements Screen {

	private PlutoWasAPlanetOnce game;
    private Stage stage;
	private BitmapFont white;
	private BitmapFont black;
	private TextureAtlas atlas;
	private Skin skin;
	private SpriteBatch batch;
	private TextButton startButton,optionsButton,creditsButton;

	private Image title1, title2, planet;

	public MainMenu(PlutoWasAPlanetOnce game){
		this.game = game;
		
	}
	
	@Override
	public void render(float delta) {
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
		//Use as its called at startup
		if(stage == null)
		{
			stage = new Stage(); // bool scale to screen
			stage.clear();
			
			Gdx.input.setInputProcessor(stage);
			
			TextureRegion p = new TextureRegion(new Texture(game.getAssetManager().get("data/world/planetfinal.png", Pixmap.class)));
			
			planet = new Image(p);
			planet.setX(Gdx.graphics.getWidth()/2 - (planet.getWidth()/2));
			planet.setY(Gdx.graphics.getHeight()/2 - (planet.getHeight()/2));
			stage.addActor(planet);
			
			title1 = new Image(game.getAssetManager().get("data/mainmenu/mainmenupart1.png", Texture.class));
			title2 = new Image(game.getAssetManager().get("data/mainmenu/mainmenupart2.png", Texture.class));
			title1.setX(Gdx.graphics.getWidth()/2 - (title1.getWidth()/2));
			title1.setY(Gdx.graphics.getHeight() - (title1.getHeight()));
			title2.setX(Gdx.graphics.getWidth()/2 - (title2.getWidth()/2));
			title2.setY(0);
			stage.addActor(title1);
			stage.addActor(title2);
			
			TextButtonStyle style = new TextButtonStyle();
			style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
			style.down = skin.getDrawable("buttonpressed"); 
			
			style.font = black;
			
			startButton = new TextButton("Start",style);
			startButton.setWidth(300);
			startButton.setHeight(80);
			startButton.setX(width/2 - (startButton.getWidth()/2));
			startButton.setY(height/2 + startButton.getHeight());
			
			optionsButton = new TextButton("Options",style);
			optionsButton.setWidth(300);
			optionsButton.setHeight(80);
			optionsButton.setX(width/2 - (optionsButton.getWidth()/2));
			optionsButton.setY(height/2 - 5 );
			
			creditsButton = new TextButton("Tutorial",style);
			creditsButton.setWidth(300);
			creditsButton.setHeight(80);
			creditsButton.setX(width/2 - (creditsButton.getWidth()/2));
			creditsButton.setY(height/2 - (creditsButton.getHeight()) - 10);
			
			stage.addActor(startButton);
			stage.addActor(optionsButton);
			stage.addActor(creditsButton);
			
			startButton.addListener(new InputListener()
			{
				public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
				{
					System.out.println("down");
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button)
				{
					game.getAudioHandler().buttonClick();
            		System.out.println("Up");
            		stage.addAction(Actions.sequence(Actions.fadeOut(1),Actions.run(new Runnable()
            		{     
            			@Override        
            			public void run() 
            			{        
            				game.setScreen(new GameScreen(game));                    
            			}                             
            		}))); 
				}
			});
			
			creditsButton.addListener(new InputListener()
			{
				
				public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
				{
					System.out.println("down");
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button)
				{
					System.out.println("Up");
					game.getAudioHandler().buttonClick();
					game.setScreen(new Tutorial(game));
				}
	
			});
			
			optionsButton.addListener(new InputListener()
			{
				public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
				{
					System.out.println("down");
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button)
				{
					System.out.println("Up");
					game.getAudioHandler().buttonClick();
					game.setScreen(new OptionsScreen(game));
				}
			});
		}
	}	
	@Override
	public void show() {
	//show is called first then resize - so called again
		batch = new SpriteBatch();
		skin = new Skin();
		
		atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
		
		skin.addRegions(atlas);
		
		white = new BitmapFont(Gdx.files.internal("data/ui/whiteNew2.fnt"),false);
		black = new BitmapFont(Gdx.files.internal("data/ui/black3.fnt"),false);
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume()
	{
		
	}

	@Override
	public void dispose() {
		batch.dispose(); //dispose everything
		skin.dispose();
		atlas.dispose();
		white.dispose();
		black.dispose();
		stage.dispose();
	}
	
	public Stage getStage(){
		
		return stage;
	}

}
