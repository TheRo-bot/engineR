package dev.ramar.e2.structures.renderables.entities;


import dev.ramar.e2.backend.Moment;

import dev.ramar.e2.interfaces.updating.SlowUpdater;
import dev.ramar.e2.interfaces.updating.SUpdateManager;
import dev.ramar.e2.structures.StaticInfo;


import dev.ramar.e2.structures.Vec2;


public class SlowEntity extends WorldEntity implements SlowUpdater
{


	public SlowEntity(double x, double y, double xv, double yv)
	{
		super(new Vec2(x, y), new Vec2(xv, yv));
		initialise();
	}


	protected SlowEntity(Vec2 pos, Vec2 vel)
	{
		super(pos, vel);
	}


	public SlowEntity(SlowEntity se)
	{
		super((WorldEntity)se);
	}

	public SlowEntity clone()
	{
		SlowEntity se = new SlowEntity(this);
		se.initialise();
		return se;
	}


	/* SlowUpdater implementation
	--------------------------------
	*/	

	protected boolean doKill = false;


	public void startUpdating()
	{
		// consoleOutput("startUpdating");
		StaticInfo.Objects.getSUpdateManager().addSUpdater(this);
	}


	public void update(Moment m)
	{
		doControl();
	}


	public boolean kill()
	{
		return doKill;
	}


	public void onKill()
	{
		if( getChunk() != null )
			getChunk().removeLayerable(this);
	}


}