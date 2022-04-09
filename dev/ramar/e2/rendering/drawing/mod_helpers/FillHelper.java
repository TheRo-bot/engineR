package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Colour;

public class FillHelper<E extends ModHelperOwner>
{
    private E owner;

    public FillHelper(E owner) 
    {
        this.owner = owner;
    }


    public FillHelper(E owner, boolean f)
    {
        this(owner);
        this.with(f);
    }

    
    /*
    Class-Field: colour
     - The colour of this rect!
    */  

    private boolean fill = false;
    public boolean get()
    {   return this.fill;   }


    public E with()
    {   return this.with(true);   }

    public E without()
    {   return this.with(false);   }

    public E with(boolean f)
    {
        this.fill = f;
        return this.owner;
    }
}