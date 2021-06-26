package dev.ramar.e2.backend.updaters;


import dev.ramar.e2.backend.Moment;


import dev.ramar.e2.interfaces.events.producers.TimerManager;
import dev.ramar.e2.interfaces.updating.SUpdateManager;
import dev.ramar.e2.interfaces.updating.SlowUpdater;

import dev.ramar.e2.structures.Timer;

import java.util.*;
import java.util.concurrent.*;


public class SlowUpdateManager implements SUpdateManager
{
	private static final int MAX_BLOCKING_ARRAY_SIZE = 1000;


	private double ups = 60.0;
	private Moment moment;

	public SlowUpdateManager(double ups)
	{
		this.ups = ups;
		moment = new Moment(ups);
	}





	/* SUpdateManager implementation
	-----------------------------------
	*/

	private List<SlowUpdater> updaters = new ArrayList<>(999999);

	private ArrayBlockingQueue<SlowUpdater> toAdd = new ArrayBlockingQueue<SlowUpdater>(MAX_BLOCKING_ARRAY_SIZE);
	private ArrayBlockingQueue<SlowUpdater> toRemove = new ArrayBlockingQueue<SlowUpdater>(MAX_BLOCKING_ARRAY_SIZE);

	private boolean checkComms = false;

	private Thread thread;
	private boolean paused = false;

	public boolean addSUpdater(SlowUpdater su)
	{
		// incase something *potentially* weird happens and toAdd throws
		// an exception, checkComms doesn't get set
		boolean expB = toAdd.offer(su);
		checkComms = true;
		return expB;
	}


	public boolean removeSUpdater(SlowUpdater su)
	{
		boolean expB = toRemove.offer(su);
		checkComms = true;
		return expB;
	}

    public boolean hasSUpdater(SlowUpdater su)
    {
    	return updaters.contains(su);
    }


    public void waitForClose()
    {
    	try
    	{
    		if( thread != null )
	    		thread.join();
    	}
    	catch(InterruptedException e) {}
    }


	public void start()
	{
		thread = new Thread(this::threadMain, "Slow Update Thread");
		thread.start();
	}

	public void pause()
	{
		paused = true;
	}


	public void unpause()
	{
		paused = false;
	}


	public void shutdown()
	{
		thread.interrupt();
	}


	public void threadMain()
	{
		System.out.println("SUpdateThread start");
		long test = System.currentTimeMillis();
		long lastTime = System.nanoTime();

		double ns = 1000000000 / ups;

		int updateCount = 0; 

		double delta = 0;
		long timer = System.currentTimeMillis();

		int frames = 0;
		long accumulativeTime = 0;


		while(! thread.isInterrupted() )
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while( delta >= 1)         
			{

				if(! paused )
				{
					accumulativeTime += update();
					moment.step();
					updateCount++;
					if( updateCount % (int)ups == 0)
					{
						long test2 = System.currentTimeMillis();
						System.out.println("[SU] Updates in the last " + (test - test2) + "ms: " + updateCount + " mspt: " + (accumulativeTime / ups));
						updateCount = 0;
						accumulativeTime = 0;
						test = test2;
					}
				}


				delta--;
			}
		}
		System.out.println("SUpdateThread end");

	}


	private List<SlowUpdater> deadUpdaters = new ArrayList<>(9999);

	private long update()
	{
		long startTime = System.currentTimeMillis();

		if( checkComms )
		{


			SlowUpdater su = toAdd.poll();
			while(su != null )
			{
				updaters.add(su);
				su = toAdd.poll();
			}

			toAdd.clear();

			su = toRemove.poll();
			while(su != null )
			{
				updaters.remove(su);
				su = toRemove.poll();
			}

			toRemove.clear();


			checkComms = false;
		}

		for( SlowUpdater thisSUpdater : updaters )
		{
			thisSUpdater.update(moment);			
			if( thisSUpdater.kill() )
				deadUpdaters.add(thisSUpdater);
		}


		for( SlowUpdater su : deadUpdaters )
			su.onKill();

		updaters.removeAll(deadUpdaters);
		deadUpdaters.clear();		

		long endTime = System.currentTimeMillis();
		// System.out.println("update (took " + (endTime - startTime) + " ms)");
		return endTime - startTime;
	}



}