package dev.ramar.e2.structures.items.weapons;


import dev.ramar.utils.Timer;

import dev.ramar.e2.structures.items.weapons.guns.*;

import dev.ramar.e2.interfaces.events.listeners.GunListener;
import dev.ramar.e2.interfaces.events.listeners.EventListener;
import dev.ramar.e2.interfaces.events.listeners.ItemHolder;

import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class Gun extends Weapon implements EventListener
{

	protected static class Constants
	{
		private static final String RELOADING = "Reloading";
		private static final String SHOT_GAP = "Shot Gap";
		private static final String INOPERABLE = "Inoperable";
		private static final String IWANTITTO = "I want it to";
	}

	protected int maxAmmo;

	protected int clip;

	protected double force;

	// in updates, how long does it take for the gun to work
	// from selecting it
	protected int loadTime;

	// in updates
	protected int reloadTime;
	// in updates
	protected int timeBetweenShots;

	// in degrees
	protected double spread;

	private Set<String> blockedReasons = new HashSet<>();

	private Bullet bullet;

	protected void initialise()
	{

	}

	public Gun(String name, Bullet bullet)
	{
		super(name);
		this.bullet = bullet;
		initialise();
	}

	/* EventListener Implementation
	----------------------------------
	*/


    public void event(String eventName)
    {

    }


	public void timerComplete(String name)
	{
		switch(name)
		{
			case Constants.RELOADING:
				clip = maxAmmo;
				break;
		}
		
		blockedReasons.remove(name);
	}


	public int getMaxAmmo()
	{
		return maxAmmo;
	}

	public int getClip()
	{
		return clip;
	}

	public double getForce()
	{
		return force;
	}

	public int getLoadTime()
	{
		return loadTime;
	}

	public int getReloadTime()
	{
		return reloadTime;
	}

	public int getTimeBetweenShots()
	{
		return timeBetweenShots;
	}

	public double getSpread()
	{
		return spread;
	}


	public String toString()
	{
		return name;
	}


	public void reload()
	{
		if(! blockedReasons.contains(Constants.RELOADING) )
		{
			blockedReasons.add(Constants.RELOADING);
			Timer.wait(reloadTime, Constants.RELOADING, this);
		}
	}


	public void setBulletColour(int r, int g, int b, int a)
	{
		bullet.setSecondaryColour(r, g, b, a);
	}


	protected void shoot(GunListener gl)
	{
		// if we can shoot
		if( clip > 0 && blockedReasons.isEmpty() )
		{
			// shoot gun
			gl.fireShot(bullet.bulletFactory());
			// do things to make gun shoot realistic
			clip--;
			Timer.wait(timeBetweenShots, Constants.SHOT_GAP, this);
			blockedReasons.add(Constants.SHOT_GAP);
		}
		else if( clip == 0 )
			reload();
	}


	@Override
	public void mainUse(ItemHolder il)
	{
		if(! (il instanceof GunListener) )
			throw new IllegalArgumentException("ItemHolder can't shoot a gun! " + il + " needs to be a GunListener");

		shoot((GunListener)il);
	}


	public void secondUse()
	{
		// idk lmao
	}


	public void drawSelf(Vec2 pos, ViewPort vp)
	{
		double x = 0.0,
			   y = 0.0;

		if( pos != null )
		{
			x += pos.getX();
			y += pos.getY();
		}

		vp.drawText("" + clip + " / " + maxAmmo, x, y);
	}

}