package dev.ramar.e2.demos.combat.hitboxes;

import dev.ramar.utils.PairedValues;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;

public class HitManager
{
    public HitManager()
    {

    }


    private Map<String, List<Hitter>> hitLayers = new HashMap<>();

    private Set<PairedValues<PairedValues<String, Hitter>, Boolean>> toProc = new HashSet<>();

    public synchronized void queueAdd(String channel, Hitter hit)
    {
        if( hit != null && this.hitLayers.get(channel) != null )
        {
            toProc.add(new PairedValues<PairedValues<String, Hitter>, Boolean>()
                .withK(new PairedValues<String, Hitter>()
                    .withK(channel)
                    .withV(hit)
                )
                .withV(true)
            );
        }
    }

    public synchronized void queueRemove(String channel, Hitter hit)
    {
        if( hit != null && this.hitLayers.get(channel) != null )
        {
            toProc.add(new PairedValues<PairedValues<String, Hitter>, Boolean>()
                .withK(new PairedValues<String, Hitter>()
                    .withK(channel)
                    .withV(hit)
                )
                .withV(false)
            );
        }
    }

    public synchronized void add(String channel, Hitter hit)
    {
        List<Hitter> li = this.hitLayers.get(channel);
        if( li == null ) 
        {
            li = new LinkedList<>();
            this.hitLayers.put(channel, li);
        }
        li.add(hit);
    }

    public synchronized boolean remove(String channel, Hitter hit)
    {
        boolean out = false;

        List<Hitter> li = this.hitLayers.get(channel);
        if( li != null )
            out = li.remove(hit);

        return out;
    }


    // private double procInterval = 0.25,
    //                delta = 0.0;
    // public boolean update(double delta)
    // {
    //     if( this.delta <= 0.0 )
    //     {
    //         this.delta = procInterval;
    //         this.proc();
    //     }

    //     return false;
    // }


    public synchronized void proc(String fr, String to)
    {
        List<Hitter> li_fr = this.hitLayers.get(fr),
                     li_to = this.hitLayers.get(to);

        this.proc(li_fr, li_to);
    }

    public synchronized void proc(List<Hitter> fr, String to)
    {
        this.proc(fr, this.hitLayers.get(to));
    }

    public synchronized void proc(String fr, List<Hitter> to)
    {
        this.proc(this.hitLayers.get(fr), to);
    }

    public synchronized void proc(List<Hitter> fr, List<Hitter> to)
    {

        for( PairedValues<PairedValues<String, Hitter>, Boolean> proc : this.toProc )
        {
            if( proc.getV() )
                this.add(proc.getK().getK(), proc.getK().getV());
            else
                this.remove(proc.getK().getK(), proc.getK().getV());
        }
        toProc.clear();

        if( fr != null && to != null )
        {
            for( Hitter hit_to : to )
            {
                if( hit_to != null )
                {
                    synchronized(hit_to)
                    {
                        hit_to.onClear();
                        for( Hitter hit_fr : fr )
                        {
                            if( hit_fr.box != null && hit_to.box != null )
                            {
                                if( hit_fr.box.collidesWith(hit_to.box) )
                                {
                                    hit_to.onHit(hit_fr);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
