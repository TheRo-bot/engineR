package dev.ramar.e2.demos.combat.hitboxes;


import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.LinkedList;

public class HitManager
{
    public HitManager()
    {

    }


    private Map<String, List<Hitter>> hitLayers = new HashMap<>();

    public void add(String channel, Hitter hit)
    {
        List<Hitter> li = this.hitLayers.get(channel);
        if( li == null ) 
        {
            li = new LinkedList<>();
            this.hitLayers.put(channel, li);
        }
        li.add(hit);
    }

    public boolean remove(String channel, Hitter hit)
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


    public void proc(String fr, String to)
    {
        List<Hitter> li_fr = this.hitLayers.get(fr),
                     li_to = this.hitLayers.get(fr);

        if( li_fr != null && li_to != null )
        {
            for( Hitter hit_to : li_to )
            {
                hit_to.resetHit();
                for( Hitter hit_fr : li_fr )
                {
                    if( hit_fr.collidesWith(hit_to) )
                    {
                        hit_to.onHit();
                        break;
                    }
                }
            }
        }
    }
}
