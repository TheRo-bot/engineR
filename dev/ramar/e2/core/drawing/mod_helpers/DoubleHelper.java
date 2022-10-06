package dev.ramar.e2.core.drawing.mod_helpers;

public class DoubleHelper<E extends ModHelperOwner>
{
    private E owner;

    public DoubleHelper(E owner) 
    {
        this.owner = owner;
    }

    public DoubleHelper(E owner, double v)
    {
        this(owner);
        this.with(v);
    }



    private double value = 0;
    public double get()
    {   return this.value;   }

    public E with(double v)
    {
        this.value = v;
        return this.owner;
    }

}