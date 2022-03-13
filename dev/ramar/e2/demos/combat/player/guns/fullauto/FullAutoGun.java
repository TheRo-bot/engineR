package dev.ramar.e2.demos.combat.player.guns.fullauto;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.demos.combat.Anchor;
import dev.ramar.e2.demos.combat.RegisteredER2Instances;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.player.guns.Gun;
import dev.ramar.e2.demos.combat.player.guns.Bullet;

import dev.ramar.e2.demos.combat.actions.ActionManager;

import dev.ramar.e2.demos.combat.player.guns.fullauto.FullAutoActions.StartShootingListener;
import dev.ramar.e2.demos.combat.player.guns.fullauto.FullAutoActions.StopShootingListener;

import java.util.List;
import java.util.ArrayList;

public class FullAutoGun extends Gun implements StartShootingListener, StopShootingListener
{
	public FullAutoActions actions = new FullAutoActions();
	public FullAutoStats stats = new FullAutoStats();

	public FullAutoActions getGunActions()
	{   return this.actions;   }


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








	/* StartShootingListener implementation
	--===------------------------------------
	*/
	private double updateDelta = 0;

	private ActionManager madeUsStart = null;

	private Updatable toUpdate = (double delta) ->
	{
		this.updateDelta -= delta;

		if( this.updateDelta < 0 )
		{
			this.updateDelta = 1.0 / FullAutoGun.this.stats.getFireRate();

			double xD = this.x - this.anchor.getX(),
				   yD = this.y - this.anchor.getY();

			double abs = Math.sqrt(xD * xD + yD * yD);

			double xS = xD / abs * this.stats.getVelocity();
			double yS = yD / abs * this.stats.getVelocity();

			Bullet b = new Bullet(this.anchor.getX(), this.anchor.getY(), xS, yS);
			DeltaUpdater.getInstance().toUpdate.queueAdd(b);

			for( EngineR2 er : RegisteredER2Instances.getInstance().instances )
				er.viewport.draw.stateless.perm.queueAdd(b);
		}


		boolean stop = madeUsStart != null && madeUsStart.isBlocked(FullAutoGun.this.actions.startShooting);
		return stop;
	};

	public void onStart(ActionManager am, boolean blocked)
	{
		this.updateDelta = 0.0;
		madeUsStart = am;

		DeltaUpdater.getInstance().toUpdate.queueAdd(this.toUpdate);
	}


	/* StopShootingListener implementation
	--===------------------------------------
	*/

	public void onStop(ActionManager am)
	{
		madeUsStart = null;
		DeltaUpdater.getInstance().toUpdate.queueRemove(this.toUpdate);

		// the updater should remove itself if madeUsStart is empty
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




}