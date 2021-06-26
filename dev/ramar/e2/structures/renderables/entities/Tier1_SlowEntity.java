package dev.ramar.e2.structures.renderables.entities;


import dev.ramar.e2.backend.Moment;

import dev.ramar.e2.interfaces.updating.SlowUpdater;
import dev.ramar.e2.interfaces.updating.SUpdateManager;
import dev.ramar.e2.structures.StaticInfo;


import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.interfaces.events.listeners.ActionListener;
import dev.ramar.e2.interfaces.events.producers.Controller;

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
Class: Tier1_SlowEntity
 - The lowest tier of Entities that have usefulness,
 - Governed by the SlowUpdater, these can have complex things
   in them, as update happens at a constant rate governed by the slowupdater,
   so it'll take time before intensive things get noticed

 - Tier1 entities have the following abilities:
 	- They can be collided with
*/


/*
	- Collidable
	- Can Store/Use Items
	- Can be entirely manipulated by an AI
*/

public class Tier1_SlowEntity extends SlowEntity implements Hittable
{
	protected Vec2 drag = new Vec2(0.98);
	protected HitBox hitbox;

	public Tier1_SlowEntity(double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
	}


	public Tier1_SlowEntity(Vec2 pos, Vec2 vel)
	{
		super(pos, vel);
	}


	public Tier1_SlowEntity(HitBox hb, double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
		hitbox = hb;
	}


	public Tier1_SlowEntity(HitBox hb, Vec2 p, Vec2 v)
	{
		super(p, v);
		hitbox = hb;
	}

	protected Tier1_SlowEntity(Tier1_SlowEntity cl)
	{
		super(cl);
		drag.set(cl.drag);

		if( cl.hitbox != null )
			hitbox = cl.hitbox.clone();
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
	public void drawSelf(Vec2 p, ViewPort vp)
	{
		double x = getXPos(),
			   y = getYPos();

		if( p != null )
		{
			x += p.getX();
			y += p.getY();
		}




		Colour origC = vp.getColour();

		vp.setColour(colliding ? mainColour : secondaryColour);
		int size = 10;
		vp.drawRect(x - size/2, y - size/2, size, size);

		if( hitbox != null )
			hitbox.drawSelf(p, vp);

		vp.setColour(origC);
	}


	@Override
	public void update(Moment m)
	{
		super.update(m);

		setPos(pos.add(vel.multiply(drag)));

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
		for( Group g : groups )
			g.removeHittable(this);
	}


	/* Hittable Implementation
	-----------------------------
	*/

	private Set<Hittable> colliders = java.util.Collections.synchronizedSet(new HashSet<Hittable>());

	private List<Group> groups = java.util.Collections.synchronizedList(new ArrayList<Group>());

	private boolean colliding = false;


	public boolean isColliding()
	{
		return colliding;
	}

	public void doCollision()
	{
		if( hitbox != null )
			for( Group g : groups )
				g.doCollision(this);
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


	public List<String> getGroupNames()
	{
		List<String> list = new ArrayList<>();

		for( Group g : groups )
			list.add(g.getName());

		return list;
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


	public Vec2 getPos()
	{
		return pos;
	}


	private void colliderUpdate()
	{
		colliding = !colliders.isEmpty();
	}


	public void onCollision(Hittable h)
	{
		colliders.add(h);
		colliderUpdate();
	}


	public void onNoCollision(Hittable h)
	{
		colliders.remove(h);
		colliderUpdate();
	}

}