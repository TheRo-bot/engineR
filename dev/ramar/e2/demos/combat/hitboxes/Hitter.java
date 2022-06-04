package dev.ramar.e2.demos.combat.hitboxes;

import dev.ramar.utils.HiddenList;
import java.util.List;

public class Hitter<O, E extends Hitbox>
{

    private O owner;
    public O getOwner()
    {   return this.owner;  }

    public Hitter(O owner) 
    {
        this.owner = owner;
    }


    public Hitter(O owner, E box)
    {
        this.owner = owner;
        this.box = box;
    }

    private boolean hit = false;
    public synchronized boolean isHit()
    {   return this.hit;  }


    public final LocalList<OnHit> onHit = new LocalList<>();
    public final LocalList<OnClear> onClear = new LocalList<>();


    public synchronized void onClear()
    {
        this.hit = false;
        List<OnClear> li = onClear.getList();
        for( OnClear oc : li )
            oc.onClear();
    }


    public synchronized void onHit(Hitter collidee)
    {
        this.hit = true;
        List<OnHit> li = onHit.getList();
        for( OnHit oh : li )
            oh.onHit(collidee);
    }



    public E box = null;


    /* Type Definitions
    --===-----------------
    */

    public interface OnHit
    {   public void onHit(Hitter collidee);   }

    public interface OnClear
    {   public void onClear();   }

    public class LocalList<E> extends HiddenList<E>
    {
        private List<E> getList()
        {   return this.list;   }
    }


}