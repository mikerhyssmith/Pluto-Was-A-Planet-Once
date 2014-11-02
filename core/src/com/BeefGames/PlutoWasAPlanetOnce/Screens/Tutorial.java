package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Tutorial implements Screen {
	
	private PlutoWasAPlanetOnce game;
	private Stage stage;
	private SpriteBatch batch;
	private Image slide1,slide2,slide3,slide4,slide5,slide6,slide7,currentImage;
	private int currentSlide,outWidth,outHeight;
	private Image slides[];

	public Tutorial(PlutoWasAPlanetOnce game) {
		
		
		slides = new Image[8];
		this.game = game;
		slide1 = new Image(game.getAssetManager().get("data/ui/slide1.png", Texture.class));
		slide2 = new Image(game.getAssetManager().get("data/ui/slide2.png", Texture.class));
		slide3 = new Image(game.getAssetManager().get("data/ui/slide3.png", Texture.class));
		slide4 = new Image(game.getAssetManager().get("data/ui/slide4.png", Texture.class));
		slide5 = new Image(game.getAssetManager().get("data/ui/slide5.png", Texture.class));
		slide6 = new Image(game.getAssetManager().get("data/ui/slide6.png", Texture.class));
		slide7 = new Image(game.getAssetManager().get("data/ui/slide7.png", Texture.class));

		
		slides[0] = slide1;
		slides[1] = slide2;
		slides[2] = slide3;
		slides[3] = slide4;
		slides[4] = slide5;
		slides[5] = slide6;
		slides[6] = slide7;
		
		currentSlide = 0;
		 
	
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
	public void resize(int width, int height) {
		if(stage == null){
			
			outWidth = width;
			outHeight = height;
			stage = new Stage(); // bool scale to screen
			stage.clear();
			
			Gdx.input.setInputProcessor(stage);
			
			currentImage = slide1;
			currentImage.setSize(width, height);
			stage.addActor(currentImage);
			
			
			
		}
		
		 addHandler(currentImage);
		 

		
	}
	
	public void addHandler(Image image){
		
		 image.addListener(new InputListener()
		    {
		    	public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
		    	{
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button)
				{
					Image newImage;
					currentSlide ++;
					
					if(currentSlide <= 6){
					stage.clear();
					
					newImage = slides[currentSlide];
					newImage.setSize(outWidth, outHeight);
					stage.addActor(newImage);
					addAlternateHandler(newImage);
					
					}else{
						
						game.setScreen(new MainMenu(game));
					
					}
				} });
		
		
	}
	
	public void addAlternateHandler(Image image){
		
		 image.addListener(new InputListener()
		    {
		    	public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
		    	{
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button)
				{
					Image newImage;
					currentSlide ++;
					
					stage.clear();
					if(currentSlide <= 6){
						stage.clear();
						
						newImage = slides[currentSlide];
						newImage.setSize(outWidth, outHeight);
						stage.addActor(newImage);
						addHandler(newImage);
						
						}else{
							
							game.setScreen(new MainMenu(game));
						
						}
					} });
					
			
		
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		
	}

}
