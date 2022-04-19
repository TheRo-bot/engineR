package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.drawing.Drawer;

import dev.ramar.e2.structures.Vec2;


/*
Drawer: Polygon
 - draws dang polygons darnit!
*/
public abstract class PolygonDrawer extends Drawer<PolygonMods>
{


    public PolygonMods withMod()
    {
        return this.withMod(new PolygonMods());
    }


    public abstract void points(double... points);
    public abstract void points(Vec2... points);


    // offsets as in this offset = last offset + x, y
    public abstract void offsets(double... offsets);
    public abstract void offsets(Vec2... offsets);
}
