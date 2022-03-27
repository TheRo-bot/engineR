package dev.ramar.e2.demos.combat.player.guns;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.demos.combat.Anchor;


import dev.ramar.e2.demos.combat.player.guns.GunActions.AimListener;
import dev.ramar.e2.demos.combat.player.guns.GunActions.ReloadListener;

public abstract class Gun implements Drawable, AimListener, ReloadListener
{
	public Gun()
	{
		this.actions = new GunActions();
		this.stats = new GunStats();
		this.actions.listeners.aim.add(this);

	}


	protected Gun(boolean makeFields)
	{
		if( makeFields )
		{
			this.actions = new GunActions();
			this.stats = new GunStats();
			this.actions.listeners.aim.add(this);
			this.actions.listeners.reload.add(this);
		}
		else
		{
			this.actions = null;
			this.stats = null;
		}
	}



	/* Class-Fields
	--===-------------
	*/

	/*
	Class-Field: clip
	 - how many shots the gun do be having left doe
	*/
	protected int clip = 0;
	public void setClip(int c)
	{
		this.clip = c;
	}

	public GunActions actions;
	public Gun withActions(GunActions ga)
	{
		this.actions = ga;
		return this;
	}



	public GunStats stats;
	public Gun withStats(GunStats gs)
	{
		this.stats = gs;
		return this;
	}



	protected Anchor anchor = null;
	public void setAnchor(Anchor an)
	{
		this.anchor = an;
	}

	public Gun withAnchor(Anchor an)
	{
		this.setAnchor(an);
		return this;
	}

	/* ReloadListener Implementation
	--===------------------------------
	*/

	public void onReload()
	{

	}


	/* AimListener implementation
	--===------------------------------------
	*/

	protected Vec2 target = new Vec2();

	public void onAim(double x, double y)
	{
		this.target.set(x, y);
	}



	/* Gun Interface
	--===--------------
	*/

	/*
	Method: aim
	 - for when the user wants to aim
	*/
	public void aim(double x, double y)
	{
		this.actions.manager.blockedRun(this.actions.aim, x, y);
	}



	/* Drawable Implementation
	--===------------------------
	*/

	public void drawAt(double x, double y, ViewPort vp)
	{
		vp.draw.stateless.line.withMod()
			.withColour(255, 0, 0, 125)
			.withOffset(x, y)
			.withThickness(1)
		;

		// find the normal vector of the line {anchor.getX(), anchor.getY()} -> {this.x, this.y}

		double xDist = this.target.getX() - this.anchor.getX(),
			   yDist = this.target.getY() - this.anchor.getY();

		double hyp = Math.sqrt(xDist * xDist + yDist * yDist);

		double xNorm = xDist / hyp,
			   yNorm = yDist / hyp;

		xDist = xNorm * vp.window.width();
		yDist = yNorm * vp.window.width();

		vp.draw.stateless.line.poslen(this.anchor.getX(), this.anchor.getY(), xDist, yDist);
	}


}