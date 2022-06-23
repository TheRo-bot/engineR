package dev.ramar.e2.rendering.drawing.rect;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;


import dev.ramar.e2.structures.Vec2;


/*
Drawable: Rect
 - A more persistent way to draw a rect!
 - put it to the screen by using drawAt(...) with a 
   ViewPort that goes to a screen!
*/
public class Rect implements Drawable
{

    public Rect() {}

    public Rect(double x, double y)
    {
        this.pos.set(x, y);
    }


    public Rect(double x, double y, double w, double h)
    {
        this.pos.set(x, y);
        this.len.set(w, h);
    }





    public Vec2 pos = new Vec2();

    public Vec2 getPos()
    {   return this.pos;    }
    public Rect withPos(Vec2 p)
    {
        this.pos = p;
        return this;
    }


    public double getX()
    {   return this.pos.getX();   }
    public Rect withX(double x)
    {
        this.pos.setX(x);
        return this;
    }


    public double getY()
    {   return this.pos.getY();   }
    public Rect withY(double y)
    {
        this.pos.setY(y);
        return this;
    }



    /*
    Class-Field: len
     - Vec2 of the length of this object, pos + len should be
       bottom-right of the rect assuming len's x and y > 0
    */
    public Vec2 len = new Vec2();

    public Vec2 getLen()
    {   return this.len;   }

    public Rect withLen(Vec2 l)
    {
        this.len = l;
        return this;
    }


    public double getW()
    {   return this.len.getX();   }
    public Rect withW(double w)
    {
        this.len.setX(w);
        return this;
    }


    public double getH()
    {   return this.len.getY();   }
    public Rect withH(double h)
    {
        this.len.setY(h);
        return this;
    }




    /*
    Class-Field: mod
     - the RectMods that describes information about drawing this rect
     - same stuff as vp.draw.rect.withMod() !
    */
    public RectMods mod = new RectMods();

    public RectMods getMod()
    {    return this.mod;   }
    public RectMods withMod()
    {   return this.withMod(new RectMods());   }

    public RectMods withMod(RectMods rm)
    {
        this.mod = rm;
        return this.mod;
    }



    /* Drawable Implementation
    --===------------------------
    */


    public void drawAt(double x, double y, ViewPort vp)
    {
        //// set up
        this.mod.offset.with(x, y);

        //// draw
        vp.draw.rect.withMod(this.mod);
        vp.draw.rect.poslen(pos.getX(), pos.getY(), len.getX(), len.getY());
        vp.draw.rect.clearMod();

        //// set down
        this.mod.offset.with(-x, -y);
    }
}