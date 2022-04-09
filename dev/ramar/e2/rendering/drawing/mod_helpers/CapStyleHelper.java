package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.CapStyle;

public class CapStyleHelper<E extends ModHelperOwner>
{
    private E owner;

    public CapStyleHelper(E owner) 
    {
        this.owner = owner;
    }

    public CapStyleHelper(E owner, CapStyle cs)
    {
        this(owner);
        this.with(cs);
    }


    private CapStyle capStyle = CapStyle.Round;
    public CapStyle get()
    {   return this.capStyle;   }

    public E with(CapStyle cs)
    {
        this.capStyle = cs;
        return this.owner;
    }
}
