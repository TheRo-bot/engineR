package dev.ramar.e2.demos.combat.hitboxes;

import dev.ramar.e2.rendering.Drawable;

public abstract class Hitbox implements Drawable
{
    public static class HitLayers
    {
        public static class Player
        {
            public static final int COLLIDE = 0;
            public static final int BULLETS = 1;
        }

        public static class Enemy
        {
            public static final int COLLIDE = 2;
            public static final int BULLETS = 3;
        }
    }

    protected boolean hit = false;
    public boolean isHit()
    {   return this.hit;   }

    public void setHit(boolean b)
    {   this.hit = b;   }

    public final Hitbox withHit(boolean b)
    {   this.setHit(b);  return this;  }


    public boolean collidesWith(Hitbox hb)
    {
        boolean out = false;
        if( hb != null )
        {
            if( hb instanceof Rectbox )
                out = this.collidesWith((Rectbox)hb);
        }

        return out;
    }

    public abstract boolean collidesWith(Rectbox rb);

}