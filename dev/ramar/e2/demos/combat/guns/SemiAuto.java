package dev.ramar.e2.demos.combat.guns;


import dev.ramar.e2.rendering.Drawable;

public class SemiAuto implements Drawable
{

    public SemiAuto()
    {

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



    public void shootAt(double x, double y)
    {
        if( this.from != null && this.bf != null )
        {
            Bullet b = this.bf.make(this.from.getX() - x, this.from.getY() - y);

        }
    }
}