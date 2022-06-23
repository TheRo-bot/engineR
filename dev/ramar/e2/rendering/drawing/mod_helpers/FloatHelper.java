package dev.ramar.e2.rendering.drawing.mod_helpers;

/*
ModHelper: Float
 - Links back to this.owner (final!)
*/
public class FloatHelper<E extends ModHelperOwner>
{
    protected final E owner;

    public FloatHelper(E mho)
    {
        this.owner = mho;
    }

    private float value = 0.0f;

    public float get()
    {   return this.value;   }


    public E with(float f)
    {
        this.value = f;
        return this.owner;
    }

}