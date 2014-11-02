package com.BeefGames.PlutoWasAPlanetOnce.View.Handlers;

import java.io.IOException;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ParticleHandler {
        
        ParticleEmitter shipHit,newShipHit,eliteSpawn,newEliteSpawn,planetDestroy,newPlanetDestroy,protection,newProtection,eliteExhaust,newEliteExhaust,turret,newTurret,sniperExhaust,newSniperExhaust,bombTrails,newBombTrails,followerExhaust,newFollowerExhaust,shipCrash,newShipCrash,explosion,planetExplosion,newPlanetExplosion,exhaust,shot,nuke,bulletHit,newBullet,newExplosion,newNuke;
        Sprite circle,explosionCircleSprite,nukeSprite,bulletSprite;
        WorldRenderer worldRenderer;
        Ship ship;

        
        public ParticleHandler(WorldRenderer worldRenderer,Ship ship){
                
                this.worldRenderer = worldRenderer;
                this.ship = ship;
                exhaust = new ParticleEmitter();
                explosion = new ParticleEmitter();
                bulletHit = new ParticleEmitter();
                nuke = new ParticleEmitter();
                shipCrash = new ParticleEmitter();
                planetExplosion = new ParticleEmitter();
                followerExhaust = new ParticleEmitter();
                bombTrails = new ParticleEmitter();
                sniperExhaust = new ParticleEmitter();
                turret = new ParticleEmitter();
                eliteExhaust = new ParticleEmitter();
                protection = new ParticleEmitter();
                planetDestroy = new ParticleEmitter();
                eliteSpawn = new ParticleEmitter();
                shipHit = new ParticleEmitter();
                load();
                circle = new Sprite(worldRenderer.getTexture("shipexhaust"));
                exhaust.setSprite(circle);
                planetExplosion.setSprite(circle);
                shipCrash.setSprite(circle);
                followerExhaust.setSprite(circle);
                bombTrails.setSprite(circle);
                turret.setSprite(circle);
                explosionCircleSprite = new Sprite(worldRenderer.getTexture("explosion"));
                nukeSprite = new Sprite(worldRenderer.getTexture("nuke"));
                bulletSprite = new Sprite(worldRenderer.getTexture("bulletHit"));
                nuke.setSprite(nukeSprite);
                exhaust.getScale().setHigh(10);
                exhaust.start();
                worldRenderer.addDrawn("exhaust", exhaust);
        }
        
        public void load(){
                
                try {
                		shipHit.load(Gdx.files.internal("data/effect/shipBulletHit").reader(2024));
                        exhaust.load(Gdx.files.internal("data/effect/exhaustupdated91").reader(2024));
                        explosion.load(Gdx.files.internal("data/explosion4").reader(2024));
                        nuke.load(Gdx.files.internal("data/nuke2").reader(2024));
                        bulletHit.load(Gdx.files.internal("data/shipbullethit2").reader(2024));
                        planetExplosion.load(Gdx.files.internal("data/planetHit").reader(2024));
                        shipCrash.load(Gdx.files.internal("data/enemyCrash").reader(2024));
                        followerExhaust.load(Gdx.files.internal("data/enemyExhaust").reader(2024));
                        bombTrails.load(Gdx.files.internal("data/planetScout").reader(2024));
                        sniperExhaust.load(Gdx.files.internal("data/SniperExhaust").reader(2024));
                        eliteExhaust.load(Gdx.files.internal("data/effect/elite").reader(2024));
                        protection.load(Gdx.files.internal("data/effect/protection").reader(2024));
                        turret.load(Gdx.files.internal("data/effect/turret").reader(2024));
                        eliteSpawn.load(Gdx.files.internal("data/effect/eliteSpawn").reader(2024));
                        planetDestroy.load(Gdx.files.internal("data/effect/planetDestroy").reader(2024));
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        
        /**
         * Creates a new explosion
         * @param e
         */
        public void addCrash(Enemy e)
        {
                newShipCrash  = new ParticleEmitter(shipCrash);
                
                newShipCrash.setSprite(explosionCircleSprite);
                
                worldRenderer.addDrawn("newShipCrash"+newShipCrash.hashCode(),"explosion",new Vector2(e.getPosition().x,e.getPosition().y),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newShipCrash"+newShipCrash.hashCode(), false);
                
                explosionCircleSprite = new Sprite(worldRenderer.getTexture("explosion"));
                newShipCrash.setSprite(explosionCircleSprite);
                //newExplosion.getScale().setHigh(0.3f);
                newShipCrash.setPosition(e.getPosition().x + e.getWidth()/2,e.getPosition().y+e.getHeight()/2);
                newShipCrash.start();
                
                worldRenderer.addDrawn("newShipCrash"+newShipCrash.hashCode(), newShipCrash);
        }
        public void addTurretRemoval(float x,float y){
        
                newTurret = new ParticleEmitter(turret);
                
                newTurret.setSprite(explosionCircleSprite);
                
                worldRenderer.addDrawn("newTurret"+newTurret.hashCode(),"explosion",new Vector2(x,y),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newTurret"+newTurret.hashCode(), false);
               
                newTurret.getScale().setHigh(15f);
                newTurret.setPosition(x ,y);
                newTurret.start();
                
                worldRenderer.addDrawn("newTurret"+newTurret.hashCode(), newTurret);
        
        }
        public void addPlanetDestruction(float x,float y){
            
            newPlanetDestroy = new ParticleEmitter(planetDestroy);
            
            newPlanetDestroy.setSprite(explosionCircleSprite);
            
            worldRenderer.addDrawn("newPlanetDestroy"+newPlanetDestroy.hashCode(),"explosion",new Vector2(x,y),new Vector2(0, 0),
                            new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            false, false, true);
            
            worldRenderer.setVisible("newPlanetDestroy"+newPlanetDestroy.hashCode(), false);
           
           // planetDestroy.getScale().setHigh(15f);
            newPlanetDestroy.setPosition(x ,y);
            newPlanetDestroy.start();
            
            worldRenderer.addDrawn("newPlanetDestroy"+newPlanetDestroy.hashCode(), newPlanetDestroy);
    
    }
        public void addExplosion(Enemy e){
                newExplosion  = new ParticleEmitter(explosion);
                
                newExplosion.setSprite(explosionCircleSprite);
                newExplosion.getScale().setHigh(10f);
                
                worldRenderer.addDrawn("newExplosion"+newExplosion.hashCode(),"explosion",new Vector2(e.getPosition().x,e.getPosition().y),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newExplosion"+newExplosion.hashCode(), false);
                
                explosionCircleSprite = new Sprite(worldRenderer.getTexture("explosion"));
                newExplosion.setSprite(explosionCircleSprite);
                //newExplosion.getScale().setHigh(0.3f);
                newExplosion.setPosition(e.getPosition().x + e.getWidth()/2,e.getPosition().y+e.getHeight()/2);
                newExplosion.start();
                
                worldRenderer.addDrawn("newExplosion"+newExplosion.hashCode(), newExplosion);
        }
        
        public void addNuke(Ship ship){
                
                newNuke = new ParticleEmitter(nuke);
                newNuke.setSprite(nukeSprite);
                worldRenderer.addDrawn("newNuke"+newNuke.hashCode(),"nuke",new Vector2(ship.getPosition().x + ship.getWidth()/2,ship.getPosition().y + ship.getHeight()/2),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("nuke").getWidth(), worldRenderer.getTexture("nuke").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("nuke").getWidth(), worldRenderer.getTexture("nuke").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newNuke"+newNuke.hashCode(), false);
                nukeSprite = new Sprite(worldRenderer.getTexture("explosion"));
                newNuke.setSprite(nukeSprite);
                newNuke.getScale().setHigh(100);
                newNuke.setPosition(ship.getPosition().x + ship.getHeight()/2,ship.getPosition().y + ship.getHeight()/2);
                newNuke.start();
                
                worldRenderer.addDrawn("newNuke"+newNuke.hashCode(), newNuke);
        }
        
        public void addBulletHit(Bullet b){
                newBullet = new ParticleEmitter(bulletHit);
                
                newBullet.setSprite(bulletSprite);
                //newBullet.getScale().setHigh(10f);
                
                worldRenderer.addDrawn("newBullet"+newBullet.hashCode(),"explosion",new Vector2(b.getPosition().x + b.getWidth()/2,b.getPosition().y + b.getHeight()),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("bulletHit").getWidth(), worldRenderer.getTexture("bulletHit").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("bulletHit").getWidth(), worldRenderer.getTexture("bulletHit").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newBullet"+newBullet.hashCode(), false);
                
                bulletSprite = new Sprite(worldRenderer.getTexture("bulletHit"));
                newBullet.setSprite(bulletSprite);
                //newExplosion.getScale().setHigh(0.3f);
                newBullet.setPosition(b.getPosition().x + b.getWidth()/2,b.getPosition().y + b.getHeight());

                newBullet.getAngle().setLow(b.getRotation() + 270);
                newBullet.getAngle().setHighMin(b.getRotation() + 270 - 45);
                newBullet.getAngle().setHighMax(b.getRotation() + 270 + 45);
                
                newBullet.start();
                
                worldRenderer.addDrawn("newExplosion"+newBullet.hashCode(), newBullet);
                
                
        }
        
        public void addShipHit(Bullet b){
            newShipHit = new ParticleEmitter(shipHit);
            
            newShipHit.setSprite(bulletSprite);
            //newBullet.getScale().setHigh(10f);
            
            worldRenderer.addDrawn("newShipHit"+newShipHit.hashCode(),"explosion",new Vector2(b.getPosition().x + b.getWidth()/2,b.getPosition().y + b.getHeight()),new Vector2(0, 0),
                            new Vector2(worldRenderer.getTexture("bulletHit").getWidth(), worldRenderer.getTexture("bulletHit").getHeight()),
                            new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("bulletHit").getWidth(), worldRenderer.getTexture("bulletHit").getHeight()),
                            false, false, true);
            
            worldRenderer.setVisible("newShipHit"+newShipHit.hashCode(), false);
            
            
            //newExplosion.getScale().setHigh(0.3f);
            newShipHit.setPosition(b.getPosition().x + b.getWidth()/2,b.getPosition().y + b.getHeight());
            newShipHit.getAngle().setLow(b.getRotation() + 270);
            newShipHit.getAngle().setHighMin(b.getRotation() + 270 - 45);
            newShipHit.getAngle().setHighMax(b.getRotation() + 270 + 45);
            
            newShipHit.start();
            
            worldRenderer.addDrawn("newShipHit"+newShipHit.hashCode(), newShipHit);
            
            
    }
        
        public void addPlanetHit(Enemy e){
                newPlanetExplosion  = new ParticleEmitter(planetExplosion);
                
                newPlanetExplosion.setSprite(explosionCircleSprite);
                newPlanetExplosion.getScale().setHigh(10f);
                
                worldRenderer.addDrawn("newExplosion"+newPlanetExplosion.hashCode(),"explosion",new Vector2(e.getPosition().x,e.getPosition().y),new Vector2(0, 0),
                                new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                                false, false, true);
                
                worldRenderer.setVisible("newExplosion"+newPlanetExplosion.hashCode(), false);
                
                explosionCircleSprite = new Sprite(worldRenderer.getTexture("explosion"));
                newPlanetExplosion.setSprite(explosionCircleSprite);
                //newExplosion.getScale().setHigh(0.3f);
                newPlanetExplosion.setPosition(e.getPosition().x + e.getWidth()/2,e.getPosition().y+e.getHeight()/2);
                newPlanetExplosion.start();
                
                worldRenderer.addDrawn("newExplosion"+newPlanetExplosion.hashCode(), newPlanetExplosion);
                
                
        }
        
        public void addEliteSpawn(Enemy e){
            newEliteSpawn  = new ParticleEmitter(eliteSpawn);
            
            newEliteSpawn.setSprite(explosionCircleSprite);
           newEliteSpawn.getScale().setHigh(15f);
            
            worldRenderer.addDrawn("newEliteSpawn"+newEliteSpawn.hashCode(),"explosion",new Vector2(e.getPosition().x,e.getPosition().y),new Vector2(0, 0),
                            new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            false, false, true);
            
            worldRenderer.setVisible("newEliteSpawn"+newEliteSpawn.hashCode(), false);
            
            //newExplosion.getScale().setHigh(0.3f);
            newEliteSpawn.setPosition(e.getPosition().x + e.getWidth()/2,e.getPosition().y+e.getHeight()/2);
            newEliteSpawn.start();
            
            worldRenderer.addDrawn("newEliteSPawn"+newEliteSpawn.hashCode(), newEliteSpawn);
            
            
    }
        
        public void addExhaust(Enemy e)
        {
        	if(e.getType() == "Follower" || e.getType() == "Soldier")
        	{
                newFollowerExhaust = new ParticleEmitter(followerExhaust);
                newFollowerExhaust.setSprite(circle);
                newFollowerExhaust.setPosition(e.getPosition().x + e.getWidth() / 2+e.getWidth()/4,e.getPosition().y + e.getHeight() / 2);
                newFollowerExhaust.getScale().setHigh(10f);
                e.setEmitter(newFollowerExhaust);
                worldRenderer.addDrawn("newFollowerExhaust"+e.hashCode(), newFollowerExhaust);
                newFollowerExhaust.start();
        	}
        	else if(e.getType() == "Elite")
        	{
                newEliteExhaust = new ParticleEmitter(eliteExhaust);
                newEliteExhaust.setSprite(circle);
                newEliteExhaust.setPosition(e.getPosition().x + e.getWidth() / 2+e.getWidth()/4,e.getPosition().y + e.getHeight() / 2);
                e.setEmitter(newEliteExhaust);
               // newEliteExhaust.getScale().setHigh(60f);
                worldRenderer.addDrawn("newEliteExhaust"+e.hashCode(), newEliteExhaust);
    			
                worldRenderer.swapDrawns("ship", "newEliteExhaust"+e.hashCode());
    			worldRenderer.swapDrawns("enemyturret"+ e.hashCode(), "exhaust");
    			worldRenderer.swapDrawns("enemy" + e.hashCode(), "enemyturret"+e.hashCode());

            	worldRenderer.swapDrawns("enemy" + e.hashCode(), "newEliteExhaust"+e.hashCode());

                newEliteExhaust.start();
        	}
        	else if(e.getType() == "PlanetScout")
        	{
                newBombTrails = new ParticleEmitter(bombTrails);
                newBombTrails.setSprite(circle);
                newBombTrails.setPosition(e.getPosition().x + e.getWidth() / 2+e.getWidth()/4,e.getPosition().y + e.getHeight() / 2);
                //newFollowerExhaust.getScale().setHigh(10f);
                e.setEmitter(newBombTrails);
                worldRenderer.addDrawn("newBombTrails"+e.hashCode(), newBombTrails);
                newBombTrails.start();
        	}
        	else if(e.getType() == "Sniper")
        	{
                newSniperExhaust = new ParticleEmitter(sniperExhaust);
                newSniperExhaust.setSprite(circle);
				newSniperExhaust.setPosition(e.getPosition().x +e.getWidth()/2 - circle.getWidth(),e.getPosition().y + e.getHeight()/4 + circle.getHeight()/2);
                newSniperExhaust.getScale().setHigh(12f);
                e.setEmitter(newSniperExhaust);
                worldRenderer.addDrawn("newSniperExhaust"+e.hashCode(), newSniperExhaust);
                newSniperExhaust.start();
        	} 
        }
        
        public void addProtection(float x ,float y){
        	newProtection = new ParticleEmitter(protection);
            
            newProtection.setSprite(explosionCircleSprite);
            
            worldRenderer.addDrawn("newProtection"+newProtection.hashCode(),"explosion",new Vector2(x,y),new Vector2(0, 0),
                            new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            new Vector2(1, 1),0,new Vector2(0, 0),new Vector2(worldRenderer.getTexture("explosion").getWidth(), worldRenderer.getTexture("explosion").getHeight()),
                            false, false, true);
            
            worldRenderer.setVisible("newProtection"+newProtection.hashCode(), false);
           
           newProtection.getScale().setHigh(15f);
            newProtection.setPosition(x ,y);
            newProtection.start();
            
            worldRenderer.addDrawn("newProtection"+newProtection.hashCode(), newProtection);
            
    }
        public void updateParticles()
        {
        	
        	float degAngle = ship.getRotation();
			float angle = -((degAngle)/360) * 2 * (float)Math.PI;
			
			float x1 =- 3*ship.getHeight()/8 * (float)Math.sin(angle);
			float y1 =- 3*ship.getHeight()/8 * (float)Math.cos(angle);
			
			
			Vector2 position = new Vector2(ship.getPosition().x + ship.getWidth()/2 +x1,
					ship.getPosition().y + ship.getHeight()/2 + y1) ;
			exhaust.setPosition(position.x, position.y);
			//exhaust.getAngle().setHigh(ship.getRotation());
			//exhaust.getAngle().setLow(ship.getRotation());
            /*    worldRenderer.updateDrawn("shipexhaust",new Vector2(ship.getPosition().x + ship.getWidth() / 2,
                                ship.getPosition().y + ship.getHeight() / 2),new Vector2(0, 0), new Vector2(worldRenderer.getTexture("shipexhaust").getWidth() / 10,worldRenderer.getTexture("shipexhaust")
                                .getHeight() / 10), new Vector2(1, 1),0);
*/
               // exhaust.setPosition(ship.getPosition().x + ship.getWidth() / 2,ship.getPosition().y + ship.getHeight() / 2);
                
                //set exhaust rotation
               // float angle = ship.getRotation();
               // exhaust.getAngle().setLow(angle);
               // exhaust.getAngle().setHigh(angle);
                
                
                worldRenderer.setEmitter("exhaust", exhaust);
        }
        
        public void stopAll(){
                if(newExplosion != null){
                newExplosion.reset();
                }
                exhaust.reset();
                
                if(newNuke != null){
                newNuke.reset();
                }
                
        }
        public void startExhaust(){
                exhaust.start();
        }

}