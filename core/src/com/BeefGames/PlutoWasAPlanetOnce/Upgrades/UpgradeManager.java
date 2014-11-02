package com.BeefGames.PlutoWasAPlanetOnce.Upgrades;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.InputHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;



public class UpgradeManager {


	private int healthLevel;
	private World world;
	private Ship ship;
	private int nukeNumber;
	private Array<Nuke> nukeArray;
	private InputHandler inputHandler;
	private String[] description;
	private int turretLevel,nukeMoney,healthMoney,armourMoney,armourMultiplier,protectionMoney,protectionMultiplier;
	private int nukeMultiplier,healthMultiplier,upgradeNumber,upgradeLevels,armourLevel,turretMoney,turretMultiplier;
	private int weaponDamageMoney,weaponDamageMultiplier,weaponSpeedMoney,weaponSpeedMultiplier,weaponSpeedLevel,weaponDamageLevel,protectionlevel;
	private int cost[],multiplier[];
	private ProtectionBubble protectionBubble;
	private Turret turret;
	private int moneySpent;
	private PlutoWasAPlanetOnce game;
	private boolean nightmare,upgradeAdded;
	
	public UpgradeManager(World world,PlutoWasAPlanetOnce game,boolean nightmare){
		
		healthLevel = 1;
		this.nightmare = nightmare;
		upgradeAdded = false;
		this.game = game;
		this.world = world;
		ship = world.getShip();
		nukeArray = new Array<Nuke>();
		inputHandler = world.getInputHandler();
		upgradeNumber = 3;
		upgradeLevels = 4;
		healthMoney = 1;
		healthMultiplier =2;
		nukeMoney = 5;
		nukeMultiplier = 2;
		armourLevel =0;
		armourMoney = 7;
		armourMultiplier = 2;
		protectionMoney = 5;
		protectionMultiplier =2;
		turretMoney = 5;
		turretMultiplier = 3;
		weaponDamageMoney = 3;
		weaponDamageMultiplier = 7;
		weaponSpeedMoney = 2;
		weaponSpeedMultiplier = 7;
		weaponDamageLevel =0;
		weaponSpeedLevel =0;
		turretLevel =0;
		protectionlevel =0;
		
		description = new String[6];
		cost = new int[7];
		multiplier = new int[7];
		
		
		
		description[0] = "Increase the ships maximum health  ";
		description[1] = "    Add one nuke , Maximum of 3    ";
		description[2] = "  Protects the planet from impact" + "\n" + "Maximum of one per level";
		description[3] = "Adds a turret to protect the planet";
		description[4] = "   Increases your ships armour     ";
		description[5] = "  Upgrade your ships main weapon   ";
		
		cost[0] = healthMoney;
		cost[1] = nukeMoney;
		cost[2] = protectionMoney;
		cost[3] = turretMoney;
		cost[4] = armourMoney;
		cost[5] = weaponDamageMoney;
		cost[6] = weaponSpeedMoney;
	
		multiplier[0] = healthMultiplier;
		multiplier[1] = nukeMultiplier;
		multiplier[2] = protectionMultiplier;
		multiplier[3] = turretMultiplier;
		multiplier[4] = armourMultiplier;
		multiplier[5] = weaponDamageMultiplier;
		multiplier[6] = weaponSpeedMultiplier;
		
		//ship.setMoney(2000);
	}
	
	/**
	 * Gets the level of the health upgrade.
	 * @return Current upgrade level
	 */
	public int getHealthLevel(){
		
		return healthLevel;
	}
	
	public int getLevel(int id){
		
		if(id ==0){
			return healthLevel;
		}
		else if(id == 1){
			
			return nukeNumber;

		}
		else if (id == 2){
			
			return getProtectionLevel();
		}
		else if (id ==3){
			
			return getTurretLevel();
		}
		else if(id ==4){
			
			return armourLevel;
		}
		else if( id ==5){
			return weaponDamageLevel;
		}
		else if(id == 6){
			
			return weaponSpeedLevel;
		}
		else{
			return 0;
		}
		
		
	}
	
	/**
	 * Increases the level of the maximum health.
	 */
	public void upgradeHealthlevel()
	{
			if((ship.getMoney()-getCost(0,healthLevel)) >= 0)
			{
				ship.setMaxHealth(ship.getMaxHealth() + 20);
				ship.setHealth(ship.getHealth() +(ship.getHealth()*2));
				ship.setMoney(ship.getMoney()-getCost(0,healthLevel));
				moneySpent = moneySpent + getCost(0,healthLevel);
				world.setMoneySpent(moneySpent);
				healthLevel++;
			}
	}
	
	/**
	 * Adds a nuke to the array assuring there's a maximum of 3
	 */
	public void addNuke()
	{
		if(inputHandler.getNukes().size < nukeArray.size)
		{
			nukeArray = inputHandler.getNukes();
			nukeNumber = nukeArray.size;
		}
		if(nukeArray.size <3){
			
			if((ship.getMoney()-getCost(1,nukeNumber)) >= 0){
				Nuke n = new Nuke(new Vector2(0,0),1,1,world);
				nukeArray.add(n);
				inputHandler.setNuke(n);
				ship.setMoney(ship.getMoney()-getCost(1,nukeNumber));
				moneySpent = moneySpent + getCost(1,nukeNumber);
				world.setMoneySpent(moneySpent);

				nukeNumber = nukeArray.size;
			}
		}
	}
	
	/**
	 * Returns the current number of nukes available to the player
	 * @return Number of nukes
	 */
	public int getNumberOfNukes()
	{
		if(inputHandler.getNukes().size < nukeArray.size){
			nukeArray = inputHandler.getNukes();
		}
		
		nukeNumber = nukeArray.size;
		return nukeNumber;
	}
	
