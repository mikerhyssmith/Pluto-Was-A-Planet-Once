package com.BeefGames.PlutoWasAPlanetOnce.View;

import java.util.ArrayList;
import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Planet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemies.Sniper;
import com.BeefGames.PlutoWasAPlanetOnce.Screens.GameScreen;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.Token;
import com.BeefGames.PlutoWasAPlanetOnce.Tokens.TokenManager;
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

	Texture circleTexture, explosionCircle;
	Sprite circle, explosionCircleSprite;
	
	private final Vector2 WORLDSIZE = new Vector2(Gdx.graphics.getWidth()*1.5f, Gdx.graphics.getWidth()*1.5f); 

	private Iterator<Bullet> bulletIter;
	private Array<Token> tokenArray;
	private Iterator<Token> tokenIter;
	private Token token;
	private int moneyBefore;
	private boolean timeDelay;
	
	private Sniper snipe;
	private Array<Bullet> enemyBullets;
	private TimeHandler timeHandler = new TimeHandler();
	private int upgrades;
	
	
	public World(PlutoWasAPlanetOnce game, GameScreen gs) 
	{
		this.game = game;
		this.gameScreen = gs;
		level = 1;
		elapsedTime = 0;	
		
		// Set up handlers
		audioHandler = game.getAudioHandler();
			
		worldRenderer = new WorldRenderer(game.getAssetManager(), debug, WORLDSIZE);
		wavemanager = new WaveManager(WORLDSIZE.x,WORLDSIZE.y,this, worldRenderer);
		
		// Temporary values for player
		float playerStartHealth = 100f;
		float playerMaxHealth = 100f;
		float playerMaxArmour = 20f;
		float cooldown = 0.4f;
		float playerDamage = 5f;
		
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
		
		tokenManager = new TokenManager(worldRenderer,this);
		
		if(debug)
		{
			worldRenderer.addRect(planet.getBounds());
			worldRenderer.addRect(ship.getBounds());
			worldRenderer.addRect(new Rectangle(0,0,2,2));
		}

		enemyBullets = new Array<Bullet>();
		
		collisionHandler = new CollisionHandler(game.getAssetManager().get("data/world/planetfinal.png", Pixmap.class),
				es, planet, worldRenderer,this,tokenManager,particleHandler);
		
		timeDelay = false;
		
		audioHandler.startMusic();
		
	}
	
	public void update() 
	{
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
			
			worldRenderer.updateDrawn("enemy" + e.hashCode(), e.getPosition(),new Vector2(e.getWidth() / 2, e.getHeight() / 2),
					new Vector2(e.getWidth(), e.getHeight()),new Vector2(1, 1), e.getRotation());
		}
		
		bIter = enemyBullets.iterator();
		while(bIter.hasNext())
		{
			b = bIter.next();
			b.update();
			worldRenderer.updateDrawn("bullet" + b.hashCode(), b.getPosition(),new Vector2(b.getWidth() / 2, b.getHeight() / 2),
					new Vector2(b.getWidth(), b.getHeight()),new Vector2(1, 1), b.getRotation());
			
		}
		
		//Handle Collisions
		collisionHandler.checkEnemyCollisions(es, bs);
		collisionHandler.checkPlanetCollisions(es, planet);
		collisionHandler.checkShipCollision(ship, es);
		collisionHandler.checkSniperCollision(ship,enemyBullets);
		
		if (!wavemanager.checkAlive()) {

			long currentTime = TimeUtils.nanoTime();
			elapsedTime = ((actionBeginTime - currentTime) / 1000000000);
			

			particleHandler.startExhaust();
			if (elapsedTime == -5 && gameStatus == 0) {
				
				kills =0;
				level++;
				wavemanager.createWave(planet.getPosition(), level, difficulty, debug);
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
	
	public int getTotalKills(){
		return totalKills;
	}

	public void removeAllBullets(){
		bulletIter = enemyBullets.iterator();
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
	
	public void addUpgrade(){
		
		upgrades++;
	}
	public int getUpgrade(){
		
		return upgrades;
	}
	
}
