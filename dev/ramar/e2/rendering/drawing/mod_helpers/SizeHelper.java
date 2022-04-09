package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Vec2;


public class SizeHelper<E extends ModHelperOwner>
{
    private E owner;

    public SizeHelper(E owner) 
    {
        this.owner = owner;
    }

    public SizeHelper(E owner, double s)
    {
        this(owner);
        this.with(s);
    }



    private double size = 0;
    public double get()
    {   return this.size;   }

    public E with(double s)
    {
        this.size = s;
        return this.owner;
    }

}