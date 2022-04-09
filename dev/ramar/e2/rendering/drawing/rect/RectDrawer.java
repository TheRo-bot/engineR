package dev.ramar.e2.rendering.drawing.rect;

import dev.ramar.e2.rendering.drawing.Drawer;

public abstract class RectDrawer extends Drawer<RectMods>
{
    public RectMods withMod()
    {
        return super.withMod(new RectMods());
    }

    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double w, double h);

}