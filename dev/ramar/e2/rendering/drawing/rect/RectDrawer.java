package dev.ramar.e2.rendering.drawing.rect;

import dev.ramar.e2.rendering.drawing.Drawer;


/*
Drawer: RectDrawer
 - Representation of something that can draw rectangles to something!
 - has a singular RectMods instance, you should use that when drawing
*/
public abstract class RectDrawer extends Drawer<RectMods>
{

    public RectMods withMod()
    {
        return this.withMod(new RectMods());
    }


    public abstract void pospos(double x1, double y1, double x2, double y2);
    public abstract void poslen(double x1, double y1, double w, double h);
}