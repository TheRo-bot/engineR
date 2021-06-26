package dev.ramar.e2.structures.renderables.entities;



import dev.ramar.e2.interfaces.events.listeners.ActionListener;
import dev.ramar.e2.interfaces.events.producers.Controller;

import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.interfaces.events.listeners.ItemHolder;


import dev.ramar.e2.interfaces.events.listeners.GunListener;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.HitBox;
import dev.ramar.e2.structures.StaticInfo;
import dev.ramar.e2.structures.Colour;


import dev.ramar.e2.structures.items.*;
import dev.ramar.e2.structures.items.weapons.*;
import dev.ramar.e2.structures.items.weapons.guns.*;


import dev.ramar.e2.structures.hitboxes.Group;


import java.util.*;

import dev.ramar.e2.backend.renderers.AWTRenderer;

/*
Class: Tier2_FastEntity
 - The second tier of Entities that have usefulness,
 - Governed by the FastUpdater, these really shouldn't
   have anything complex in them, and if it's necessary to do so,
   pair a FastEntity to an <x>SlowEntity to give the benefits you need
   without making the FastUpdater a SlowUpdater unintentionally

 - Tier2 entities have the following abilities:
 	- They can be collided with
 	- They can Store/Use Items
*/


/*
	- Can Store/Use Items
	- Can be entirely manipulated by an AI
*/

public class Tier2_FastEntity extends Tier1_FastEntity implements ItemHolder, GunListener
{

	protected List<Item> items = new ArrayList<>();
	protected Integer selectedItem = null;


	public Tier2_FastEntity(double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
	}


	public Tier2_FastEntity(Vec2 pos, Vec2 vel)
	{
		super(pos, vel);
	}


	public Tier2_FastEntity(HitBox hb, double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
		hitbox = hb;
	}


	public Tier2_FastEntity(HitBox hb, Vec2 p, Vec2 v)
	{
		super(p, v);
		hitbox = hb;
	}


	public Item getHeldItem()
	{
		try
		{
			if( selectedItem != null )
				return items.get(selectedItem);
		}
		catch(IndexOutOfBoundsException e) {}

		return null;
	}


	/* Overriden methods
	-----------------------
	*/

	@Override
	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
	}


	private Vec2 memVec = new Vec2(0);

	@Override
	public void drawSelf(Vec2 p, ViewPort vp)
	{
		Colour origC = vp.getColour();
		super.drawSelf(p, vp);

		double x = getXPos(),
			   y = getYPos();

		if( p != null )
		{
			x += p.getX();
			y += p.getY();

		}

		if( hitbox != null )
			hitbox.drawSelf(p, vp);

		Item heldItem = getHeldItem();
		if( heldItem != null )
		{
			vp.setColour(255, 255, 255, 255);
			memVec.set(x, y);
			heldItem.drawSelf(memVec.add(0, -10), vp);
		}

		vp.setColour(origC);
	}


	@Override
	public void update(double d)
	{
		super.update(d);
	}

	@Override
	protected void leftClick(double delta)
	{
		if( selectedItem != null )
		{
			// consoleOutput("using item " + items.get(selectedItem));
			items.get(selectedItem).use(0, this);
		}
	}


	@Override
	protected void rightClick(double delta)
	{
		if( selectedItem != null )
		{
			items.get(selectedItem).use(1, this);
		}
	}


	@Override
	protected void onAction(String action, double delta)
	{
		super.onAction(action, delta);
		switch(action)
		{
			case StaticInfo.Control.Actions.RELOAD:
				if( selectedItem != null )
				{
					Item thisItem = items.get(selectedItem);
					if( thisItem instanceof Gun )
					{
						((Gun)thisItem).reload();
					}
				}
				break;
		}
	}


	/* Tier2_FastEntity Implementation
	-------------------------------------
	*/


	public void addItem(Item i)
	{
		items.add(i);
	}


	public void holdItem(int i)
	{
		if( 0 >= i && i < items.size() )
			selectedItem = i;
	}


	/* ItemHolder Implementation
	-----------------------------
	*/

	public void itemUseCallback(Item i)
	{}



	/* GunListener Implementation
	-----------------------------
	*/

	protected void shootTo(Bullet b, Vec2 p1, Vec2 p2)
	{
		if( selectedItem != null )
		{
			double distX = p2.getX() - p1.getX();
			double distY = p2.getY() - p1.getY();


			Gun g = ((Gun)items.get(selectedItem));

			Random rd = StaticInfo.Objects.getRandom();

			b.setPos(p1);

			if( distX != 0 || distY != 0 )
			{
	           double angle = Math.acos(distX / Math.sqrt(distX * distX + distY * distY));

	           if( p2.getY() < p1.getY() )
					angle *= -1;

	           angle += g.getSpread() * (rd.nextDouble() * (rd.nextBoolean() ? 1 : -1));

	           double velX = (g.getForce()) * Math.cos(angle);
	           double velY = (g.getForce()) * Math.sin(angle);

	           b.setXVel(velX);
	           b.setYVel(velY);

	           b.givenAngle = angle;
	           b.givenForce = g.getForce();
			}
		}
	}


	public void fireShot(Bullet b)
	{
		shootTo(b, pos, StaticInfo.Objects.getMouseController().getMousePos());

		b.onShoot();
		

	}

}