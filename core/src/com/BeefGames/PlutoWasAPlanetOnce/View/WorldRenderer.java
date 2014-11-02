package com.BeefGames.PlutoWasAPlanetOnce.View;

import java.util.HashMap;
import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BackgroundHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class WorldRenderer {
	private SpriteBatch batch;
	private AssetManager assetManager;
	private Vector2 worldSize;
	
	private OrthographicCamera cam;

	private float width;
	private float height;
	
	//New renderer stuff
	private String k;
	private Array<String> keys;
	
	private HashMap<String, Texture> textures;
	private HashMap<String, DrawnObject> drawnObjects;
	
	private Iterator<String> drawIter;
	
	private BackgroundHandler background;
	
	//Debug variables
	private Boolean debug;
	private ShapeRenderer sr;
	private Array<Rectangle> debugRects;
	private Iterator<Rectangle> rectIter;
	private Rectangle r;
	private float[] points;
	
	public WorldRenderer(AssetManager manager, Boolean debug, Vector2 worldSize, Boolean nightmare)
	{
		this.debug = debug;
		
		this.assetManager = manager;
		keys = new Array<String>();
		textures = new HashMap<String, Texture>();	
		drawnObjects = new HashMap<String, DrawnObject>(400,0.75f);
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		
		cam.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		batch.enableBlending();
		
		setUpTextures();
		
		background = new BackgroundHandler(300, worldSize, textures.get("star"), nightmare, false);
		
		if(debug)
		{
			sr = new ShapeRenderer();
			debugRects = new Array<Rectangle>();
		}	
		
		this.worldSize = worldSize;
	}
	
	/**
	 * Add a debug rectangle
	 * @param rect
	 */
	public void addRect(Rectangle rect)
	{
		debugRects.add(rect);
	}
	
	/**
	 * Adds a new texture that was previously loaded by the asset manager
	 * @param key Name of the texture
	 * @param path File path for the image
	 * @param minFilter  
	 * @param magFilter
	 */
	public void addTexture(String key, Texture texture, TextureFilter minFilter, TextureFilter magFilter)
	{
		texture.setFilter(minFilter, magFilter);
		textures.put(key, texture);
	}
	/**
	 * Returns a texture
	 * @param name Name of texture to return
	 * @return
	 */
	public Texture getTexture(String name)
	{
		return textures.get(name);
	}

	/**
	 * Add rectangle to be drawn on-screen
	 * @param name Name of the object
	 * @param texture Texture of rectangle
	 * @param position Position to draw at
	 * @param origin Offset of origin from bottom left corner 
	 * @param dimension Width and height 
	 * @param scale Scaling factors along x and y
	 * @param rotation Angle to draw at
	 * @param srcPos Source rectangle position in texture
	 * @param srcSize Source rectangle size in texture
	 * @param flipX Flip along x direction
	 * @param flipY Flip along y direction
	 * @param visible Only draws if true
	 */
	public void addDrawn(String name, String textureName, Vector2 position,	Vector2 origin, Vector2 dimension,
			Vector2 scale, float rotation, Vector2 srcPos, Vector2 srcSize, Boolean flipX,
			Boolean flipY, Boolean visible)
	{
		keys.add(name);	
		
		DrawnObject newDrawn = new DrawnObject(name, textures.get(textureName), position, origin, dimension, 
				scale,rotation,srcPos, srcSize, flipX, flipY, visible);
		drawnObjects.put(name, newDrawn);
	}
	public void addDrawn(String name, ParticleEmitter emitter)
	{
		keys.add(name);
		DrawnObject newDrawn = new DrawnObject(name, emitter, true);
		drawnObjects.put(name, newDrawn);
	}
	
	/**
	 * Update something already being drawn
	 * @param name Name of object to be updated
	 * @param position New position
	 * @param origin New origin
	 * @param dimension New size
	 * @param scale New scaling
	 * @param rotation New rotation
	 */
	public void updateDrawn(String name, Vector2 position,	Vector2 origin, Vector2 dimensions,
			Vector2 scale, float rotation)
	{
		DrawnObject updatedDrawn = drawnObjects.get(name);
		updatedDrawn.updateDrawn(position, origin, dimensions, scale, rotation);

		drawnObjects.put(name, updatedDrawn);
	}
	
	/**
	 * Delete something that was drawn
	 * @param name Name of object to be deleted
	 */
	public void removeDrawn(String name)
	{
		keys.removeValue(name, false);
		drawnObjects.remove(name);		
	}
	
	public void render(Vector3 camPosition)
	{
		/*
		//Remove finished emitters
		drawIter = keys.iterator();
		while(drawIter.hasNext())
		{
			k = drawIter.next();
			
			drawnObjects.get(k).updateDrawn();
			
			if(drawnObjects.get(k).needsRemoval())
			{
				drawIter.remove();
			}
		}*/
		
		//Clear the renderer
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Vector2 newCamPosition = new Vector2(0,0);
		
		//Centre the camera on the ship, making sure that the screen does not show outside the playable area
		if(Math.abs(camPosition.x) <= (worldSize.x/2 - width/2)) 
		{	newCamPosition.x = camPosition.x;	}
		else 
		{	newCamPosition.x = Math.copySign((worldSize.x/2 - width/2), camPosition.x);	}
		
		if(Math.abs(camPosition.y) <= (worldSize.y/2 - height/2))
		{	newCamPosition.y = camPosition.y;	}
		else 
		{	newCamPosition.y = Math.copySign((worldSize.y/2 - height/2), camPosition.y);	}
		
		cam.position.set(newCamPosition.x, newCamPosition.y, camPosition.z);	
		
		cam.update();
		
		//Set up the spritebatch
		batch.setProjectionMatrix(cam.combined);

		batch.begin();
		
		//Draw the background first
		background.draw(batch, 1);
		
		
		//Draw everything that is visible
		drawIter = keys.iterator();
		while(drawIter.hasNext())
		{
			k = drawIter.next();
			
			drawnObjects.get(k).updateDrawn();
			
			if(drawnObjects.get(k).needsRemoval())
			{
				drawIter.remove();
			}
			else if(drawnObjects.get(k).getVisible())
			{
				drawnObjects.get(k).draw(batch,Gdx.graphics.getDeltaTime() );
			}
		}
		batch.end();
		
		//Draw all debug rectangles
		if(debug)
		{
			sr.setProjectionMatrix(cam.combined); 
        	//sr.begin(ShapeType.Rectangle); // tell what kind of shape we want
        	sr.setColor(Color.CYAN);
        
        	rectIter = debugRects.iterator();
        	while(rectIter.hasNext())
        	{
        		r = rectIter.next();
            	sr.rect(r.x,r.y,r.width,r.height);
        	}
        	sr.setColor(Color.RED);
       
        	sr.end();
        	
        	//Used to draw vertices of polygons
        	sr.begin(ShapeType.Point); // tell what kind of shape we want
        	sr.setColor(Color.GREEN);
        	for(int i = 0; i < points.length; i += 2)
        	{
        		sr.point(points[i], points[1 + i], 0);
        	}
        	sr.end();
		}
	}
	
	public OrthographicCamera getCamera()
	{
		return cam;
	}
	public void setVisible(String name, Boolean visible)
	{
		drawnObjects.get(name).setVisible(visible);
	}
	public void setEmitter(String name, ParticleEmitter emitter)
	{
		drawnObjects.put(name, new DrawnObject(name, emitter, true));
	}
	public void setDrawableTexture(String name, String newTexture)
	{
		drawnObjects.get(name).setTexture(textures.get(newTexture));
	}
	
	
	public void setUpTextures()
	{
	    Texture newTexture;
	    
	    newTexture = assetManager.get("data/ship.png", Texture.class);
		addTexture("ship", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = new Texture(assetManager.get("data/world/planetfinal.png", Pixmap.class));
		addTexture("planet", newTexture, TextureFilter.Linear, TextureFilter.Linear);

	    newTexture = assetManager.get("data/enemies/Follower.png", Texture.class);
		addTexture("Follower", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/enemies/enemy1.png", Texture.class);
		addTexture("Soldier", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/enemies/enemy2.png", Texture.class);
		addTexture("PlanetScout", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
		newTexture = assetManager.get("data/enemies/Sniper.png",Texture.class);
		addTexture("Sniper",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/Bullet.png", Texture.class);
		addTexture("playerbullet", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/tokens/healthtoken.png", Texture.class);
		addTexture("healthtoken", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/tokens/armourtoken.png", Texture.class);
		addTexture("armourtoken", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/tokens/moneyToken.png", Texture.class);
		addTexture("moneytoken", newTexture, TextureFilter.Linear, TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/CIRCLE.png", Texture.class);
		addTexture("shipexhaust",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
	    newTexture = assetManager.get("data/CIRCLE.png", Texture.class);
		addTexture("explosion",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/CIRCLE.png", Texture.class);
		addTexture("nuke",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/CIRCLE.png", Texture.class);
		addTexture("bulletHit",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/protectionBubble.png",Texture.class);
		addTexture("protectionBubble",newTexture,TextureFilter.Linear,TextureFilter.Linear);
	
		newTexture = assetManager.get("data/world/turret.png",Texture.class);
		addTexture("turret",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/enemies/Elite.png",Texture.class);
		addTexture("Elite",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/enemies/eliteturret.png",Texture.class);
		addTexture("eliteTurret",newTexture,TextureFilter.Linear,TextureFilter.Linear);

		newTexture = assetManager.get("data/world/turretbase.png",Texture.class);
		addTexture("turretBase",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/world/enemybullet.png",Texture.class);
		addTexture("sniperBullet",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		newTexture = assetManager.get("data/world/enemybullet2.png",Texture.class);
		addTexture("eliteBullet",newTexture,TextureFilter.Linear,TextureFilter.Linear);
		
		//Background textures
		newTexture = assetManager.get("data/world/star.png",Texture.class);
		addTexture("star",newTexture,TextureFilter.Linear,TextureFilter.Linear);
	}
	
	/**
	 * Swap the drawing order of two images
	 * @param name1
	 * @param name2
	 */
	public void swapDrawns(String name1, String name2)
	{
		keys.swap(keys.indexOf(name1, false), keys.indexOf(name2, false));
	}
	
	public void dispose()
	{
		//batch.dispose();
		assetManager.unload("data/world/planetfinal.png");
		textures.clear();	
		
		
	}
	
	public void setPoints(float[] points)
	{
		this.points = points;
	}
	public SpriteBatch getSpriteBatch()
	{
		return batch;
	}
}
