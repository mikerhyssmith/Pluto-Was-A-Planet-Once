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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class EndGameScreen implements Screen{

	
	private PlutoWasAPlanetOnce game;
	private Stage stage;
	private int kills,moneySpent;
	private String time;
	private SpriteBatch batch;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont white,black;
	private LabelStyle ls;
	private Label label,stats,killsLabel,moneyLabel,timeLabel,wavesLabel,scoreLabel;
	private TextButtonStyle style;
	private TextButton replay,leaderBoards;
	private Table buttons,statsContainer;
	private int gameModeID,wavesSurvived,score;
	private BackgroundHandler background;
	private Screen gs;
	
	
	public EndGameScreen(Screen gs,PlutoWasAPlanetOnce game,int kills,int wavesSurvived, int moneySpent,String time,int gameModeID,int score) 
	{
		this.score = score;
		this.game = game;
		this.kills = kills;
		this.moneySpent = moneySpent;
		this.time = time;
		this.gameModeID = gameModeID;
		this.wavesSurvived = wavesSurvived;
		this.gs = gs;
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

			Gdx.input.setInputProcessor(stage);
			ls = new LabelStyle(white,Color.WHITE);
			buttons = new Table();
			statsContainer = new Table();
			
            background = new BackgroundHandler(30, new Vector2(width,height), game.getAssetManager().get("data/world/star.png",Texture.class), false, true);
            stage.addActor(background);
			
			label = new Label("Game Over !", ls);
			label.setX(width/2 - label.getWidth()/2);
			label.setY(height - height/8);
			label.setAlignment(Align.center);
			stage.addActor(label);
		
			style = new TextButtonStyle();
			style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
			style.down = skin.getDrawable("buttonpressed"); 
			style.font = black;
		
			replay = new TextButton("Play Again",style);
			leaderBoards = new TextButton("Leaderboards",style);
		
			stats = new Label("Stats",ls);
			killsLabel = new Label("Kills : "+ kills,ls);
			moneyLabel = new Label("Money Spent : " + moneySpent,ls);
			timeLabel = new Label("Time Taken : " + time,ls);
			wavesLabel = new Label("Waves Survived : " + wavesSurvived,ls);
			scoreLabel = new Label("Final Score : " + score,ls);
		
			buttons.add(replay).row().spaceTop(10);
		
			buttons.add(leaderBoards).row().spaceBottom(10);
		
			statsContainer.add(stats).row();
			statsContainer.add(scoreLabel).row();
			statsContainer.add(killsLabel).row();
			statsContainer.add(moneyLabel).row();
			statsContainer.add(timeLabel).row();
			statsContainer.add(wavesLabel).row();
		

			buttons.setPosition(width - width/4, height/2);
			statsContainer.setPosition(width/4,height /2);
		
			stage.addActor(buttons);
			stage.addActor(statsContainer);
			

		}

		replay.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
			{
				System.out.println("down");
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				System.out.println("Up");
				game.getScreen().dispose();
				game.getAudioHandler().stopMusic();
				game.setScreen(new SplashScreen(game));
				gs.dispose();
			}

		});
		
		leaderBoards.addListener(new InputListener()
		{
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
			{
				System.out.println("down");
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button)
			{
				
				System.out.println("Up");
			}
	
		});
	
	}


	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin();
		
		atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
		skin.addRegions(atlas);
		white = new BitmapFont(Gdx.files.internal("data/ui/whiteNew2.fnt"),false);
		black = new BitmapFont(Gdx.files.internal("data/ui/black3.fnt"),false);
		black.setScale(0.75f, 0.75f);
		white.setScale(0.90f,0.90f);
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
