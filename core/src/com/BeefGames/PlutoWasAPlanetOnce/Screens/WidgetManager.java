package com.BeefGames.PlutoWasAPlanetOnce.Screens;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.UpgradeManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


public class WidgetManager  {

	private UpgradeManager upgradeManager;
	private Table container,weaponContainer;
	private Label description,cost,damageCost,speedCost;
	private int costMoney,damageCostMoney,speedCostMoney;
	private Texture nuke,armour,health,weapon,protection,turret,table;
	private Image tableImage,nukeImage,upgradeLogo,healthImage,armourImage,protectionImage,turretImage,weaponImage;
	private Image logo[];
	private TextButton upgradeButton,weaponDamage,weaponSpeed;
	private Skin skin,uiSkin;
	private TextureAtlas atlas;
	private TextButtonStyle style;
	private BitmapFont black;
	private int upgradeID;
	
	public WidgetManager(UpgradeManager upgradesManager, PlutoWasAPlanetOnce game)
	{
		this.upgradeManager = upgradesManager;
		
		
		//Upgrades

		AssetManager assetManager = game.getAssetManager();
		
		
		black = new BitmapFont(Gdx.files.internal("data/font/black.fnt"), false);
		
		nuke = assetManager.get("data/upgrades/nukeIcon.png",Texture.class);
		health = assetManager.get("data/upgrades/healthIcon.png",Texture.class);
		armour = assetManager.get("data/upgrades/armourIcon.png",Texture.class);
		protection = assetManager.get("data/upgrades/shieldIcon.png",Texture.class);
		turret = assetManager.get("data/upgrades/turretIcon.png",Texture.class);
		weapon = assetManager.get("data/upgrades/weaponIcon.png",Texture.class);
		
		table = new Texture(Gdx.files.internal("data/tablebackground.png"));
		tableImage = new Image(table);
		nukeImage = new Image(nuke);
		healthImage = new Image(health);
		armourImage = new Image(armour);
		turretImage = new Image(turret);
		protectionImage = new Image(protection);
		weaponImage = new Image(weapon);
		
		logo = new Image[6];
		logo[0] = healthImage;
		logo[1] = nukeImage;
		logo[2] = protectionImage;
		logo[3] = turretImage;
		logo[4] = armourImage;
		logo[5] = weaponImage;
		
		skin = new Skin();
		
		atlas = new TextureAtlas("data/ui/button.pack"); //takes file as string
		skin.addRegions(atlas);
		
		uiSkin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
		
		style = new TextButtonStyle();
		style.up = skin.getDrawable("buttonnormal"); //refers to part of .pack file
		style.down = skin.getDrawable("buttonpressed"); 
		style.font = black;
		
		upgradeButton = new TextButton("Upgrade",style);
		weaponDamage = new TextButton("Upgrade Damage",style);
		weaponSpeed = new TextButton("Upgrade Speed",style);
		container = new Table();
		weaponContainer = new Table();	
	}
	
	public Image getlogo(int ID){
		
		return logo[ID];
	}
	public String getDescription(int ID){
		
		return upgradeManager.getUpgradeDescription(ID);
	}
	public int getCost(int Id){
		
		costMoney = upgradeManager.getCost(Id, upgradeManager.getLevel(Id));
		return costMoney;
	}
	

	
	public Table createTable(int upgradeID){
		upgradeLogo = getlogo(upgradeID);
		description =new Label( getDescription(upgradeID),uiSkin);
		cost = new Label("Cost :" + getCost(upgradeID),uiSkin);
		this.upgradeID = upgradeID;
		
		container.add(upgradeLogo).colspan(1);
		container.add(upgradeButton).colspan(1);
		container.row();
		container.add(cost).colspan(2);
		container.row();
		container.add(description).colspan(2);
		
		

		
		return container;
		
	}
	
	public Table getWeaponTable(){
		upgradeLogo = getlogo(5);
		description = new Label(getDescription(5),uiSkin);
		damageCostMoney = getCost(5);
		speedCostMoney = getCost(6);
		damageCost = new Label("Cost: "+ getCost(5),uiSkin);
		speedCost = new Label("Cost: "+ getCost(6),uiSkin);
		description = new Label(getDescription(5),uiSkin);
		this.upgradeID = 5;
		
		
		weaponContainer.add(upgradeLogo).colspan(2).height(upgradeLogo.getHeight());
		weaponContainer.row();
		weaponContainer.add(weaponDamage).colspan(1);
		weaponContainer.add(weaponSpeed).colspan(1);
		weaponContainer.row();
		weaponContainer.add(damageCost).colspan(1);
		weaponContainer.add(speedCost).colspan(1);
		weaponContainer.row();
		weaponContainer.add(description).colspan(2);
		
		return weaponContainer;
	}
	
	public TextButton getButton(){
		
		return upgradeButton;
	}
	
	public TextButton getWeaponDamageButton(){
		return weaponDamage;
	}
	public TextButton getWeaponSpeedButton(){
		return weaponSpeed;
	}
	public int getUpgradeID(){
		return upgradeID;
	}
}
