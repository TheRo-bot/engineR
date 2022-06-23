package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.rendering.drawing.enums.JoinStyle;

/*
ModHelper: Join
 - Links back to this.owner (final!)
*/
public class JoinHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public JoinHelper(E mho)
    {
        this.owner = mho;
    }

    private JoinStyle join = JoinStyle.Round;

    public JoinStyle get()
    {   return this.join;   }


    public E with(JoinStyle js)
    {
        this.join = js;
        return this.owner;
    }

}