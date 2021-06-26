package dev.ramar.e2.structures.renderables.entities;



import dev.ramar.e2.interfaces.events.listeners.EventListener;

import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitShape;
import dev.ramar.e2.interfaces.rendering.ViewPort;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.HitBox;
import dev.ramar.e2.structures.StaticInfo;


import dev.ramar.e2.structures.hitboxes.Group;


import java.util.*;

/*
Class: Tier1_FastEntity
 - The lowest tier of Entities that have usefulness,
 - Governed by the FastUpdater, these really shouldn't
   have anything complex in them, and if it's necessary to do so,
   pair a FastEntity to an <x>SlowEntity to give the benefits you need
   without making the FastUpdater a SlowUpdater unintentionally

 - Simple entities have the following abilities:
 	- They can be collided with
*/


/*
	- Collidable
	- Can Store/Use Items
	- Can be entirely manipulated by an AI
*/

public class Tier1_FastEntity extends FastEntity implements Hittable, EventListener
{
	protected HitBox hitbox;



	public Tier1_FastEntity(double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
	}


	public Tier1_FastEntity(Vec2 pos, Vec2 vel)
	{
		super(pos, vel);
	}


	public Tier1_FastEntity(HitBox hb, double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
		hitbox = hb;
		hitbox.setPos(pos);
		initialise();
	}


	protected Tier1_FastEntity(HitBox hb, Vec2 p, Vec2 v)
	{
		super(p, v);
		hitbox = hb;
		hitbox.setPos(pos);
	}


	protected Tier1_FastEntity(Tier1_FastEntity cl)
	{
		super(cl);
		if( cl.hitbox != null )
			hitbox = cl.hitbox.clone();

		for( Group g : cl.groups )
		{
			g.addHittable(this);
			this.groups.add(g);
		}
	}


	public Tier1_FastEntity clone()
	{
		Tier1_FastEntity t1 = new Tier1_FastEntity(this);
		t1.initialise();
		return t1;
	}


	/* Overriden methods
	-----------------------
	*/

	@Override
	public void render(ViewPort vp)
	{
		Colour origC = vp.getColour();


		vp.setColour(colliding ? mainColour : secondaryColour);
		int size = 10;
		vp.drawRect(pos.getX() - size/2, pos.getY() - size/2, size, size);

		if( hitbox != null )
			hitbox.render(vp);

		vp.setColour(origC);
	}


	@Override
	public void update(double d)
	{
		super.update(d);

		if( hitbox != null )
			hitbox.setPos(pos);
	}


	@Override
	public void onMove()
	{
		super.onMove();
		doCollision();
	}


	@Override
	public void onKill()
	{
		super.onKill();

		synchronized(groups)
		{
			for( Group g : groups )
				g.removeHittable(this);				
		}

	}


	/* Hittable Implementation
	-----------------------------
	*/

	protected Set<Hittable> colliders = java.util.Collections.synchronizedSet(new HashSet<Hittable>());

	protected List<Group> groups = java.util.Collections.synchronizedList(new ArrayList<Group>());

	protected boolean colliding = false;

	public boolean isColliding()
	{
		return colliding;
	}


	public void doCollision()
	{
		if( hitbox != null )
			for( Group g : groups )
				g.doCollision(this);
		else
			colliding = false;
	}


	public boolean collides(Hittable h)
	{
		if( hitbox == null )
			return false;
		else
			return hitbox.collides(h);
	}


	public void resetCollision()
	{
		colliders.clear();
		colliding = false;
	}


	public boolean pointWithin(Vec2 p)
	{
		if( hitbox != null )
			return hitbox.pointWithin(p);

		return false;
	}


	public HitShape getBoundingBox()
	{
		if( hitbox != null )
			return hitbox.getBoundingBox();

		return null;
	}


	public List<HitShape> getShapes()
	{
		if( hitbox != null )
			return hitbox.getShapes();

		return null;
	}


	public HitManager getHitManager()
	{
		return StaticInfo.Objects.getHitManager();
	}	


	public void registerToGroup(String name)
	{
		getHitManager().registerHittable(this, name);
		groups.add(getHitManager().getGroup(name));
	}


	public void unregisterToGroup(String name)
	{
		getHitManager().unregisterHittable(this, name);
		groups.remove(getHitManager().getGroup(name));
	}


    public boolean collidesWith(String s)
    {
    	synchronized(groups)
    	{
	    	for( Group g : groups )
	    	{
		    	return g.getName().equals(s);
	    	}
    	}
    	return false;
    }


    public boolean collidesWithAny(List<String> s)
    {
    	boolean colliding = false;

    	synchronized(groups)
    	{
    		for(Group g : groups )
    		{
    			colliding = s.contains(g.getName());
    			if( colliding )
    				break;
    		}
    	}

    	return colliding;
    }

	public List<String> getGroupNames()
	{
		List<String> list = new ArrayList<>();

		for( Group g : groups )
			list.add(g.getName());

		return list;
	}


	public Vec2 getPos()
	{
		return pos;
	}


	protected void colliderUpdate()
	{
		colliding = !colliders.isEmpty();
	}


	public void onCollision(Hittable h)
	{
		// consoleOutput("ONCOLLISION: " + this + " | " + h);		
		colliders.add(h);
		colliderUpdate();
	}


	public void onNoCollision(Hittable h)
	{
		colliders.remove(h);
		colliderUpdate();
	}

}