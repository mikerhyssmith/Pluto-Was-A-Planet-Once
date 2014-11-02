package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.UpgradeManager;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BackgroundHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class UpgradesScreen2 implements Screen {

	PlutoWasAPlanetOnce game;
	
	Label label;
	BitmapFont black;
	SpriteBatch batch;
	private Stage stage;
	private Table container,detailContainer;
	Skin uiSkin;
	Skin skin;
	TextureAtlas atlas;
	TextButton button;
	private GameScreen gamescreen;
	private World world;
	private TextButton upgradeHealth,upgradeNuke,upgradeProtection,upgradeTurret,upgradeArmour,upgradeWeapon;
	private UpgradeManager upgradeManager;
	private Label currentMoneyLevel,nightmareMode;
	private TextButtonStyle style;
	private WidgetManager widgetManager;
	private int outwidth,outheight;
	private TextButton upgradeButton,weaponDamage,weaponSpeed;
	private Image upgradeLogo;
	private BackgroundHandler background;
	
	
	public UpgradesScreen2(PlutoWasAPlanetOnce game,Screen gs,World world)
	{
		this.game = game;
		this.gamescreen = (GameScreen) gs;
		this.world = world;
		upgradeManager = world.getUpgradeManager();
		batch = new SpriteBatch();
		widgetManager = new WidgetManager(upgradeManager, game);
		upgradeLogo = new Image(game.getAssetManager().get("data/upgrades/upgradetitle.png", Texture.class));

	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);

		batch.begin();
		stage.draw();
		//Table.drawDebug(stage);
		batch.end();
	}

	@Override
	public void resize(int width, int height) 
	{
		outwidth = width;
		outheight = height;

		if (stage == null)
		{
			stage = new Stage(); // bool scale to screen
			stage.clear();
			uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
			skin = new Skin();
			
			atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
			skin.addRegions(atlas);
			
			Boolean nightmare = false;
			if(world.getGameMode() == 2 )
			{
				nightmare = true;
			}
			background = new BackgroundHandler(20,new Vector2(width, height),game.getAssetManager().get("data/world/star.png", Texture.class),
					nightmare, true);
			
			stage.addActor(background);
			
			black = new BitmapFont(Gdx.files.internal("data/font/black.fnt"), false);
			currentMoneyLevel = new Label("Money : " + world.getShip().getMoney(),uiSkin);
			currentMoneyLevel.setPosition(width/8,  height - height/4);
			label = new Label("Upgrades", uiSkin);
			label.setX(0);
			label.setY(height/2 + height/4+height/8);
			label.setWidth(width);
			label.setAlignment(Align.center);
			//stage.addActor(label);
			
			
			nightmareMode = new Label("Maximum of one upgrade per wave !",uiSkin);
			if(world.getGameMode() == 2){
				nightmareMode.setPosition(width/2 - nightmareMode.getWidth() ,2*nightmareMode.getHeight() );
				stage.addActor(nightmareMode);
			}
			
			upgradeLogo.setPosition(width/2-upgradeLogo.getWidth()/2, height - height/8  - upgradeLogo.getHeight()/4);

			stage.addActor(upgradeLogo);
			
			style = new TextButtonStyle();
			style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
			style.down = skin.getDrawable("buttonpressed"); 
			style.font = black;
			
			Gdx.input.setInputProcessor(stage);
			
			upgradeButton = widgetManager.getButton();
			weaponDamage = widgetManager.getWeaponDamageButton();
			weaponSpeed = widgetManager.getWeaponSpeedButton();
			currentMoneyLevel.setText("Money : " + world.getShip().getMoney());

			stage.draw();

			container = new Table();
			container.setFillParent(false);
			//container.debug();
		
			detailContainer = new Table();
			detailContainer.reset();
		
			upgradeHealth = new TextButton("Health",style);
			
			upgradeNuke = new TextButton("Nuke",style);
			
			upgradeProtection = new TextButton("Protection",style);
			
			upgradeTurret = new TextButton("Turret",style);
			
			upgradeArmour = new TextButton("Armour",style);
			
			upgradeWeapon = new TextButton("Weapon",style);
			
			container.add(upgradeHealth).width(100);
			container.add(upgradeNuke).width(100);
			container.add(upgradeProtection).width(100);
			container.row();
			container.add(upgradeTurret).width(100);
			container.add(upgradeArmour).width(100);
			container.add(upgradeWeapon).width(100);
			
			container.setPosition(width/2 , (height/2-height/4));
			container.align(Align.center);
			
			button = new TextButton("Start",style);
			button.setWidth(200);
			button.setHeight(50);
			button.setX(width - (button.getWidth() + button.getWidth()/4));
			button.setY(height/2 - height/4);			
			stage.addActor(detailContainer);
			stage.addActor(container);
			stage.addActor(currentMoneyLevel);
			stage.addActor(button);
			stage.addActor(detailContainer);
			
			
			button.addListener(new InputListener(){
				
				public boolean touchDown(InputEvent event, float x,float y , int pointer, int button)
				{
					
					
					return true;
				}
				public void touchUp(InputEvent event, float x,float y , int pointer, int button){
					
					System.out.println("Up");
					world.setInputHandler();
					world.removeAllBullets();
					world.setActionBeginTime(TimeUtils.nanoTime());
					world.setWaveStatus(0);
					world.setDelay(true);
					gamescreen.hud.SetInput(gamescreen.world.getInputHandler());
					upgradeManager.setUpgradeAdded(false);
					game.setScreen(gamescreen);
					stage.draw();
				}
			});
		
		
		upgradeHealth.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				System.out.println("Up");
				
					//stage.clear();
					detailContainer.reset();
					detailContainer = widgetManager.createTable(0);
					
					//detailContainer.debug();
					currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
					detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));					upgradeButton = widgetManager.getButton();
					stage.addActor(detailContainer);
					stage.draw();
				
			}
		});
		
		upgradeNuke.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				System.out.println("Up");
				
				
					//stage.clear();
					//upgradeManager.addNuke(upgradeManager.getCost(1,upgradeManager.getNumberOfNukes()));
					//nukeCost.setText("Cost :" + upgradeManager.getCost(1,upgradeManager.getNumberOfNukes()));
					detailContainer.reset();
					detailContainer = widgetManager.createTable(1);
				//	detailContainer.debug();
					upgradeButton = widgetManager.getButton();

					
					currentMoneyLevel.setText("Money : " + world.getShip().getMoney());


					detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));
					stage.addActor(detailContainer);
					stage.draw();
				
			}

	});
		
		upgradeProtection.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				System.out.println("Up");
				
				detailContainer.reset();
				detailContainer = widgetManager.createTable(2);
				//detailContainer.debug();
				upgradeButton = widgetManager.getButton();

				currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
				detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));				
				stage.addActor(detailContainer);
				stage.draw();;	
			}

	});
		
		upgradeTurret.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				System.out.println("Up");
				detailContainer.reset();
				detailContainer = widgetManager.createTable(3);
				//detailContainer.debug();
				upgradeButton = widgetManager.getButton();

				currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
				detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));				
				stage.addActor(detailContainer);
				stage.draw();;
			
				
				
			}

	});
	upgradeArmour.addListener(new InputListener(){
			
			public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
				
				
				return true;
			}
			public void touchUp(InputEvent event, float x,float y , int pointer, int button){
				
				System.out.println("Up");
				
				detailContainer.reset();
				detailContainer = widgetManager.createTable(4);
				//detailContainer.debug();
				upgradeButton = widgetManager.getButton();

				currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
				detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));		
				stage.addActor(detailContainer);
				stage.draw();;
				
				
				
			}

	});
	upgradeWeapon.addListener(new InputListener(){
		
		public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
			
			
			return true;
		}
		public void touchUp(InputEvent event, float x,float y , int pointer, int button){
			
			System.out.println("Up");
			
			detailContainer.reset();
			detailContainer = widgetManager.getWeaponTable();
			detailContainer.debug();
			upgradeButton = widgetManager.getButton();

			currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
			detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));			
			stage.addActor(detailContainer);
			stage.draw();;
			
			
			
		}

});
	
	upgradeButton.addListener(new InputListener(){
		
		public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
			
			
			return true;
		}
		public void touchUp(InputEvent event, float x,float y , int pointer, int button){
			
			System.out.println("UPGRADE BUTTON");
			upgradeManager.upgrade(widgetManager.getUpgradeID());
			currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
			detailContainer.reset();
			detailContainer = widgetManager.createTable(widgetManager.getUpgradeID());
			detailContainer.setPosition(outwidth/2 , (float) (outheight-outheight/3));
			upgradeManager.setUpgradeAdded(true);
			stage.draw();
			
			
			
		}

});
	
