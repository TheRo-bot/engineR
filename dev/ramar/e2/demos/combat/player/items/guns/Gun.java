package dev.ramar.e2.demos.combat.player.items.guns;

import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

import dev.ramar.e2.demos.combat.player.Item;

import dev.ramar.e2.demos.combat.Player;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import java.util.List;
import java.util.ArrayList;

public class Gun extends Item
{
	public static final List<ViewPort> viewports = new ArrayList<>();

	public GunStats stats;

	private int ammo = 0;
	private int clipSize = 0;

	private Player player;

	public Gun(Player player)
	{
		super();
		this.stats = new GunStats();
		this.player = player;
	}

	public Gun(Player player, GunStats gs)
	{
		super();
		this.player = player;
		this.stats = gs;
	}

	@Override
	protected void setup_actions()
	{
		this.actions.add(new Action()
		{
			public String getName()
			{   return "gun:shoot";   }

			public boolean act(ActionManager am, Object... o)
			{
			 	boolean acted = false;
			 	if( o != null && o.length > 1 )
			 	{
				 	double xPos = (double)o[0];
				 	double yPos = (double)o[1];
				 	Gun.this.noBlocking_shoot(xPos, yPos);
			 	}
			 	return acted;
			}
		});

		this.actions.add(new Action()
		{
			public String getName()
			{   return "gun:reload";   }

			public boolean act(ActionManager am, Object... o)
			{
				
				return false;
			}
		});
	}


	public void shoot(double x, double y)
	{
		this.actions.blockedRun(this.actions.get("gun:shoot"), x, y);
	}

	class Bullet implements Updatable, Drawable
	{
		double x,  y,
			  xn, yn,
			  // units per second
			  vel;
		double timeToLive = 1.0;
		public Bullet(double x, double y, double xn, double yn, double vel)
		{
			this.x   = x;
			this.y   = y;
			this.xn  = xn;
			this.yn  = yn;
			this.vel = vel;
		}

		public boolean update(double delta)
		{	
			this.timeToLive -= delta;
			boolean stop = this.timeToLive < 0;

			if( !stop )
			{
				x += xn * vel * delta;
				y += yn * vel * delta;
			}
			else
			{
				for( ViewPort vp : viewports )
					vp.draw.stateless.perm.queueRemove(this);
			}

			return stop;
		}

		private long lastTime = 0;
		public void drawAt(double x, double y, ViewPort vp)
		{
			vp.draw.stateless.rect.withMod()
				.withColour(255, 255, 255, 255)
				.withFill()
				.withOffset(this.x + x, this.y + y)
			;

			vp.draw.stateless.rect.poslen(-2, -2, 4, 4);

			vp.draw.stateless.line.withMod()
				.withColour(0, 255, 255, 255)
				.withOffset(this.x + x, this.y + y)
			;

			if( lastTime == 0 )
				lastTime = System.currentTimeMillis();

			long currTime = System.currentTimeMillis();

			double delta = (currTime - lastTime) / 1000.0;
			lastTime = currTime;
			vp.draw.stateless.line.pospos(0, 0, this.xn * vel * delta, this.yn * vel * delta);
		}
	}

	private void noBlocking_shoot(double x, double y)
	{
		double xDist = x - this.player.getX();
		double yDist = y - this.player.getY();

		double abs = Math.sqrt(xDist * xDist + yDist * yDist);

		double xNorm = xDist / abs;
		double yNorm = yDist / abs;

		Bullet b = new Bullet(this.player.getX(), this.player.getY(), xNorm, yNorm, this.stats.velocity);
		DeltaUpdater.getInstance().toUpdate.queueAdd(b);
		for( ViewPort vp : Gun.viewports )
			vp.draw.stateless.perm.add(b);
	}

	public void reload()
	{
		
	}

	private void noBlocking_reload()
	{

	}


	/* Item implemenation
	--===-------------------
	*/

	public void use()
	{

	}


	public void act(String s, Object... o)
	{
		switch(s)
		{
			case "reload":
				this.reload();
				break;
		}
	}

	/* Subclasses
	--===-----------
	*/

	public class GunStats
	{

		private static int    DEFAULT_CLIP_SIZE = 30;
		private static double DEFAULT_RELOAD_TIME = 1.0;
		private static double VELOCITY = 3000;

		public int clipSize = GunStats.DEFAULT_CLIP_SIZE;
		public GunStats withClipSize(int cs)
		{
			this.clipSize = cs;
			return this;
		}


		public double reloadTime = GunStats.DEFAULT_RELOAD_TIME;
		public GunStats withReloadTime(double rt)
		{
			this.reloadTime = rt;
			return this;
		}


		public double velocity = GunStats.VELOCITY;
		public GunStats withClipSize(double v)
		{
			this.velocity = v;
			return this;
		}


		public GunStats() {}
	}
}