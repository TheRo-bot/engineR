package dev.ramar.e2.demos.combat.guns.bullets;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.structures.Vec2;

public class Bullet implements Drawable, Updatable
{
    public final Vec2 pos = new Vec2();
    public final Vec2 vel = new Vec2();

    public Bullet(double xv, double yv)
    {
        this.vel.set(xv, yv);
    }

    public Bullet(double x, double y, double xv, double yv)
    {
        this(xv, yv);
        this.pos.set(x, y);
    }


    private double timeToLive = -999.0;
    public Bullet withTimeToLive(double ttl)
    {
        this.timeToLive = ttl;
        return this;
    }


    private double deceleration = 1.0;
    public Bullet withDeceleration(double decel)
    {
        this.deceleration = decel;
        return this;
    }

    public boolean update(double delta)
    {
        this.timeToLive -= delta;


        double deltaMoveX = this.vel.getX() * this.deceleration * delta,
               deltaMoveY = this.vel.getY() * this.deceleration * delta;

        this.pos.add (deltaMoveX, deltaMoveY);
        this.vel.take(deltaMoveX, deltaMoveY);

        boolean cease =  (timeToLive < 0 && timeToLive > -999) ||
                         (Math.abs(this.vel.getX()) < 0.5 && Math.abs(this.vel.getY()) < 0.5); 

        return cease;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.rect.withMod()
            .colour.with(255, 255, 255, 255)
            .fill.with()
            .offset.with(x, y)
            .offset.with(this.pos.getX(), this.pos.getY())
        ;

        double size = 3;
        vp.draw.rect.poslen(-size, -size, size * size, size * 2);

    }
}
