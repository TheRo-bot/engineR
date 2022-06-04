package dev.ramar.e2.demos.combat;

import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.utils.PairedValues;

import java.util.List;
import java.util.ArrayList;

public class Timer implements Updatable
{

    private static Timer singleton = new Timer();
    public static Timer getInstance()
    {   return Timer.singleton;   }



    private List<PairedValues<Double, OnTimerDone>> timers = new ArrayList<>(); 
    public interface OnTimerDone
    {   public void onTimerDone();   }

    public synchronized Timer after(double seconds, OnTimerDone otd)
    {
        this.timers.add(new PairedValues<Double, OnTimerDone>()
            .withK(seconds)
            .withV(otd)
        );

        return this;
    }


    public synchronized boolean update(double delta)
    {
        for( int ii = 0; ii < this.timers.size(); ii++ )
        { 
            PairedValues<Double, OnTimerDone> timer = timers.get(ii);

            double k = timer.getK() - delta;
            if( k <= 0.0 )
            {
                timer.getV().onTimerDone();
                timers.remove(ii);
                ii--;
            }
            else
                timer.setK(k);

        }


        return false;
    }
}