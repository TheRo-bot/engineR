package dev.ramar.e2.core.drawing.mod_helpers;

public class BooleanHelper<E extends ModHelperOwner>
{
    private E owner;

    public BooleanHelper(E owner) 
    {
        this.owner = owner;
    }


    public BooleanHelper(E owner, boolean v)
    {
        this(owner);
        this.with(v);
    }

    
    /*
    Class-Field: colour
     - The colour of this rect!
    */  

    private boolean value = false;
    public boolean get()
    {   return this.value;   }


    public E with()
    {   return this.with(true);   }

    public E without()
    {   return this.with(false);   }

    public E with(boolean v)
    {
        this.value = v;
        return this.owner;
    }
}