	public void upgradeProtectionBubble(){
		
		if((ship.getMoney()-getCost(2,protectionlevel)) >= 0){
			if(!world.getProtectionBubble()){
			
			int health;
        	if(protectionlevel == 0 || protectionlevel ==1 ){
        		
        		health = 20;
        	}
        	else {
        	health = (protectionlevel * 10) +5;
        	}
			
			
		protectionBubble = new ProtectionBubble(new Vector2(world.getPlanet().getPosition().x  - world.getRenderer().getTexture("protectionBubble").getWidth()/2 ,world.getPlanet().getPosition().y - world.getRenderer().getTexture("protectionBubble").getHeight()/2),world.getRenderer().getTexture("protectionBubble").getWidth(),world.getRenderer().getTexture("protectionBubble").getHeight(),health);
		world.setProtectionBubble(protectionBubble);
		ship.setMoney((ship.getMoney()-getCost(2,protectionlevel)));
		moneySpent = moneySpent + getCost(2,protectionlevel);
		world.setMoneySpent(moneySpent);

		protectionlevel ++;
			}
		}
	}
	public int getProtectionLevel(){
		
		return protectionlevel;
	}
	
    
    public void upgradeTurret(){
    	if(!world.getTurretActive()){
            if((ship.getMoney()-getCost(3,getTurretLevel())) >= 0){
            	
            	int ammo;
            	if(turretLevel == 0){
            		
            		ammo = 10;
            	}
            	else {
            	ammo = (turretLevel * 5) +5;
            	}
            	
                    turret = new Turret(new Vector2(world.getPlanet().getPosition().x/2- world.getRenderer().getTexture("turret").getWidth()/2,world.getPlanet().getPosition().y/2-world.getRenderer().getTexture("turret").getHeight()/2),world.getRenderer().getTexture("turret").getWidth(),world.getRenderer().getTexture("turret").getHeight(),world,ammo);
                    world.setTurret(turret);
                    ship.setMoney(ship.getMoney()-getCost(3,getTurretLevel()));
                    moneySpent = moneySpent + getCost(3,getTurretLevel());
        			world.setMoneySpent(moneySpent);

                    turretLevel ++;
            }
    	}
    }
	public boolean checkCost(int cost){
		
		if(ship.getMoney()- cost >=0){
			return true;
		}
		else return false;
	}
	
	public void upgradeArmour(){
		if((ship.getMoney()-getCost(4,armourLevel)) >= 0)
		{
			ship.setMaxArmour(ship.getMaxArmour() + 2);
			ship.setArmour(ship.getArmour()+2);
			ship.setMoney(ship.getMoney() -getCost(4,armourLevel));
			moneySpent = moneySpent + getCost(4,armourLevel);
			world.setMoneySpent(moneySpent);

			armourLevel++;
		}
	}
	public int getArmourLevel(){
		return armourLevel;
	}


	public int getTurretLevel(){
		
		return turretLevel;
	}
	
	public void upgradeWeaponDamage()
	{
		

		if((ship.getMoney()-getCost(5,weaponDamageLevel)) >= 0){

			ship.setDamage(ship.getDamage()+1.5f);
			ship.setMoney(ship.getMoney() -getCost(5,weaponDamageLevel));
			moneySpent = moneySpent + getCost(5,weaponDamageLevel);
			world.setMoneySpent(moneySpent);

			weaponDamageLevel++;
			
		
		}
		
	}
	
	public void upgradeWeaponSpeed(){
		
		if(weaponSpeedLevel <20){
		if((ship.getMoney()-getCost(6,weaponSpeedLevel)) >= 0){

			ship.setCooldown(ship.getCooldown()*0.90f);
			ship.setMoney(ship.getMoney() - getCost(6,weaponSpeedLevel));
			moneySpent = moneySpent + getCost(6,weaponSpeedLevel);
			world.setMoneySpent(moneySpent);

			weaponSpeedLevel++;
		}
		}
	}
	
	
	
	/**
	 * Returns a string containing the description of the next upgrade
	 * @param upgradeNumber A number which refers to the upgrade
	 * @param level Current level of the upgrade
	 * @return Description of the upgrade
	 */
	public String getUpgradeDescription(int upgradeNumber){
		
		return description[upgradeNumber];
	}
	
	public void upgrade(int ID){
		
		if(!upgradeAdded)
		{
		
			switch(ID) 
		
			{
		
		case 0: upgradeHealthlevel();
				break;
		case 1: addNuke();
				break;
		case 2: upgradeProtectionBubble();
				break;
		case 3: upgradeTurret();
				break;
		case 4: upgradeArmour();
				break;
		case 5: upgradeWeaponDamage();
				break;
		case 6: upgradeWeaponSpeed();
				break;
				
		
			}
		
			if(nightmare)
			{
		
				upgradeAdded = true;
		
			}
		
			else{
			
				upgradeAdded = false;
		
			}
		
		
		}
		
	}
		
	
	/**
	 * Gets the cost of the next upgrade
	 * @param upgradeNumber and Id referring to the upgrade
	 * @param level the current level of the upgrade
	 * @return an integer containing the cost of the upgrade
	 */
	public int getCost(int upgradeNumber,int level){
		int newCost;
		if(level >0){
		newCost = (cost[upgradeNumber]*multiplier[upgradeNumber])*level;
		
		}
		else{
			newCost = (cost[upgradeNumber]);
			
		}
		return newCost;
	}
	
	public void setUpgradeAdded(boolean upgrade){
		
		if(nightmare)
		{
			upgradeAdded = upgrade;
		}
		else{
			
			upgradeAdded = false;
		}
	}
}
