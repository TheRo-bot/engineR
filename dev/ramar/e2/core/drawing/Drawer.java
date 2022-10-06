package dev.ramar.e2.core.drawing;


public abstract class Drawer<E extends Mod>
{
    private E mod = null;
    public E getMod()
    {   return this.mod;   }

    public E withMod(E e)
    {
        this.mod = e;
        return e;
    }

    public void clearMod()
    {   this.mod = null;   }

}