package dev.ramar.e2.backend.updaters;


import dev.ramar.e2.interfaces.updating.*;

import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.utils.Timer;


import java.util.*;
import java.util.concurrent.*;

public class FastUpdateManager implements FUpdateManager
{
	private static final String INTERNAL_THREAD_NAME = "Fast Update Thread";


	public FastUpdateManager()
	{

	}


	/* FUpdateManager implementation
	-----------------------------------
	*/

	private Thread thread;

	public Set<FastUpdater> fupdaters = new HashSet<>();

	public ArrayBlockingQueue<FastUpdater> toAdd = new ArrayBlockingQueue<FastUpdater>(999);
	public ArrayBlockingQueue<FastUpdater> toRemove = new ArrayBlockingQueue<FastUpdater>(999);


	public void addFUpdater(FastUpdater fu)
	{
		toAdd.offer(fu);
	}


	public void removeFUpdater(FastUpdater fu)
	{
		toRemove.offer(fu);
	}


	public boolean hasFUpdater(FastUpdater fu)
	{
		return fupdaters.contains(fu);
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
		thread = new Thread(this::threadMain, INTERNAL_THREAD_NAME);
		thread.start();
	}

	public void shutdown()
	{
		thread.interrupt();
	}

	// fastupdaters that request death
	private List<FastUpdater> deathRow = new ArrayList<>();

	private void threadMain()
	{
		double lastTime = System.nanoTime(), delta, currTime;

		int updatesInSec = 0;
		double deltaToSecond = 0;
		while(! thread.isInterrupted())
		{
	
			currTime = System.nanoTime();
			delta = (currTime - lastTime) / StaticInfo.Constants.DELTA_SECOND;
			deltaToSecond += delta;

			lastTime = currTime;

			FastUpdater thisFu = toAdd.poll();

			while( thisFu != null )
			{
				fupdaters.add(thisFu);
				thisFu = toAdd.poll();
			}

			thisFu = toRemove.poll();
			while( thisFu != null )
			{
				fupdaters.remove(thisFu);
				thisFu = toRemove.poll();
			}


			toRemove.clear();


			updatesInSec++;
			Timer.update();

			deathRow.clear();

			int index = 0;
			for( FastUpdater fu : fupdaters )
			{
				fu.update(delta);
				if( fu.kill() )
					deathRow.add(fu);

				index++;
			}

			fupdaters.removeAll(deathRow);

			for( FastUpdater fu : deathRow )
			{
				fu.onKill();
			}


			double test = StaticInfo.Constants.DELTA_SECOND / 10 / StaticInfo.Constants.DELTA_SECOND;

			// if deltaToSecond added up to a second
			if( deltaToSecond > 1.0 )
			{
				deltaToSecond = 0;

				System.out.println("[FU] Updated " + fupdaters.size() + " in the last second: " + updatesInSec);

				updatesInSec = 0;

			}

		}
	}
}