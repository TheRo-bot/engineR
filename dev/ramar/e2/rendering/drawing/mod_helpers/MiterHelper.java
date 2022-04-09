package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.JoinStyle;

public class MiterHelper<E extends ModHelperOwner>
{
    private E owner;

    public MiterHelper(E owner) 
    {
        this.owner = owner;
    }

    public MiterHelper(E owner, float m)
    {
        this(owner);
        this.with(m);
    }



    private float miter = 10.0f;
    public float get()
    {   return this.miter;   }

    public E with(double m)
    {
        this.miter = (float)m;
        return this.owner;
    }

}





