package com.BeefGames.PlutoWasAPlanetOnce.Tokens;

import java.util.Iterator;
import java.util.Random;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.AudioHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TokenManager {

	private Array<Token> tokenArray;
	private Iterator<Token> tokenIterator;
	private WorldRenderer worldRenderer;
	private Token token;
	private Random random;
	private AudioHandler audioHandler;
	
	
	public TokenManager(WorldRenderer worldRenderer,World world)
	{
		tokenArray = new Array<Token>();
		this.worldRenderer = worldRenderer;
		random = new Random();
		audioHandler = world.getAudioHandler();
	}
	
	public void addToken(Vector2 position, Ship ship, Vector2 velocity)
	{
		int randomToken = random.nextInt(3);

		float giveToken = random.nextFloat();
		 
		if(giveToken > 0.7)
		{
			if(randomToken == 0){
				
				Texture armour = worldRenderer.getTexture("armourtoken");
				
				ArmourToken at = new ArmourToken(new Vector2(position.x - armour.getWidth()/2,position.y - armour.getHeight()/2),
						armour.getWidth(), armour.getHeight(),ship,0, velocity);
		
				worldRenderer.addDrawn("token"+at.hashCode(), "armourtoken", at.getPosition(),
						new Vector2(at.getWidth()/2, at.getHeight()/2),	new Vector2(at.getWidth(), at.getHeight()),
						new Vector2(1,1), 0, new Vector2(0,0), new Vector2(armour.getWidth(), armour.getHeight()),
						false, false, true);
		
				tokenArray.add(at);
			}
			else if(randomToken == 1 || randomToken == 2)
			{
				Texture health = worldRenderer.getTexture("healthtoken");
				
				HealthToken ht = new HealthToken(new Vector2(position.x - health.getWidth()/2,position.y - health.getHeight()/2),
						health.getWidth(), health.getHeight(),ship,0, velocity);
			
				worldRenderer.addDrawn("token"+ht.hashCode(), "healthtoken", ht.getPosition(),
						new Vector2(ht.getWidth()/2, ht.getHeight()/2),	new Vector2(ht.getWidth(), ht.getHeight()),
						new Vector2(1,1), 0, new Vector2(0,0), new Vector2(health.getWidth(), health.getHeight()),
						false, false, true);
				tokenArray.add(ht);
			}
			else 
			{
				Texture money =  worldRenderer.getTexture("moneytoken");
				
				MoneyToken mt = new MoneyToken(new Vector2(position.x - money.getWidth()/2,position.y - money.getHeight()/2),
						money.getWidth(), money.getHeight(),ship,0, velocity);
			
				worldRenderer.addDrawn("token"+mt.hashCode(), "moneytoken", mt.getPosition(),
						new Vector2(mt.getWidth()/2,mt.getHeight()/2),	new Vector2(mt.getWidth(), mt.getHeight()),
						new Vector2(1,1), 0, new Vector2(0,0), new Vector2(money.getWidth(), money.getHeight()),
						false, false, true);
			
				tokenArray.add(mt);
			
			}
		}
	}
	
	public void update()
	{
		tokenIterator = tokenArray.iterator();
		while(tokenIterator.hasNext()){
			token = tokenIterator.next();
			token.update();
			worldRenderer.updateDrawn("token"+token.hashCode(), token.getPosition(), new Vector2(token.getWidth()/2,
					token.getHeight()/2), new Vector2(token.getWidth(),token.getHeight()), new Vector2(1,1), token.getRotation());
			
			if(token.checkCollect())
			{
				tokenIterator.remove();
				audioHandler.pickupToken();
				worldRenderer.removeDrawn("token"+token.hashCode());
			}
		}
	}
	
	public Array<Token> getTokens()
	{
		return tokenArray;
	}
}
