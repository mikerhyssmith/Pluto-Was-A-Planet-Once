package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public class PixmapHandler implements Disposable
{
	private class PixmapChange 
	{
		int x, y;
		int width;

		void set(int x, int y, int width)
		{
			this.x = x;
			this.y = y;
			this.width = width;
		}
	}

	
	private Pixmap pixmap;
	private Sprite sprite;
	private Texture texture;

	private Color colour = new Color();
	
	private float radiusFactor = 1f;

	private Array<PixmapChange> mods;
	private Iterator<PixmapChange> changeIter;
	
	private Pixmap renderPixmap32;
	private Pixmap renderPixmap64;
	private Pixmap renderPixmap128;
	private Pixmap renderPixmap256;

	public PixmapHandler(Pixmap pixmap, Sprite sprite, Texture texture)
	{
		this.pixmap = pixmap;
		this.sprite = sprite;
		this.texture = texture;

		mods = new Array<PixmapChange>();
		
		this.renderPixmap32 = new Pixmap(32, 32, Format.RGBA8888);
		this.renderPixmap64 = new Pixmap(64, 64, Format.RGBA8888);
		this.renderPixmap128 = new Pixmap(128, 128, Format.RGBA8888);
		this.renderPixmap256 = new Pixmap(256, 256, Format.RGBA8888);
	}

	
	public PixmapHandler(Pixmap pixmap)
	{
		this.pixmap = pixmap;
		this.texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false));
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.sprite = new Sprite(texture);

		mods = new Array<PixmapChange>();

		this.renderPixmap32 = new Pixmap(32, 32, Format.RGBA8888);
		this.renderPixmap64 = new Pixmap(64, 64, Format.RGBA8888);
		this.renderPixmap128 = new Pixmap(128, 128, Format.RGBA8888);
		this.renderPixmap256 = new Pixmap(256, 256, Format.RGBA8888);
	}

	/**
	 * Projects the coordinates (x, y) to the Pixmap coordinates system and store the result in the specified Vector2.
	 * 
	 * @param position
	 *            The Vector2 to store the transformed coordinates.
	 * @param x
	 *            The x coordinate to be projected.
	 * @param y
	 *            The y coordinate to be prjected
	 */
	public void project(Vector2 position, float x, float y) {
		position.set(x, y);

		float centerX = sprite.getX(); //+ sprite.getOriginX();
		float centerY = sprite.getY();// + sprite.getOriginY();

		position.add(-centerX, -centerY);

		position.rotate(-sprite.getRotation());

		float scaleX = pixmap.getWidth() / sprite.getWidth();
		float scaleY = pixmap.getHeight() / sprite.getHeight();

		position.x *= scaleX;
		position.y *= scaleY;

		position.add( //
				pixmap.getWidth() * 0.5f, //
				-pixmap.getHeight() * 0.5f //
		);

		position.y *= -1f;
	}

	public int getPixel(Vector2 position) {
		return getPixel(position.x, position.y);
	}

	public int getPixel(float x, float y) {
		return pixmap.getPixel((int) x, (int) y);
	}

	public void setPixel(float x, float y, int value) {
		Color.rgba8888ToColor(colour, value);
		pixmap.setColor(colour);
	}

	public void eraseCircle(float x, float y, float radius) 
	{
		float scaleX = pixmap.getWidth() / sprite.getWidth();

		int newRadius = Math.round(radius * scaleX * radiusFactor);

		if (x + newRadius < 0 || y + newRadius < 0)
			return;

		if (x  - newRadius > pixmap.getWidth() || y - newRadius > pixmap.getHeight())
			return;

		Blending blending = Pixmap.getBlending();
		pixmap.setColor(0f, 0f, 0f, 0f);
		Pixmap.setBlending(Blending.None);

		int newX = Math.round(x);
		int newY = Math.round(y);

		pixmap.fillCircle(newX, newY, newRadius);
		Pixmap.setBlending(blending);
		
		PixmapChange change = new PixmapChange();
		change.set(newX, newY, newRadius * 2);

		mods.add(change);
	}

	private Pixmap getPixmapForRadius(int width) {
		if (width <= 32)
			return renderPixmap32;
		if (width <= 64)
			return renderPixmap64;
		if (width <= 128)
			return renderPixmap128;
		return renderPixmap256;
	}

	/**
	 * Updates the opengl texture with all the pixmap modifications.
	 */
	public Texture update() 
	{
		if(mods.size!= 0)
		{
			Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, texture.getTextureObjectHandle());

			int width = pixmap.getWidth();
			int height = pixmap.getHeight();

			changeIter = mods.iterator();
			while(changeIter.hasNext())
			{
				PixmapChange change = changeIter.next();

				Pixmap renderPixmap = getPixmapForRadius(change.width);

				int dstWidth = renderPixmap.getWidth();
				int dstHeight = renderPixmap.getHeight();
				Pixmap.setBlending(Blending.None);
			
				int x = Math.round(change.x) - dstWidth / 2;
				int y = Math.round(change.y) - dstHeight / 2;

				if (x + dstWidth > width)
					x = width - dstWidth;
				else if (x < 0)
					x = 0;

				if (y + dstHeight > height)
					y = height - dstHeight;
				else if (y < 0) {
					y = 0;
				}
			
				renderPixmap.drawPixmap(pixmap, 0, 0, x, y, dstWidth, dstHeight);

				Gdx.gl.glTexSubImage2D(GL20.GL_TEXTURE_2D, 0, x, y, dstWidth, dstHeight, //
						renderPixmap.getGLFormat(), renderPixmap.getGLType(), renderPixmap.getPixels());
			}
			mods.clear();
		}
		return texture;
	}

	/**
	 * Reload all the pixmap data to the opengl texture, to be used after the game was resumed.
	 */
	public void reload() 
	{
		// texture.dispose();
		// if (texture.getTextureObjectHandle() == 0)
		texture.load(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false));
	}

	@Override
	public void dispose() {
		//this.pixmap.dispose();
		//this.texture.dispose();
		this.renderPixmap32.dispose();
		this.renderPixmap64.dispose();
		this.renderPixmap128.dispose();
		this.renderPixmap256.dispose();
	}
	
	/**
	 * Use this to count the number of non-transparent pixels in the stored pixmap
	 * @return
	 */
	public int countPixels()
	{
		int pixels = 0;
		for(int i = 0; i < pixmap.getWidth(); i++)
		{
			for(int j = 0; j < pixmap.getHeight(); j++)
			{	
				Color.rgba8888ToColor(colour, pixmap.getPixel(i, j));  
				
				if(colour.a != 0)
				{
					pixels ++;
				}
			}
		}
		
		return pixels;
	}
	
}
