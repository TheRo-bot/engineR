package dev.ramar.e2.demos.combat.guns.bullets;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.rect.RectMods;

import dev.ramar.e2.demos.combat.CombatDemo;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.hitboxes.Hitter;
import dev.ramar.e2.demos.combat.hitboxes.Rectbox;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import dev.ramar.utils.HiddenList;

import java.util.List;

public class Bullet implements Drawable, Updatable
{
    public final Vec2 pos = new Vec2();
    public final Vec2 vel = new Vec2();

    public final Hitter<Bullet, Rectbox> hitter = new Hitter(this, new Rectbox(5, 5));

    public Bullet(double xv, double yv)
    {
        this.vel.set(xv, yv);
        this.hitter.box.withAnchor(this.pos);

        this.mods
            .colour.with(255, 255, 255, 255)
            .fill.with()
        ;
    }

    public Bullet(double x, double y, double xv, double yv)
    {
        this(xv, yv);
        this.pos.set(x, y);
    }



    public void kill()
    {
        this.onCease();
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


    public LocalList<OnCease> onCease = new LocalList<>();

    public interface OnCease
    {   public void onCease();   }
    public class LocalList<E> extends HiddenList<E>
    { 
        private List<E> getList() 
        {  return this.list;  }
    }

    protected final void onCease()
    {
        List<OnCease> li = this.onCease.getList();
        for( OnCease oc : li )
            oc.onCease();
    }


    public boolean update(double delta)
    {

        double deltaMoveX = this.vel.getX() * this.deceleration * delta,
               deltaMoveY = this.vel.getY() * this.deceleration * delta;

        this.pos.add (deltaMoveX, deltaMoveY);
        this.vel.take(deltaMoveX, deltaMoveY);

        boolean cease =  (timeToLive < 0 && timeToLive > -999) ||
                         (Math.abs(this.vel.getX()) < 0.5 && Math.abs(this.vel.getY()) < 0.5); 
        if( cease )
            this.onCease();
        return cease;
    }

    public final RectMods mods = new RectMods();


    /* Hitter Implementation
    --===----------------------
    */




    /* Drawable Implementation
    --===------------------------
    */


    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.rect.withMod(this.mods);


        double px = this.pos.getX(),
               py = this.pos.getY();

        Colour origColour = new Colour(this.mods.colour.get());

        if( this.hitter.isHit() )
            this.mods.colour.with(255, 0, 0, 255);

        this.mods
            .offset.with(x, y)
            .offset.with(px, py)
        ;

        double w = this.hitter.box.getW(),
               h = this.hitter.box.getH();

        vp.draw.rect.poslen(w * -0.5, h * -0.5, w, h);

        this.mods
            .offset.with( -x,  -y)
            .offset.with(-px, -py)
            .colour.with(origColour)
        ;

        vp.draw.rect.clearMod();
    }
}