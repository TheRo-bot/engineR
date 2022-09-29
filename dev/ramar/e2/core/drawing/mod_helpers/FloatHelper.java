package dev.ramar.e2.core.drawing.mod_helpers;

public class FloatHelper<E extends ModHelperOwner>
{
    private E owner;

    public FloatHelper(E owner) 
    {
        this.owner = owner;
    }

    public FloatHelper(E owner, float m)
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





