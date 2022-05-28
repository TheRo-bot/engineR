package dev.ramar.e2.demos.combat.hitboxes;

import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Point;
import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.drawing.rect.RectMods;

public class Rectbox extends Hitbox implements Point
{


    public Rectbox(double w, double h)
    {
        this.w = w;
        this.h = h;

        this.drawing
            .fill.with()
        ;
    }

    public Rectbox(Point an, double w, double h)
    {
        this(w, h);
        this.pos = an;
    }

    public Rectbox(double x, double y, double w, double h)
    {
        this(new Vec2(x, y), w, h);
    }


    private double w = 0, h = 0;

    private Point pos;
    public Rectbox withAnchor(Point anchor)
    {   this.pos = anchor;  return this;  }

    public double getX() {  return this.pos.getX();  }
    public double getY() {  return this.pos.getY();  }

    public double addX(double x)  {  return this.pos.addX(x);  }
    public double addY(double y)  {  return this.pos.addY(y);  }

    public double minX(double x)  {  return this.pos.minX(x);  }
    public double minY(double y)  {  return this.pos.minY(y);  }

    public double mulX(double x)  {  return this.pos.mulX(x);  }
    public double mulY(double y)  {  return this.pos.mulY(y);  }

    public double divX(double x)  {  return this.pos.divX(x);  }
    public double divY(double y)  {  return this.pos.divY(y);  }


    /* Hitbox Implementation
    --====---------------------
    */

    public boolean collidesWith(Rectbox rb)
    {   
        boolean collides = false;

        if( rb != null )
        {
            //// determine on each axis which is bigger, 
            //// if smaller is within bigger for both axes, then collides

            Rectbox smallx, bigx;
            Rectbox smally, bigy;

            if( this.w > rb.w )
            {
                bigx = this;
                smallx = rb;
            }
            else
            {
                bigx = rb;
                smallx = this;
            }

            if( this.h > rb.h )
            {
                bigy = this;
                smally = rb;
            }
            else
            {
                bigy = rb;
                smally = this;
            }

            boolean x = (bigx.getLeft() <= smallx. getLeft() && smallx. getLeft() <= bigx.getRight())
                     || (bigx.getLeft() <= smallx.getRight() && smallx.getRight() <= bigx.getRight());

            if( x )
                collides = (bigy.getTop() <= smally.getTop() && smally.getTop() <= bigy.getBot())
                        || (bigy.getTop() <= smally.getBot() && smally.getBot() <= bigy.getBot());
        }

        return collides;
    }


    public double getTop()
    {  return this.getY() - this.h / 2;  }
    public double getBot()
    {  return this.getY() + this.h / 2;  }
    public double getRight()
    {  return this.getX() + this.w / 2;  }
    public double getLeft()
    {  return this.getX() - this.w / 2;  }


    public final RectMods drawing = new RectMods();

    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.rect.withMod(this.drawing);

        this.drawing
            .offset.with( x,  y)
        ;

        vp.draw.rect.poslen(this.getLeft(), this.getTop(), this.w, this.h);

        this.drawing
            .offset.with(-x, -y)
        ;

        vp.draw.rect.clearMod();
    }
}