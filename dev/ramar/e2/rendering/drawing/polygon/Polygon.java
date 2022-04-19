package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;
/*
Drawable: Polygon
 - Represents a gosh darn Polygon!
*/
public class Polygon implements Drawable
{

    public Polygon()
    {

    }

    public final PolygonPoints points = new PolygonPoints();
    public static class PolygonPoints
    {
        private PolygonPoints() {}

        private List<Vec2> list = new ArrayList<>();

        private Vec2[] drawCache = null;

        private Vec2[] getArray()
        {   return this.drawCache;   }


        public int size()
        {   return this.list.size();   }

        public Vec2 get(int ii)
        {   return this.list.get(ii);    }


        public PolygonPoints add(double x, double y)
        {
            this.add(new Vec2(x, y));
            return this;
        }


        public PolygonPoints add(Vec2 v)
        {
            synchronized(this)
            {
                this.list.add(v);
            }

            this.drawCache = this.list.toArray(new Vec2[this.list.size()]);
            return this;
        }

        private boolean offsets = false;
        public PolygonPoints makeOffsets(boolean offsets)
        {
            this.offsets = offsets;
            return this;
        }

    }

    private PolygonMods mod = new PolygonMods();

    public PolygonMods getMod()
    {
        return this.mod;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        if( this.points.drawCache != null )
        {
            this.mod
                .offset.with(x, y)
            ;   

            vp.draw.polygon.withMod(this.mod);

            synchronized(this.points)
            {
                if( this.points.offsets )
                    vp.draw.polygon.offsets(points.drawCache);
                else
                    vp.draw.polygon.points(points.drawCache);
            }

            vp.draw.polygon.clearMod();

            this.mod
                .offset.with(-x, -y)
            ;
        }
    }
}