package dev.ramar.e2.rendering.drawing.mod_helpers;

/*
ModHelper: Fill
 - Stores a boolean, has helper methods so you just say with / without fill
 - Links back to this.owner (final!)
*/
public class FillHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public FillHelper(E mho)
    {
        this.owner = mho;
    }

    public FillHelper(E mho, FillHelper fh)
    {
        this(mho);
        if( fh != null )
        {
            this.fill = fh.fill;
        }
    }

    private boolean fill = false;

    public boolean get()
    {   return this.fill;   }


    public E with()
    {
        return this.with(true);
    }

    public E without()
    {
        return this.with(false);
    }

    public E with(boolean f)
    {
        this.fill = f;
        return this.owner;
    }

}