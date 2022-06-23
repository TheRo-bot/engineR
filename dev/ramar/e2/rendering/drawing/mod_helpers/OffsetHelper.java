package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Vec2;


/*
ModHelper: Offset
 - Stores a Vec2, allows modification of that Vec2 easily!
 - Links back to this.owner (final!)
*/
public class OffsetHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public OffsetHelper(E mho)
    {
        this.owner = mho;
    }

    private Vec2 off = new Vec2();
    public Vec2 get()
    {   return this.off;   }


    public double getX()
    {   return this.off.getX();   }


    public double getY()
    {   return this.off.getY();   }




    public E with(Vec2 off)
    {
        if( off == null )
            throw new NullPointerException("can't set offset to OffsetHelper.offset to null!");

        this.off = off;
        return this.owner;
    }


    public E with(double x, double y)
    {
        return this.with(x, y, false);
    }


    public E with(double x, double y, boolean force)
    {
        if( force )
            this.off.set(x, y);
        else
            this.off.add(x, y);

        return this.owner;
    }


}