package com.BeefGames.PlutoWasAPlanetOnce.Upgrades;

import java.util.Iterator;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Ship;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.WorldRenderer;
import com.BeefGames.PlutoWasAPlanetOnce.Wave.WaveManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Nuke extends Upgrade {

        private World world;
        Array<Enemy> enemies ;
        Iterator<Enemy> eIter;
        WorldRenderer wr;
        Enemy e;
        Texture planetTexture;
        WaveManager wm;
        private int level;
        private Circle nukeRadius;
        
        public Nuke(Vector2 position, float width, float height, World world) {
                super(position, width, height);
                
                this.world = world;
                wm = world.getManager();
                
                enemies = wm.getenemies();
                wr = world.getRenderer();
                planetTexture = wr.getTexture("planet");
                nukeRadius = new Circle(new Vector2(position.x + width/2,position.y + height/2),20);
        }
        
        public void detonate(Ship ship){
                eIter = enemies.iterator();
                world.setActionBeginTime(TimeUtils.nanoTime());
                nukeRadius.set(ship.getPosition().x + ship.getWidth()/2, ship.getPosition().y + ship.getHeight()/2, 500);
                
                while(eIter.hasNext()){
                        
                        e = eIter.next();
                        
                        if(nukeRadius.contains(new Vector2(e.getPosition().x + e.getWidth()/2,e.getPosition().y + e.getHeight()/2))){
                                
                                wr.removeDrawn("enemy"+ e.hashCode());
                                wr.removeDrawn("newFollowerExhaust"+e.hashCode());
        						wr.removeDrawn("newBombTrails"+e.hashCode());
        						wr.removeDrawn("newSniperExhaust"+e.hashCode());
                                
                                eIter.remove();
                        }
                           
                }       
        }
        public void setLevel(int level){
                
                this.level = level;
                if(level == 0){
                        

                }
                else if (level ==1){
                        
                }
                else if (level ==2){
                        
                        
                }
                
        }
        public int getLevel(){
                return level;
        }
        public boolean isAlive(){
                
                return isAlive;
        }
        public void purchase(){
                
                
        }
        
        
}