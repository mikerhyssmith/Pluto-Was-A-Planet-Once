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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class GameModeSelect implements Screen
{
        private PlutoWasAPlanetOnce game;
        private Stage stage;
        private BitmapFont white,whiteSmall;
        private BitmapFont black;
        private TextureAtlas atlas;
        private Skin skin;
        private SpriteBatch batch;
        private TextButton normalButton,speedButton,nightmareButton,start;
        private Label label;
        private Table gameModeContainer,descriptionContainer;
        private TextButtonStyle style;
        private String description[];
        private int gameModeID,outWidth,outHeight;
        private BackgroundHandler background;
        
        public GameModeSelect(PlutoWasAPlanetOnce game) 
        {
                this.game = game;
                
                description = new String[3];
                description[0] = "Defend Pluto from the incoming alien forces" + "\n" + "for as long as possible !";
                description[1] = "Compete with your friends to set the fastest time" + "\n"+ "to Defend pluto for 10 waves of enemies";
                description[2] = "The enemies have come with increased powers and abilities" + "\n" + "hold out for as long as possible !";
        }

        @Override
        public void render(float delta) 
        {
                Gdx.gl.glClearColor(0,0,0,1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
                stage.act(delta);
                batch.begin();
                background.draw(batch, 1);
                stage.draw();
                batch.end();
        }

        @Override
        public void resize(int width, int height) 
        {        
        	if(stage == null)                   
        	{
        		outWidth = width;
        		outHeight = height;
        		stage = new Stage(); // bool scale to screen
        		stage.clear();
        		Gdx.input.setInputProcessor(stage);     
        	
                background = new BackgroundHandler(30, new Vector2(width,height), game.getAssetManager().get("data/world/star.png",Texture.class), false, true);
                stage.addActor(background);
        	}     
        	
        	
        	
        	LabelStyle ls = new LabelStyle(white, Color.WHITE);
        	label = new Label("Choose your game mode", ls);
        	label.setX(0);
        	label.setY(height - height/8);
        	label.setWidth(width);
        	label.setAlignment(Align.center);
        	stage.addActor(label);
        	gameModeContainer = new Table();
        	descriptionContainer = new Table();
        	gameModeContainer.setFillParent(false);
                                   
        	normalButton = new TextButton("Classic",style);            
        	speedButton = new TextButton("Time Trial",style);
        	nightmareButton = new TextButton("Nightmare",style);           
        	gameModeContainer = new Table();
        	gameModeContainer.add(normalButton).space(40);
        	gameModeContainer.add(speedButton).space(40);
        	gameModeContainer.add(nightmareButton).space(40);
        	gameModeContainer.setPosition(width/2 , height/2-height/8);     
        	gameModeContainer.align(Align.center);
        	stage.addActor(gameModeContainer);
                      
        	normalButton.addListener(new InputListener()
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
        			descriptionContainer.reset();
        			descriptionContainer = getDescriptionTable(0);
        			descriptionContainer.setPosition(outWidth/2, (float) (outHeight-outHeight/3));
        			stage.addActor(descriptionContainer);
        			gameModeID = 0;      
        		}
        	});
        	speedButton.addListener(new InputListener()
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
        			descriptionContainer.reset();
        			descriptionContainer = getDescriptionTable(1);
        			descriptionContainer.setPosition(outWidth/2, (float) (outHeight-outHeight/3));
        			stage.addActor(descriptionContainer);
        			gameModeID = 1;
        		}
        	});
        	nightmareButton.addListener(new InputListener()
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
        			descriptionContainer.reset();
        			descriptionContainer = getDescriptionTable(2);
        			descriptionContainer.setPosition(outWidth/2, (float) (outHeight-outHeight/3));
        			stage.addActor(descriptionContainer);
        			gameModeID = 2;            
        		}
        	});
        	

        }

        @Override
        public void show() 
        {
                batch = new SpriteBatch();
                skin = new Skin();
                
                atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
                skin.addRegions(atlas);
                white = new BitmapFont(Gdx.files.internal("data/ui/whiteNew2.fnt"),false);
                black = new BitmapFont(Gdx.files.internal("data/ui/black3.fnt"),false);
                whiteSmall = new BitmapFont(Gdx.files.internal("data/ui/whiteSmall.fnt"),false);
                style = new TextButtonStyle();
                style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
                style.down = skin.getDrawable("buttonpressed"); 
                style.font = black;
                
                
        }
        
        public Table getDescriptionTable(int ID){
                start = new TextButton("Start",style);
                LabelStyle ls = new LabelStyle(whiteSmall, Color.WHITE);
                String tempDescription = description[ID];
                Label description = new Label(tempDescription,ls);
                
                descriptionContainer.add(start).align(Align.center);
                descriptionContainer.row();
                descriptionContainer.add(description);
                start.addListener(new InputListener()
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
                				game.setScreen(new GameScreen(game,gameModeID));                    
                			}                             
                		})));           
                	}
                });
                return descriptionContainer;
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
                batch.dispose();
                stage.dispose();
                
        }

}