package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.rendering.drawing.Drawer;

import dev.ramar.e2.structures.Vec2;

public abstract class PolylineDrawer extends Drawer<PolylineMods>
{

    public PolylineMods withMod()
    {
        return this.withMod(new PolylineMods());
    }



    public abstract void points(double originX, double originY, double... points);
    public abstract void points(double originX, double originY, Vec2... points);


    // offsets as in this offset = last offset + x, y
    public abstract void offsets(double originX, double originY, double... offsets);
    public abstract void offsets(double originX, double originY, Vec2... offsets);
}