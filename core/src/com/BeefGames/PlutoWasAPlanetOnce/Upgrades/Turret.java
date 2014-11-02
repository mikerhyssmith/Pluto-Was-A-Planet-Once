package com.BeefGames.PlutoWasAPlanetOnce.Upgrades;

import com.BeefGames.PlutoWasAPlanetOnce.Model.Bullet;
import com.BeefGames.PlutoWasAPlanetOnce.Model.Enemy;
import com.BeefGames.PlutoWasAPlanetOnce.View.World;
import com.BeefGames.PlutoWasAPlanetOnce.View.Handlers.BulletHandler;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Turret extends Upgrade{

        private Circle circleBounds;
        private Vector2 position;
        private float width;
        private float height;
        private BulletHandler bulletHandler;

        private float rotation;
        private Array<Bullet> turretBullets = new Array<Bullet>();
        private int ammo;
        
        private Enemy target;
        private Boolean hasTarget = false;
        private Boolean onTarget = false;
        private float speed;
        private float rotationSpeed;
        
        public Turret(Vector2 position, float width, float height,World world,int ammo) 
        {
                super(position, width, height);
                this.position = position;
                this.width = width;
                this.height = height;
                circleBounds = new Circle(position.x + width/2, position.y + height/2, 1000);
                bulletHandler = new BulletHandler(world);
                rotation =0;
                this.ammo = ammo;
                speed = 2000;
        }

        public boolean isAlive()
        {
                return isAlive;
        }

        /**
         * This method controls the turret rotation
         */
        public void rotate()
        {
        	if(rotation >360)
        	{
        		rotation -= 360;
        	}
        	//Vector between turret and its target
        	Vector2 turretToEnemy = new Vector2(target.getPosition().x + target.getWidth()/2 - (position.x+ width/2),
        			target.getPosition().y + target.getHeight()/2 - (position.y+ height/2));
        	//Angle that the turret wants to have
        	float requiredAngle = turretToEnemy.angle();
        	//Work out desired rotation speed
        	rotationSpeed = speed/turretToEnemy.len();
        	//Alter rotation if it is not correct
        	if((rotation+90) <requiredAngle)
        		rotation += rotationSpeed;
        	if((rotation+90) > requiredAngle)
        		rotation -= rotationSpeed;
        	
        	if(Math.abs((rotation+90) - requiredAngle) <= rotationSpeed)
        	{	//If the turret is roughly facing the right way, let it shoot
        		onTarget = true;
        	}
        	else
        	{
        		onTarget = false;
        	}
        	
        }
        
        
        public void Fire(float x, float y)
        {
        	if(onTarget)
        	{
                if(ammo >0)
                {	//only shoot if the turret is facing the right way and has ammo
                        bulletHandler.Fire(x, y,this);
                }
        	}
        }
        
        public double calculateAngle(double x, double y)
        {
                double tempValue = Math.atan2(x,y);
                return tempValue; 
        }

        public void addBullet(Bullet b){
                turretBullets.add(b);
                
        }
        

        public Circle getCircleBounds()
        {
                return circleBounds;
        }
        public Vector2 getPostiion()
        { 
                return position;
        }
        public float getWidth()
        {
                return width;
        }
        public float getHeight()
        {
                return height;
        }
        public void setRotation(float r)
        {
                rotation = r;
        }
        public float getRotation(){
                return rotation;
        }
        public Array<Bullet> getBullets(){
                
                return turretBullets;
        }
        public int getAmmo(){
        	return ammo;
        }
        public void setAmmo(int ammo){
        	this.ammo = ammo;
        }
        public Enemy getTarget()
        {
        	return target;
        }
        public void setTarget(Enemy target)
        {
        	this.target = target;
        }
        public Boolean getHasTarget()
        {
        	return hasTarget;
        }
        public void setHasTarget(Boolean target)
        {
        	hasTarget = target;
        }
}