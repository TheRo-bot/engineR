package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Colour;

public class ColourHelper<E extends ModHelperOwner>
{
    private E owner;

    public ColourHelper(E owner) 
    {
        this.owner = owner;
    }

    public ColourHelper(E owner, int r, int g, int b, int a)
    {
        this(owner);
        this.with(r, g, b, a);
    }

    /*
    Class-Field: colour
     - The colour of this rect!
    */  

    private Colour colour = new Colour(255, 255, 255, 255);
    public Colour get()
    {   return this.colour;   }


    public E with(Colour colour)
    {
        if( colour == null )
            throw new NullPointerException("Colour.with(Colour) must not be null!");

        this.colour = colour;
        return this.owner;
    }

    public E with(int r, int g, int b, int a)
    {
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        a = Math.max(0, Math.min(255, a));

        this.colour.set(r, g, b, a);

        return this.owner;
    }
}