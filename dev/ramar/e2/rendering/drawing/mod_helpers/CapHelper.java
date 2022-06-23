package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.enums.CapStyle;

/*
ModHelper: Cap
 - Links back to this.owner (final!)
*/
public class CapHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public CapHelper(E mho)
    {
        this.owner = mho;
    }

    private CapStyle join = CapStyle.Round;

    public CapStyle get()
    {   return this.join;   }


    public E with(CapStyle js)
    {
        this.join = js;
        return this.owner;
    }

}