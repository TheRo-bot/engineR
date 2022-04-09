package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Vec2;


public class OffsetHelper<E extends ModHelperOwner>
{

    private E owner;

    public OffsetHelper(E owner) 
    {
        this.owner = owner;
    }

    private Vec2 offset = new Vec2();
    public Vec2 get()
    {   return this.offset;   }

    public double getX()
    {   return this.offset.getX();   }

    public double getY()
    {   return this.offset.getY();   }


    public E with(Vec2 o)
    {
        this.offset = o;
        return this.owner;
    }


    public E with(double x, double y)
    {
        this.with(x, y, false);
        return this.owner;
    }

    public E with(double x, double y, boolean set)
    {
        if( set )
            this.offset.set(x, y);
        else
            this.offset.add(x, y);

        return this.owner;
    }


    public E withForce(double x, double y)
    {
        return this.with(x, y, true);
    }

}