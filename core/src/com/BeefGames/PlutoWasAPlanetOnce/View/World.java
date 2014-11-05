package com.BeefGames.PlutoWasAPlanetOnce.View;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Planet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Elite;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Sniper;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.GameScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.UpgradesScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.Token;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.TokenManager;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.ProtectionBubble;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.Turret;
import com.BeefGames.PlutoWasAPlanetOnce.Upgrades.UpgradeManager;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.AudioHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.CollisionHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.InputHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.ParticleHandler;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.TimeHandler;
import com.BeefGames.PlutoWasAPlanetOnce.Wave.WaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class World 
{
	private Boolean debug = false;
	
	PlutoWasAPlanetOnce game;

	// In-game objects
	private Ship ship;
	private Planet planet;
	private Array<Enemy> es;
	private Array<Bullet> bs;
	private Array<Bullet> turretBullets;
	private Turret turret;
	private ProtectionBubble protectionBubble;
	
	// Iterators
	private Iterator<Bullet> bIter;
	private Iterator<Enemy> eIter;
	private Bullet b;
	private Enemy e;

	// Controllers
	private WaveManager wavemanager;
	private WorldRenderer worldRenderer;
	private InputHandler inputHandler;
	private CollisionHandler collisionHandler;
	private TokenManager tokenManager;
	private ParticleHandler particleHandler;
	private AudioHandler audioHandler;
	
	private int difficulty;
	private int level;
	private long elapsedTime;
	private long actionBeginTime;
	private int kills,totalKills;
	
	private int gameStatus;
	
	private GameScreen gameScreen;
	private UpgradesScreen upgradesScreen;

	Texture circleTexture, explosionCircle;
	Sprite circle, explosionCircleSprite;
	
	private Boolean protection = false;
	private Boolean turretActive = false;
	private boolean upgradesComplete;
	
	private final Vector2 WORLDSIZE = new Vector2(Gdx.graphics.getWidth()*4, Gdx.graphics.getWidth()*4); 

	private Iterator<Bullet> bulletIter;
	private Array<Token> tokenArray;
	private Iterator<Token> tokenIter;
	private Token token;
	private int moneyBefore;
	private boolean timeDelay;
	
	private Sniper snipe;
	private Array<Bullet> enemyBullets;
	private int gameMode;
	private TimeHandler timeHandler = new TimeHandler();
	private int moneySpent;
	private long turretFireTime;
	private long turretStartTime;
	private UpgradeManager upgradeManager;
	private int turretKill,eliteKills,upgrades;
	
	
	public World(PlutoWasAPlanetOnce game, GameScreen gs,int gameMode) 
	{
		this.game = game;
		this.gameScreen = gs;
		this.gameMode = gameMode;
		difficulty = 1;
		level = 0;
		elapsedTime = 0;	
		
		// Set up handlers
		audioHandler = game.getAudioHandler();
	
		//Used by the renderer to colour stars red if the mode is nightmare
		Boolean nightmare;
		if(gameMode == 2)
			nightmare = true;
		else
			nightmare = false;
		
		worldRenderer = new WorldRenderer(game.getAssetManager(), debug, WORLDSIZE, nightmare);
		wavemanager = new WaveManager(difficulty,WORLDSIZE.x,WORLDSIZE.y,this,gameMode, worldRenderer);
		
		// Temporary values for player
		float playerStartHealth = 100f;
		float playerMaxHealth = 100f;
		float playerMaxArmour = 20f;
		float cooldown = 0.4f;
		float playerDamage = 5f;
		turretStartTime = TimeUtils.nanoTime();
		
		Texture s = worldRenderer.getTexture("ship");
		
		ship = new Ship(new Vector2(5, 5),(int)( s.getWidth()), (int)(s.getHeight()), 0, 10f, playerStartHealth,
				playerMaxHealth, playerMaxArmour, cooldown, playerDamage, s.getWidth(), s.getHeight(), 5);
		
		//Temporary starting value
		ship.setArmour(20f);

		// Temporary value for planet health
		float planethealth = 1f;
		
		Texture p = worldRenderer.getTexture("planet");
		
		planet = new Planet( new Vector2(0, 0),p.getWidth(),p.getHeight(),
				planethealth,p.getWidth(), p.getHeight());
		
		inputHandler = new InputHandler(this);
		Gdx.input.setInputProcessor(inputHandler);

		// Add things to draw
		worldRenderer.addDrawn("planet", "planet", 
				new Vector2(planet.getPosition().x- planet.getWidth() / 2, planet.getPosition().y- planet.getHeight() / 2),
				new Vector2(planet.getWidth() / 2, planet.getHeight() / 2),
				new Vector2(planet.getWidth(), planet.getHeight()),
				new Vector2(1, 1), 0, new Vector2(0, 0), 
				new Vector2(worldRenderer.getTexture("planet").getWidth(),worldRenderer.getTexture("planet").getHeight()), 
				false,false, true);
		
		worldRenderer.addDrawn("ship", "ship", ship.getPosition(),
				new Vector2(ship.getWidth() / 2, ship.getHeight() / 2),
				new Vector2(ship.getWidth(), ship.getHeight()),
				new Vector2(1,1), ship.getRotation(), new Vector2(0, 0), 
				new Vector2(worldRenderer.getTexture("ship").getWidth(),worldRenderer.getTexture("ship").getHeight()), 
				false,false, true);

		particleHandler = new ParticleHandler(worldRenderer,ship);
		wavemanager.setParticleHandler(particleHandler);
		
		//Exhaust needs to be below the ship
		worldRenderer.swapDrawns("ship", "exhaust");
		
		wavemanager.createWave(planet.getPosition(), level, difficulty, debug);
		
		upgradesScreen = new UpgradesScreen(game,gameScreen,this);
		tokenManager = new TokenManager(worldRenderer,this);
		
		if(debug)
		{
			worldRenderer.addRect(planet.getBounds());
			worldRenderer.addRect(ship.getBounds());
			worldRenderer.addRect(new Rectangle(0,0,2,2));
		}

		turretBullets = new Array<Bullet>();
		enemyBullets = new Array<Bullet>();
		
		collisionHandler = new CollisionHandler(game.getAssetManager().get("data/world/planetfinal.png", Pixmap.class),
				es, planet, worldRenderer,this,tokenManager,particleHandler,ship);
		
		moneyBefore = ship.getMoney();
		timeDelay = false;
		
		audioHandler.startMusic();
		
		if(gameMode == 2){
		
		upgradeManager = new UpgradeManager(this,game,true);
		
		}
		else{
			upgradeManager = new UpgradeManager(this,game,false);
		}
	}

	public void update() 
	{
		
		/*
		 * if(accelhandler.checkAccellerometer()) {
		 * accelhandler.moveShip(Gdx.graphics.getDeltaTime()); }
		 */
		//System.out.println(worldRenderer.getTexture("planet").getWidth());
		ship.update(WORLDSIZE);
		
		wavemanager.update(ship, planet.getPosition());
		es = wavemanager.getenemies();
		bs = ship.getBullets();
		
		eIter = es.iterator();
		while(eIter.hasNext())
		{
			e = eIter.next();
			if(e.getType() == "Sniper")
			{
				snipe = (Sniper) e;
			
				if(snipe.getShotRadius().contains(ship.getPosition()))
				{
					snipe.setMoving(false);
					snipe.Fire(ship.getPosition().x+ship.getWidth()/2 , ship.getPosition().y+ship.getHeight()/2  );

				}
				else if(!snipe.getMoving())
				{
					snipe.setMoving(true);
				}
			}
			else if(e.getType() == "Elite")
			{
				Elite boss = (Elite) e;
				
				if(boss.getShotCircle().contains(ship.getPosition()))
				{
					boss.Fire(ship.getPosition().x+ship.getWidth()/2 , ship.getPosition().y+ship.getHeight()/2);
				}
			}
		}
		
		bIter = enemyBullets.iterator();
		while(bIter.hasNext())
		{
			b = bIter.next();
			b.update();
			worldRenderer.updateDrawn("bullet" + b.hashCode(), b.getPosition(),new Vector2(b.getWidth() / 2, b.getHeight() / 2),
					new Vector2(b.getWidth(), b.getHeight()),new Vector2(1, 1), b.getRotation());
			
		}
		
		if(turret != null)
		{
			turretBullets = turret.getBullets();
		}
		
		//Handle Collisions
		collisionHandler.checkEnemyCollisions(es, bs);
		collisionHandler.checkPlanetCollisions(es, planet);
		collisionHandler.checkShipCollision(ship, es);
		collisionHandler.checkSniperCollision(ship,enemyBullets);

		if(turretActive)
		{
			if(turret.getHasTarget())
			{
				if(!turret.getTarget().getDestroyed())
				{		//If the turret has a target that isnt destroyed, try to shoot it
					e = turret.getTarget();
					turretFireTime = TimeUtils.nanoTime();
					turret.rotate();
					
					if(((turretFireTime - turretStartTime) / 1000000000) > 0.2)
					{
						turret.Fire(e.getPosition().x + e.getWidth()/2, e.getPosition().y + e.getHeight()/2);
						turretStartTime = TimeUtils.nanoTime();
					}
				}
				else
				{
					turret.setHasTarget(false);
				}
			}
			else
			{		//If the turret has no target, search for one
				eIter = es.iterator();	
				while(eIter.hasNext())
				{
					e = eIter.next();
					if(turret.getCircleBounds().contains(new Vector2(e.getPosition().x + e.getWidth()/2,
							e.getPosition().y + e.getHeight()/2)))
					{
						turret.setTarget(e);
						turret.setHasTarget(true);
						break;
					}
				}
			}
			collisionHandler.checkTurretCollisions(turret,es,turretBullets);
		}
		
		if (!wavemanager.checkAlive()) {

			if (!upgradesComplete) {
				
				bs = ship.getBullets();
				bulletIter = bs.iterator();
				// clean up bullet array
				while(bulletIter.hasNext()){
					b = bulletIter.next();
					worldRenderer.removeDrawn("bullet" + b.hashCode());
					bulletIter.remove();
				}
				
				tokenArray = tokenManager.getTokens();
				tokenIter = tokenArray.iterator();
				// clean up token array
				while(tokenIter.hasNext()){
					token = tokenIter.next();
					if(token.getPosition().x >= WORLDSIZE.x/2 || token.getPosition().y >= WORLDSIZE.y/2){
						
						worldRenderer.removeDrawn("token" + token.hashCode());
						tokenIter.remove();
					}
					else if(token.getPosition().x <=- WORLDSIZE.x/2 || token.getPosition().y <= - WORLDSIZE.y/2){
						
						worldRenderer.removeDrawn("token"+ token.hashCode());
						tokenIter.remove();
					}
				}
				
				gameStatus = 1;
				upgradesComplete = true;
				particleHandler.stopAll();
				
			}
			long currentTime = TimeUtils.nanoTime();
			elapsedTime = ((actionBeginTime - currentTime) / 1000000000);
			

			particleHandler.startExhaust();
			if (elapsedTime == -5 && gameStatus == 0) {
				
				kills =0;
				level++;
				wavemanager.createWave(planet.getPosition(), level, difficulty, debug);
				upgradesComplete = false;
				moneyBefore = ship.getMoney();
				timeDelay = false;
			}
		}

		// Update drawing info for bullets
		bIter = bs.iterator();
		while (bIter.hasNext()) 
		{
			b = bIter.next();

			worldRenderer.updateDrawn("bullet" + b.hashCode(), b.getPosition(),new Vector2(b.getWidth() / 2, b.getHeight() / 2),
					new Vector2(b.getWidth(), b.getHeight()),new Vector2(1, 1), b.getRotation());
		}

		// Update drawing info for enemies
		eIter = es.iterator();
		while (eIter.hasNext()) 
		{
			e = eIter.next();

			worldRenderer.updateDrawn("enemy" + e.hashCode(), e.getPosition(),new Vector2(e.getWidth() / 2, e.getHeight() / 2),
					new Vector2(e.getWidth(), e.getHeight()),new Vector2(1, 1), e.getRotation());
			//Update elite turret
			if(e.getType() =="Elite")
			{
				Elite el = (Elite)e;
				
				worldRenderer.updateDrawn("enemyturret" + e.hashCode(), el.getTurretPosition(),el.getTurretOrigin(),
						el.getTurretSize(),new Vector2(1, 1), el.getTurretRotation());
			}
		}
		if(turretActive)
		{
			if(turret.getAmmo() > 0){
			worldRenderer.updateDrawn("turret"+turret.hashCode(), 
					turret.getPosition(),
					new Vector2(turret.getWidth()/2,turret.getHeight()/2), 
					new Vector2(turret.getWidth(),turret.getHeight()), 
					new Vector2(1,1),
					turret.getRotation());
			}else{
				
				particleHandler.addTurretRemoval(turret.getPosition().x + turret.getWidth()/2, turret.getPosition().y + turret.getHeight()/2);
				worldRenderer.removeDrawn("turret"+turret.hashCode());
				worldRenderer.removeDrawn("turretBase");
				turretActive = false;
				
			}
		}
		
		bIter = turretBullets.iterator();
		while(bIter.hasNext())
		{
			b = bIter.next();
			b.update();
			worldRenderer.updateDrawn("bullet" + b.hashCode(), b.getPosition(),new Vector2(b.getWidth() / 2, b.getHeight() / 2),
					new Vector2(b.getWidth(), b.getHeight()),new Vector2(1, 1), b.getRotation());
		}

		// Update drawing info for planet and ship
		worldRenderer.updateDrawn("ship", ship.getPosition(),
				new Vector2(ship.getWidth() / 2, ship.getHeight() / 2),
				new Vector2(ship.getWidth(), ship.getHeight()), new Vector2(1,1), ship.getRotation());

		worldRenderer.updateDrawn("planet",
				new Vector2(planet.getPosition().x- planet.getWidth() / 2, planet.getPosition().y- planet.getHeight() / 2),
				new Vector2(planet.getWidth() / 2, planet.getHeight() / 2), 
				new Vector2(planet.getWidth(), planet.getHeight()), new Vector2(1,1), 0);

		particleHandler.updateParticles();
		
		tokenManager.update();
		
		if(debug)
		{
			//worldRenderer.setPoints(ship.getPolyBounds().getTransformedVertices());
		}
	}
	
	public void render() {
		worldRenderer.render(new Vector3(ship.getPosition().x + ship.getWidth()/2, ship
				.getPosition().y + ship.getHeight()/2, 0));
	}

	public void dispose() {
		wavemanager.dispose();
		worldRenderer.dispose();
		
		
	
	}
	public int getKills(){
		return kills;
	}
	public void addKill(){
		totalKills++;
		kills++;
	}
	public int getWaveStatus(){
		return gameStatus;
	}
	public void setWaveStatus( int status){
		gameStatus = status;
	}
	public int getLevel(){
		return level;
	}
	public UpgradesScreen getUpgradesScreen(){
		return upgradesScreen;
	}
	public void setProtectionBubble(ProtectionBubble pb){
		protectionBubble = pb;
		worldRenderer.addDrawn("protectionBubble", "protectionBubble", protectionBubble.getPosition(), protectionBubble.getPosition(), new Vector2(worldRenderer.getTexture("protectionBubble").getWidth(),worldRenderer.getTexture("protectionBubble").getHeight()), new Vector2(1,1), 0, new Vector2(0,0), new Vector2(worldRenderer.getTexture("protectionBubble").getWidth(),worldRenderer.getTexture("protectionBubble").getHeight()), false, false, true);
		protection = true;
	}
	public boolean getProtectionBubble(){
		
		return protection;
	}
	public void setProtection(boolean protect){
		protection = protect;
	}
	public ProtectionBubble getBubble(){
		
		return protectionBubble;
	}
	public void setTurret(Turret turret){
		
		if(!turretActive)
		{
			this.turret = turret;
			worldRenderer.addDrawn("turretBase","turretBase",new Vector2(turret.getPosition().x+worldRenderer.getTexture("turret").getWidth()/2 - worldRenderer.getTexture("turretBase").getWidth()/2,turret.getPosition().y+worldRenderer.getTexture("turret").getHeight()/2 - worldRenderer.getTexture("turretBase").getHeight()/2),new Vector2(planet.getWidth()/2,planet.getHeight()/2),new Vector2(worldRenderer.getTexture("turretBase").getWidth(),worldRenderer.getTexture("turretBase").getHeight()),new Vector2(1,1),0,new Vector2(0,0),new Vector2(worldRenderer.getTexture("turretBase").getWidth(),worldRenderer.getTexture("turretBase").getHeight()), false, false, true);

			worldRenderer.addDrawn("turret" + turret.hashCode(), "turret", turret.getPosition(), new Vector2(planet.getWidth()/2,planet.getHeight()/2), new Vector2(worldRenderer.getTexture("turret").getWidth(),worldRenderer.getTexture("turret").getHeight()), new Vector2(1,1), 0, new Vector2(0,0), new Vector2(worldRenderer.getTexture("turret").getWidth(),worldRenderer.getTexture("turret").getHeight()), false, false, true);
			turretActive = true;
			
			//Make sure protection bubble is on top always
			if(protection)
			{
				worldRenderer.swapDrawns("turret"+turret.hashCode(), "protectionBubble");
				worldRenderer.swapDrawns("turretBase", "ship");
				worldRenderer.swapDrawns("turret"+turret.hashCode(), "exhaust");
				worldRenderer.swapDrawns("turretBase", "turret"+turret.hashCode());
			}
			else
			{
				worldRenderer.swapDrawns("turret"+turret.hashCode(), "ship");
				worldRenderer.swapDrawns("turretBase", "exhaust");
			}
		}
	}
	public Boolean getDebug(){
		return debug;
	}
	public Ship getShip() {
		return ship;
	}
	public Planet getPlanet() {
		return planet;
	}
	public void setRenderer(WorldRenderer wr) {
		this.worldRenderer = wr;
	}
	public WorldRenderer getRenderer() {
		return worldRenderer;
	}
	public WaveManager getManager() {
		return wavemanager;
	}
	public void setActionBeginTime(long a) {
		actionBeginTime = a;
	}
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	public boolean getStatus() {
		return gameScreen.getStatus();
	}
	public void setInputHandler() {
		Gdx.input.setInputProcessor(inputHandler);
	}
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	public ParticleHandler getParticleHandler(){
		return particleHandler;
	}
	public AudioHandler getAudioHandler(){
		return audioHandler;
	}
	public Array<Enemy> getEnemies()
	{
		return es;
	}
	public Array<Token> getTokens()
	{
		return tokenManager.getTokens();
	}
	public Vector2 getWorldSize()
	{
		return WORLDSIZE;
	}
	public int getMoneyBefore(){
		
		return moneyBefore;
	}
	public TimeHandler getTimeHandler(){
		return timeHandler;
	}
	public boolean checkDelay(){
		
		return timeDelay;
	}
	public void setDelay(boolean delay){
		
		timeDelay = delay;
	}
	public long getElapsedTime(){
		return elapsedTime;
	}
	public void addEnemyBullet(Bullet b){
		enemyBullets.add(b);
	}
	public int getGameMode(){
		
		return gameMode;
	}
	public boolean getTurretActive(){
		
		return turretActive;
	}
	public Turret getTurret(){
		
		return turret;
	}
	public void setMoneySpent(int money){
		moneySpent = money;
	}
	public int getMoneySpent(){
		return moneySpent;
	}
	
	public int getTotalKills(){
		return totalKills;
	}
	public UpgradeManager getUpgradeManager(){
		return upgradeManager;
	}
	public void removeAllBullets(){
		bulletIter = enemyBullets.iterator();
		while(bulletIter.hasNext())
		{
			
			b = bulletIter.next();
			worldRenderer.removeDrawn("bullet"+ b.hashCode());
			bulletIter.remove();
		}
		bulletIter = turretBullets.iterator();
		while(bulletIter.hasNext())
		{
			
			b = bulletIter.next();
			worldRenderer.removeDrawn("bullet"+ b.hashCode());
			bulletIter.remove();
		}
		bulletIter = ship.getBullets().iterator();
		while(bulletIter.hasNext())
		{
			
			b = bulletIter.next();
			worldRenderer.removeDrawn("bullet"+ b.hashCode());
			bulletIter.remove();
		}
		
	}
	
	public void addTurretKill(){
		
		turretKill ++;
	}
	public int getTurretKills(){
		
		return turretKill;
	}
	public int getEliteKills(){
		return  eliteKills;
		
	}
	public void addEliteKill(){
		
		eliteKills++;
	}
	
	public void addUpgrade(){
		
		upgrades++;
	}
	public int getUpgrade(){
		
		return upgrades;
	}
	
	
}
