package dev.ramar.e2.demos.combat;


import dev.ramar.utils.HiddenList;
import dev.ramar.utils.PairedValues;

import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

public class DeltaUpdater
{
	private static final DeltaUpdater singleton = new DeltaUpdater();
	public static DeltaUpdater getInstance()
	{   return DeltaUpdater.singleton;   }


    public final UpdatableList<Updatable> toUpdate = new UpdatableList<>(); 

    public interface Updatable
    {
        //     v returns if it should be removed
        public boolean update(double delta);
    }

    public class UpdatableList<E> extends HiddenList<E>
    {
        private List<PairedValues<E, Boolean>> toParse = new LinkedList<>();

        private List<E> getList()
        {
            return this.list; 
        }

        public void queueAdd(E e)
        {
            synchronized(this)
            {
                toParse.add(new PairedValues(e, true));
            }
        }

        public void queueRemove(E e)
        {
            synchronized(this)
            {
                toParse.add(new PairedValues(e, false));
            }
        }
    }


    private Thread t;

    public DeltaUpdater()
    {
    	
    }


    public void start()
    {
    	if( this.t == null || !this.t.isAlive() )
    	{
	    	this.t = this.makeThread();
	    	this.t.start();
    	}
    }

    public void interrupt()
    {
    	this.t.interrupt();
    }

    public void close() throws InterruptedException
    {
    	this.t.interrupt();
    	while(this.t.isAlive()) 
    	{
    		Thread.sleep(1);
    	}
    }

    public Thread makeThread()
    { 
    	return new Thread(() ->
    	{
	        try
	        {
	            long lastTime = System.currentTimeMillis();
	            while(true)
	            {
	                long nowTime = System.currentTimeMillis();
	                double delta = (nowTime - lastTime) / 1000.0; 	
	                // START UPDATING

	                // parse the Updatable requests in toParse
	                // then update everyone in toUpdate 
			        synchronized(toUpdate)
			        {
			            UpdatableList<Updatable> list = (UpdatableList<Updatable>)toUpdate;

			            for( PairedValues<Updatable, Boolean> vs : list.toParse )
			            {
			                if( vs.getV() )
			                    list.getList().add(vs.getK());
			                else
			                    list.getList().remove(vs.getK());
			            }

			            list.toParse.clear();
                        
			            ListIterator<Updatable> iter = list.getList().listIterator();
			            while(iter.hasNext())
			            {
			                Updatable u = iter.next();
			                boolean remove = u.update(delta);
			                if( remove )
			                    iter.remove();

			            }
			        }

	                // END UPDATING
	                lastTime = nowTime;
	                Thread.sleep(1);
	            }
	        }   
	        catch(InterruptedException e)
	        {
                System.out.println("??" + e.getMessage());
                e.printStackTrace();
            }
    	});
    }
}