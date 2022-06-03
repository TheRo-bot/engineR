package dev.ramar.e2.demos.combat.enemies;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.hitboxes.Hitter;
import dev.ramar.e2.demos.combat.hitboxes.Rectbox;

import dev.ramar.e2.demos.combat.CombatDemo;

import dev.ramar.e2.structures.Point;
import dev.ramar.e2.structures.Vec2;

public class Enemy implements Drawable, Point, Updatable
{
    public final Hitter<Rectbox> hitter = new Hitter<>()
        .withHitbox(new Rectbox(10, 10))
    ;

    public Enemy(double x, double y)
    {
        this.pos.set(x, y);
        this.hitter.box.withAnchor(this.pos);
    }


    private CombatDemo demo = null;
    public Enemy withDemo(CombatDemo cd)
    {
        this.demo = cd;
        return this;
    }

    /* Drawable Implementation
    --===------------------------
    */

    public void drawAt(double x, double y, ViewPort vp)
    {
        if( hitter.box != null )
            hitter.box.drawAt(x, y, vp);
    }

    /* Updatable Implementation
    --===-------------------------
    */

    public boolean update(double delta)
    {
        boolean kill = false;


        return kill;
    }


    /* Point Implementation
    --===---------------------
    */  

    private final Vec2 pos = new Vec2();

    public double getX()
    {   return this.pos.getX();   }

    public double getY()
    {   return this.pos.getY();   }


    public double addX(double x)
    {   return this.pos.addX(x);   }

    public double addY(double y)
    {   return this.pos.addY(y);   }


    public double minX(double x)
    {   return this.pos.minX(x);   }

    public double minY(double y)
    {   return this.pos.minY(y);   }


    public double mulX(double x)
    {   return this.pos.mulX(x);   }

    public double mulY(double y)
    {   return this.pos.mulY(y);   }


    public double divX(double x)
    {   return this.pos.divX(x);   }

    public double divY(double y)
    {   return this.pos.divY(y);   }

}