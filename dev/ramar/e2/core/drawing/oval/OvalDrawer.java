package dev.ramar.e2.core.drawing.oval;

import dev.ramar.e2.core.drawing.Drawer;

public abstract class OvalDrawer extends Drawer<OvalMods>
{
    public OvalMods withMod()
    {
        return super.withMod(new OvalMods());
    }

    public abstract void poslen(double x, double y, double w, double h);

    // centered <x, y>
    public abstract void posradii(double x, double y, double a, double b);

}