package dev.ramar.e2.structures;


import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.interfaces.rendering.Renderable;

import dev.ramar.e2.structures.hitboxes.*;
import dev.ramar.e2.interfaces.events.listeners.HitListener;


import java.util.*;

public class HitBox implements Renderable
{
	private static final double SHAPES_FIDELITY = 1.0;

	public static HitManager hitManager;

	public static void setHitManager(HitManager hm)
	{
		hitManager = hm;
	} 


	/* --- mandatory --- */
	private Vec2 pos = new Vec2(0, 0);
	private HitShape boundingBox;
	private List<HitShape> hitbox = new ArrayList<>();

	private List<HitListener> listeners = new ArrayList<>();


	public HitBox(HitShape boundingBox)
	{
		this.boundingBox = boundingBox;
	}

	public HitBox(HitShape boundingBox, double x, double y)
	{
		this.boundingBox = boundingBox;
		this.pos.set(x, y);
	}


	public HitBox(HitShape boundingBox, Vec2 pos)
	{
		this.boundingBox = boundingBox;
		this.pos.set(pos);
	}


	public HitBox(HitBox hb)
	{
		this.listeners = new ArrayList<>(hb.listeners);
		this.boundingBox = hb.boundingBox.clone();

		this.pos = hb.pos.clone();

		for( HitShape hs : hb.hitbox)
			hitbox.add(hs.clone());

	}


	public HitBox clone()
	{
		return new HitBox(this);
	}


	public void setPos(Vec2 tPos)
	{
		this.pos.set(tPos);
	}


	public void addHitListener(HitListener hl)
	{
		listeners.add(hl);
	}

	public void removeHitListener(HitListener hl)
	{
		listeners.remove(hl);
	}


	public void addToHitBox(HitShape h)
	{
		hitbox.add(h);
		onHitBoxChanged();
	}


	public boolean pointWithin(Vec2 p)
	{
		boolean isWithin = false;

		for( HitShape h : hitbox )
		{
			isWithin = h.pointInsideOff(pos, p);
			if( isWithin )
				break;
		}

		return isWithin;
	}

	private Vec2 memVec = new Vec2(0, 0);

	public boolean collides(Hittable h)
	{
		boolean collided = false;

		if( boundingBox.collidesAt(pos, h))
		{
			for( HitShape thisShape : hitbox )
			{
				collided = thisShape.collidesAt(pos, h);
				if( collided )
					break;
			}
		}
		return collided;
	}


	public HitShape getBoundingBox()
	{
		return boundingBox;
	}


	private void onHitBoxChanged()
	{
		// set defining points
		if( definingPoints == null)
			definingPoints = new HashSet<>();
		else
			definingPoints.clear();

	}


	private Set<Vec2> definingPoints;

	public Set<Vec2> getDefiningPoints()
	{
		return definingPoints;
	}


	public List<HitShape> getShapes()
	{
		return hitbox;
	}


	// "offset" in the regards to Hittable really is getPos(), but i didn't
	// change it and have to live with the consequences now
	public Vec2 getPos()
	{
		return pos;
	}


	/* Renderable implementation
	-------------------------------------
	*/

	protected Colour mainColour = new Colour(255, 255, 0, 255);
	protected Colour secondaryColour = new Colour(0, 255, 255, 255);

	public void setMainColour(Colour c)
	{
		mainColour = c;
	}


	public void setMainColour(int r, int g, int b, int a)
	{
		mainColour = new Colour(r, g, b, a);
	}


	public Colour getMainColour()
	{
		return mainColour;
	}


	public Colour getSecondaryColour()
	{
		return secondaryColour;
	}


	public void render(ViewPort vp)
	{
		vp.setColour(mainColour);

		boundingBox.drawSelfOff(pos, vp);

		vp.setColour(secondaryColour);

		for( HitShape h : hitbox )
			h.drawSelfOff(pos, vp);
	}


	public void drawSelf(Vec2 p, ViewPort vp) 
	{
		double x = pos.getX(),
			   y = pos.getY();

		if( p != null )
		{
			x += p.getX();
			y += p.getY();
		}

		vp.setColour(mainColour);

		boundingBox.drawSelfOff(x, y, vp);

		vp.setColour(secondaryColour);

		for( HitShape h : hitbox )
			h.drawSelfOff(x, y, vp);
	}


}

