package dev.ramar.e2.rendering.drawing;

public abstract class Drawer<E extends Mod>
{

    public Drawer() {}


    protected E mod = null;

    public E getMod()
    {   return this.mod;   }

    public E withMod(E mod)
    {
        this.mod = mod;
        return this.mod;
    }

    public Drawer clearMod()
    {
        this.mod = null;
        return this;
    }
}