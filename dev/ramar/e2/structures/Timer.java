package dev.ramar.e2.structures;


import dev.ramar.e2.interfaces.updating.FastUpdater;
import dev.ramar.e2.interfaces.events.producers.TimerManager;

import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.interfaces.events.listeners.TimerListener;
import dev.ramar.e2.backend.Moment;

import java.util.List;
import java.util.ArrayList;

public class Timer implements FastUpdater
{
	private long timeToComplete;
	// describes this timer, identifies this timer
	private String description;
	// the callback for when the timer finishes
	private List<TimerListener> listeners = new ArrayList<>();

	private boolean paused = false;

	public Timer(long ttc, String description, TimerListener... listeners)
	{
		timeToComplete = ttc;
		this.description = description;

		for( TimerListener tl : listeners )
			this.listeners.add(tl);

		completeDelta = (double)(timeToComplete) / 1000;
	}



	public String getDescription()
	{   return description;   }


	private double dToComplete = 0;

	public void update(double delta)
	{
		if(! paused )
			dToComplete += delta;
	}

	private double completeDelta;


	public boolean kill()
	{
		boolean exp = false;
		if( dToComplete >= completeDelta )
		{
			exp = true;
			// System.out.println("timer " + description + " complete (" + dToComplete + " >= " + completeDelta );
			for( TimerListener tl : listeners )
				tl.timerComplete(this);
		}

		return exp;
	}


	public void onKill() 
	{
	}


	public void start()
	{
		StaticInfo.Objects.getFUpdateManager().addFUpdater(this);
	}

	public void pause()
	{
		paused = true;
	}

	public void unpause()
	{
		paused = false;
	}


	public void restart()
	{
		dToComplete = 0;
		start();
	}
}

