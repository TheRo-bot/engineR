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
        this.stats.velocity = 25.0;
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
            // get the vector of <x, y> -> <anchor>
            double xD = x - this.from.getX(),
                   yD = y - this.from.getY();


            // normalise the vector
            double abs = Math.sqrt(xD * xD + yD * yD);
            double xN = xD / abs;
            double yN = yD / abs;


            // give v <velocity> distance
            double xV = xN * this.stats.velocity * this.stats.timeToLive;
            double yV = yN * this.stats.velocity * this.stats.timeToLive;

            // modify the final velocity by the "dinner plate" concept:
            // - generate a random normal vector
            // - apply that vector to v, 

            // double rang = rd.nextInt(360);

            // double dist = (this.stats.getSpread() * this.stats.timeToLive);
            // double timeMod = ((1 + this.totalShootDelta) * this.stats.getSpreadModifier());

            // dist *= timeMod;

            // double rxN = Math.cos(rang) * dist;
            // double ryN = Math.sin(rang) * dist;

            // xV += rxN;
            // yV += ryN;

            // double abs2 = Math.sqrt(xV * xV + yV * yV);

            // xV /= abs2;
            // yV /= abs2;

            xV *= this.stats.velocity * this.stats.timeToLive;
            yV *= this.stats.velocity * this.stats.timeToLive;

            Bullet b = this.bf.make(xV, yV);
            b.pos.set(from);
            this.onShoot(b);
            this.clip--;
            System.out.println(this.clip + " / " + this.stats.clipSize);
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