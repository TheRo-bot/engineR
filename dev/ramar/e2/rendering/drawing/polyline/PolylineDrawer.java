package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.rendering.drawing.Drawer;

import dev.ramar.e2.structures.Vec2;

public abstract class PolylineDrawer extends Drawer<PolylineMods>
{

    public PolylineMods withMod()
    {
        return this.withMod(new PolylineMods());
    }



    public abstract void points(double... points);
    public abstract void points(Vec2... points);


    // offsets as in this offset = last offset + x, y
    public abstract void offsets(double... offsets);
    public abstract void offsets(Vec2... offsets);
}