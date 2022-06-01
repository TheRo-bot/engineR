package dev.ramar.e2.demos.combat.guns.semiauto;

import dev.ramar.e2.demos.combat.guns.bullets.Bullet;
import dev.ramar.e2.demos.combat.guns.bullets.BulletFactory;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Point;

import dev.ramar.utils.HiddenList;

import dev.ramar.e2.demos.combat.guns.Gun;

import java.util.List;
import java.util.ArrayList;

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


    public interface OnShoot
    {   public void onShoot(Bullet b);   }
    public final LocalList<OnShoot> onShoot = new LocalList<>();

    private final List<Bullet> shots = new ArrayList<>();


    protected final void onShoot(Bullet b)
    {
        List<OnShoot> li = onShoot.getList();
        for( OnShoot os : li )
            os.onShoot(b);
    }


    public void shootAt(double x, double y)
    {
        if( this.from != null && this.bf != null )
        {
            Bullet b = this.bf.make(x - this.from.getX(), y - this.from.getY());
            b.pos.set(from);
            this.shots.add(b);
            this.onShoot(b);
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


    public class LocalList<E> extends HiddenList<E>
    {
        private List<E> getList()
        {   return this.list;   }
    }
}