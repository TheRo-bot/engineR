package dev.ramar.e2.rendering.drawing.mod_helpers;

/*
ModHelper: BooleanHelper
 - Links back to this.owner (final!)
*/
public class BooleanHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public BooleanHelper(E mho, boolean def)
    {
        this.owner = mho;
        this.bool = def;
    }

    public BooleanHelper(E mho, BooleanHelper bh)
    {
        this.owner = mho;

        if( bh != null )
            this.bool = bh.bool;
    }

    private boolean bool = false;

    public boolean get()
    {   return this.bool;   }


    public E with(boolean bool)
    {
        this.bool = bool;
        return this.owner;
    }

    public E with()
    {
        return this.with(true);
    }

    public E without()
    {
        return this.with(false);
    }

}