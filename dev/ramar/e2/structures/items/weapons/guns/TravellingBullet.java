package dev.ramar.e2.structures.items.weapons.guns;



import dev.ramar.e2.backend.Moment;

import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.HitBox;
import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.structures.hitboxes.Group;

import dev.ramar.e2.structures.renderables.entities.Tier1_FastEntity;

import dev.ramar.utils.Timer;

import java.util.*;
import java.io.*;
/*
Class: TravellingBullet
 - A SlowEntity which defines the exact nature of every normal
   bullet in this game, functions as a self-replicator, so TravellingBullet.clone();
   will return an exact copy. This should be used for a Gun object,
   which upon firing, clones a "calibre" TravellingBullet, which creates the
   actual bullet which will be fired
*/
public class TravellingBullet extends Bullet
{


	private static class EVENTS
	{
		public static final String TIME_TO_LIVE = "TIME TO LIVE";
	}

	@Override
	protected void initialise()
	{
		super.initialise();
	}


	protected Double stopSpeed = null;
	protected Integer timeToLive = null;


	public TravellingBullet(int layer, String name, HitBox hb, double x, double y, double xv, double yv)
	{
		super(layer, name, hb, new Vec2(x, y), new Vec2(xv, yv));
		initialise();
	}


	protected TravellingBullet(int layer, String name, HitBox hb, Vec2 pos, Vec2 vel)
	{
		super(layer, name, hb, pos, vel);
	}


	public TravellingBullet(int layer, String name, HitShape hs, double x, double y, double xv, double yv)
	{
		super(layer, name, new HitBox(hs, x, y), new Vec2(x, y), new Vec2(xv, yv));
		initialise();
	}


	protected TravellingBullet(TravellingBullet nb)
	{
		super(nb);

		this.stopSpeed = nb.stopSpeed;
		this.timeToLive = nb.timeToLive;
	}


	public TravellingBullet clone()
	{
		TravellingBullet b = new TravellingBullet(this);
		b.initialise();
		return b;
	}



	private List<Vec2> renderPositions = java.util.Collections.synchronizedList(new ArrayList<>());


	/* Overridden methods
	------------------------
	*/

	@Override
	public void event(String name)
	{
		super.event(name);
		switch(name)
		{
			case EVENTS.TIME_TO_LIVE:
				doKill = true;
				break;
		}
	}


	@Override
	public Vec2 getDrag()
	{
		return drag;
	}


	@Override
	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
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

		super.drawSelf(p, vp);

		Colour origColour = vp.getColour();


		vp.setColour(secondaryColour);
		// System.out.println("!!!" + renderPositions.size());
		synchronized(renderPositions)
		{
			renderPositions.add(pos);
			vp.drawSpline(renderPositions);
			renderPositions.remove(renderPositions.size() - 1);
		}

		vp.setColour(origColour);
	}


	private boolean firstUpdate = true;

	private double timeToNextPos = 0.01275;

	@Override
	public void update(double delta)
	{
		super.update(delta);

		if( firstUpdate )
		{
			firstUpdate = false;
			renderPositions.add(new Vec2(pos));

			if( timeToLive != null )
				wait(timeToLive, EVENTS.TIME_TO_LIVE);
		}


		timeToNextPos -= delta;
		if( timeToNextPos <= 0 )
		{
			renderPositions.add(new Vec2(pos));
			timeToNextPos = 0.01275;
		}

		// do collision from [lastX, lasY] to getPos() instead of just our position


		if( stopSpeed != null )
		{
			// if we've stopped (dictacted by stopSpeed) then cease
			if( Math.abs(vel.getX()) <= stopSpeed && Math.abs(vel.getY()) <= stopSpeed )
				doKill = true;
		}

		lastX = getXPos();
		lastY = getYPos();
	}


	@Override
	public boolean kill()
	{
		return doKill;
	}


	@Override
	public void onKill()
	{
		super.onKill();
		StaticInfo.Objects.getRenderManager().removeRenderable(this);
	}


	@Override
	public void setPos(Vec2 p)
	{
		super.setPos(p);
		if( hitbox != null )
			hitbox.setPos(pos);
	}


	private double lastX = 0.0,
				   lastY = 0.0;


	@Override
	public void doCollision()
	{
		if( hitbox != null )
		{
			double xDist = getXPos() - lastX,
				   yDist = getYPos() - lastY;
			
			for( Group g : groups )
				g.doCollision(this);
		}
		else
			colliding = false;

		lastX = getXPos();
		lastY = getYPos();

	}
}





