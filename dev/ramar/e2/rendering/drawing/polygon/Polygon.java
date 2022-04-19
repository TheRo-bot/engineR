package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

/*
Drawable: Polygon
 - Represents a gosh darn Polygon!
*/
public class Polygon implements Drawable
{

    public Polygon()
    {

    }

    public LocalList<Vec2> points = new LocalList<>();

    private boolean offets = false;
    public Polygon makeOffsets()
    {
        this.offsets = true;
        return this;
    }

    public static class LocalList<E> extends HiddenList<E>
    {
        private List<Vec> getList()
        {   return this.list;   }

        public void add(double x, double y)
        {
            this.add(new Vec2(x, y));
        }
    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.polygon.withMod(this.mod);

        if( offsets )
            vp.draw.polygon.points(points.getList());
        else
            vp.draw.polygon.offsets(points.getList());
    }
}