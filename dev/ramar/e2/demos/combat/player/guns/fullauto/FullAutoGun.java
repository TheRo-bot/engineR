package dev.ramar.e2.demos.combat.player.guns.fullauto;

import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.demos.combat.Anchor;
import dev.ramar.e2.demos.combat.RegisteredER2Instances;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.player.guns.Gun;
import dev.ramar.e2.demos.combat.player.guns.Bullet;

import dev.ramar.e2.demos.combat.player.guns.active_reloads.FullAutoReload;

import dev.ramar.e2.demos.combat.actions.ActionManager;

import dev.ramar.e2.demos.combat.player.guns.fullauto.FullAutoActions.StartShootingListener;
import dev.ramar.e2.demos.combat.player.guns.fullauto.FullAutoActions.StopShootingListener;

import java.util.List;
import java.util.ArrayList;

import java.util.Random;

public class FullAutoGun extends Gun implements StartShootingListener, StopShootingListener
{
	public FullAutoActions actions = new FullAutoActions();
	public FullAutoStats stats = new FullAutoStats();

	public FullAutoActions getGunActions()
	{   return this.actions;   }


	private FullAutoReload currReload = null;

	public FullAutoGun()
	{
		super(false);
		super
		    .withActions(this.actions)
		    .withStats(this.stats)
		;

		this.actions.listeners.aim.add(this);
		this.actions.listeners.shooting.start.add(this);
		this.actions.listeners.shooting.stop.add(this);
	}


	private Random rd = new Random();



	/* StartShootingListener implementation
	--===------------------------------------
	*/

	private Bullet shoot(double x, double y)
	{
		Bullet out = null;
		if( this.clip > 0 ) 
		{
			/*
			Strategy for: shooting
			 - Calculate the distance vector from anchor -> target
			*/


			// get the vector of <x, y> -> <anchor>
			double xD = this.target.getX() - this.anchor.getX(),
				   yD = this.target.getY() - this.anchor.getY();


			// normalise the vector
			double abs = Math.sqrt(xD * xD + yD * yD);
			double xN = xD / abs;
			double yN = yD / abs;


			// give v <velocity> distance
			double xV = xN * this.stats.getVelocity() * this.stats.getTimeToLive();
			double yV = yN * this.stats.getVelocity() * this.stats.getTimeToLive();

			// modify the final velocity by the "dinner plate" concept:
			// - generate a random normal vector
			// - apply that vector to v, 

			double rang = rd.nextInt(360);

			// double dist = (double) rd.nextInt((int)(30 * (1 + totalShootDelta * 2) - 10));
			double dist = (this.stats.getSpread() * this.stats.getTimeToLive());
			double timeMod = ((1 + this.totalShootDelta) * this.stats.getSpreadModifier());

			dist *= timeMod;

			double rxN = Math.cos(rang) * dist;
			double ryN = Math.sin(rang) * dist;

			xV += rxN;
			yV += ryN;

			double abs2 = Math.sqrt(xV * xV + yV * yV);

			xV /= abs2;
			yV /= abs2;

			xV *= this.stats.getVelocity() * this.stats.getTimeToLive();
			yV *= this.stats.getVelocity() * this.stats.getTimeToLive();
			out = new Bullet(this.anchor.getX(), this.anchor.getY(), xV, yV);
			this.clip--;
		}

		return out;
	}

	private double updateDelta = 0;
	private double totalShootDelta = 0;

	private ActionManager madeUsStart = null;

	Updatable cooldown = (double delta) -> 
	{
		this.totalShootDelta -= delta * this.stats.getSpreadReduction();

		return this.totalShootDelta < 0;
	};	


	private Updatable shoot = (double delta) ->
	{
		this.updateDelta -= delta;
		totalShootDelta += delta;
		if( this.updateDelta < 0 )
		{
			this.updateDelta = 1.0 / FullAutoGun.this.stats.getFireRate();

			Bullet b = this.shoot(this.anchor.getX(), this.anchor.getY());

			if( b != null )
			{	
				b.setDeceleration(FullAutoGun.this.stats.getDeceleration());

				DeltaUpdater.getInstance().toUpdate.queueAdd(b);

				for( EngineR2 er : RegisteredER2Instances.getInstance().instances )
					er.viewport.draw.stateless.perm.queueAdd(b);
			}

		}

		boolean stop = ! FullAutoGun.this.shooting;

		if( stop ) 
			DeltaUpdater.getInstance().toUpdate.queueAdd(cooldown);

		return stop;
	};

	private boolean shooting = false;

	public void onStart(ActionManager am, boolean blocked)
	{
		this.shooting = true;
		if( ! blocked )
		{
			this.updateDelta = 0.0;
			madeUsStart = am;

			DeltaUpdater du = DeltaUpdater.getInstance();
			du.toUpdate.queueRemove(this.cooldown);
			du.toUpdate.queueAdd(this.shoot);
		}
	}


	/* StopShootingListener implementation
	--===------------------------------------
	*/

	public void onStop(ActionManager am, boolean blocked)
	{
		this.shooting = false;
		if( ! blocked )
		{
			madeUsStart = null;
			DeltaUpdater du = DeltaUpdater.getInstance();
			du.toUpdate.queueAdd(this.cooldown);
			du.toUpdate.queueRemove(this.shoot);
		}
	}

	public void onUnblock(ActionManager am)
	{
		if( this.shooting && !DeltaUpdater.getInstance().toUpdate.contains(this.shoot) )
		{
			DeltaUpdater du = DeltaUpdater.getInstance();

			du.toUpdate.queueRemove(this.cooldown);
			du.toUpdate.add(this.shoot);
		}
	}

	/*
	Method: startShooting
	 - for when the user wants to shoot
	*/
	public void startShooting()
	{
		this.actions.manager.blockedRun(this.actions.startShooting);
	}


	/*
	Method: stopShooting
	 - for when the user wants to stop shooting
	*/
	public void stopShooting()
	{
		this.actions.manager.blockedRun(this.actions.stopShooting);
	}



	/* Overidden Methods
	--===------------------
	*/


	public void onReload()
	{
		System.out.println("onReload!");
	}
}