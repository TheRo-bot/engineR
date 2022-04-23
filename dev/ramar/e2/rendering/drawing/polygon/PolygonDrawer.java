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

    public void points(double... points)
    {
        this.points(0, 0, points);
    }
    public void points(Vec2... points)
    {
        this.points(0, 0, points);
    }


    public abstract void points(double originX, double originY, double... points);
    public abstract void points(double originX, double originY, Vec2... points);


    public void offsets(double... offsets)
    {
        this.offsets(0, 0, offsets);
    }
    public void offsets(Vec2... offsets)
    {
        this.offsets(0, 0, offsets);
    }
    
    // offsets as in this offset = last offset + x, y
    public abstract void offsets(double originX, double originY, double... offsets);
    public abstract void offsets(double originX, double originY, Vec2... offsets);
}
