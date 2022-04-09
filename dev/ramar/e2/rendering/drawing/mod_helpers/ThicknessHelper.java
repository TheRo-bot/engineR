package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Colour;

public class ThicknessHelper<E extends ModHelperOwner>
{
    private E owner;

    public ThicknessHelper(E owner) 
    {
        this.owner = owner;
    }


    public ThicknessHelper(E owner, double t)
    {
        this(owner);
        this.with(t);
    }

    
    /*
    Class-Field: thickness
     - The thickness of this thing!
    */  

    private double thickness = 10.0;
    public double get()
    {   return this.thickness;   }

    public E with(double thickness)
    {
        this.thickness = thickness;
        return this.owner;
    }
}