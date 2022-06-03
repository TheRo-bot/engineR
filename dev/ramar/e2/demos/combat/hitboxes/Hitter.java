package dev.ramar.e2.demos.combat.hitboxes;


public class Hitter<E extends Hitbox>
{

    public Hitter() {}

    // to be safe, synchronise on the Hitter to publicly change these safely!


    // public String hitLayer = null;

    // public synchronized Hitter withHitLayer(String layer)
    // {   this.hitLayer = layer;  return this;  }



    public boolean hit = false;

    public void onHit()
    {  this.hit = true;  }

    public void resetHit()
    {  this.hit = false;  }

    public synchronized Hitter<Hitbox> withHit(boolean hit)
    {   this.hit = hit;  return this;    }


    // no collision can happen if they don't set it :^)
    public Hitbox box;

    public synchronized Hitter<E> withHitbox(Hitbox hb)
    {   this.box = hb;  return this;   }



}