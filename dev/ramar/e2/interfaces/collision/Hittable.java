package dev.ramar.e2.interfaces.collision;



import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.interfaces.events.listeners.HitListener;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.hitboxes.HitRect;
import dev.ramar.e2.structures.hitboxes.Group;

import dev.ramar.e2.interfaces.rendering.Layerable;

import java.util.List;
import java.util.Set;



/*
Interface: Hittable
 - Interface for anything which wants to be Collidable,
   To do Collision:
   	1. Create a Hittable object
   	2. registerToGroup(some name)
   	3. Use getHitManager().getGroup(some name) to modify the
   	   Group to whitelist/blacklist specific names to collide with
   	4. register other groups to the HitManager with other names
   	   for your object to collide with 
*/
public interface Hittable extends Layerable
{
	public void doCollision();

	public boolean collides(Hittable h);
	
	public void onCollision(Hittable h);

	public void onNoCollision(Hittable h);

	public void resetCollision();

	public boolean pointWithin(Vec2 p);

	public HitShape getBoundingBox();

	public List<HitShape> getShapes();

	public HitManager getHitManager();

	public void registerToGroup(String name);

	public void unregisterToGroup(String name);

	public List<String> getGroupNames();

    public boolean collidesWith(String s);

    public boolean collidesWithAny(List<String> s);

	public void drawSelf(Vec2 centerPoint, ViewPort vp);



}