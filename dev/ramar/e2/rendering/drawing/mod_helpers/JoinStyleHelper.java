package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.JoinStyle;

public class JoinStyleHelper<E extends ModHelperOwner>
{
    private E owner;

    public JoinStyleHelper(E owner) 
    {
        this.owner = owner;
    }

    public JoinStyleHelper(E owner, JoinStyle cs)
    {
        this(owner);
        this.with(cs);
    }


    private JoinStyle capStyle = JoinStyle.Round;
    public JoinStyle get()
    {   return this.capStyle;   }

    public E with(JoinStyle cs)
    {
        this.capStyle = cs;
        return this.owner;
    }
}