weaponDamage.addListener(new InputListener(){
		
		public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
			
			
			return true;
		}
		public void touchUp(InputEvent event, float x,float y , int pointer, int button){
			
			System.out.println("UPGRADE DAMAGE");
			upgradeManager.upgradeWeaponDamage();
			currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
			detailContainer.reset();
			detailContainer = widgetManager.getWeaponTable();
			upgradeManager.setUpgradeAdded(true);
			stage.draw();
			
			
			
		}

});

weaponSpeed.addListener(new InputListener(){
	
	public boolean touchDown(InputEvent event, float x,float y , int pointer, int button){
		
		
		return true;
	}
	public void touchUp(InputEvent event, float x,float y , int pointer, int button){
		
		System.out.println("UPGRADE DAMAGE");
		upgradeManager.upgradeWeaponSpeed();
		currentMoneyLevel.setText("Money : " + world.getShip().getMoney());
		detailContainer.reset();
		detailContainer = widgetManager.getWeaponTable();
		upgradeManager.setUpgradeAdded(true);
		stage.draw();
		
		
		
	}

});
	
		}
	
	}

	@Override
	public void show() {
		// show is called first then resize - so called again
		if(detailContainer!=null){
		detailContainer.reset();
		}

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		//batch.dispose();
		black.dispose();
		atlas.dispose();
	}

	public Stage getStage(){
		return stage;
	}
}
