package dev.ramar.e2.demos.combat.guns.semiauto;

import dev.ramar.e2.demos.combat.guns.bullets.Bullet;
import dev.ramar.e2.demos.combat.guns.bullets.BulletFactory;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Point;

import dev.ramar.utils.ListenableList;

import dev.ramar.e2.demos.combat.guns.Gun;

public class SemiAuto extends Gun<SemiAutoStats> implements Drawable
{

    public SemiAuto()
    {
        super(new SemiAutoStats());
    }

    private BulletFactory bf;
    public SemiAuto withBulletFactory(BulletFactory bf)
    {
        this.bf = bf;
        return this;
    }


    private Point from;
    public SemiAuto withOrigin(Point fr)
    {
        this.from = fr;
        return this;
    }


    public final ListenableList<Bullet> shots = new ListenableList<>();



    public void shootAt(double x, double y)
    {
        if( this.from != null && this.bf != null )
        {
            Bullet b = this.bf.make(this.from.getX() - x, this.from.getY() - y);
            b.pos.set(from);
            this.shots.add(b);
            this.clip--;
        }
    }


    public boolean canShoot()
    {
        return this.clip > 0;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {

    }
}