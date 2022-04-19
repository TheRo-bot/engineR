package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;



public class Polyline implements Drawable
{

    public Polyline() {}


    public final PolylinePoints points = new PolylinePoints();
    public static class PolylinePoints
    {
        private PolylinePoints() {}

        private List<Vec2> list = new ArrayList<>();

        private Vec2[] drawCache = null;

        private Vec2[] getArray()
        {   return this.drawCache;   }


        public int size()
        {   return this.list.size();   }

        public Vec2 get(int ii)
        {   return this.list.get(ii);    }


        public PolylinePoints add(double x, double y)
        {
            this.add(new Vec2(x, y));
            return this;
        }


        public PolylinePoints add(Vec2 v)
        {
            synchronized(this)
            {
                this.list.add(v);
            }

            this.drawCache = this.list.toArray(new Vec2[this.list.size()]);
            return this;
        }

        private boolean offsets = false;
        public PolylinePoints makeOffsets(boolean offsets)
        {
            this.offsets = offsets;
            return this;
        }

    }


    private PolylineMods mod = new PolylineMods();
    public PolylineMods getMod()
    {   return this.mod;   }


    public void drawAt(double x, double y, ViewPort vp)
    {
        if( this.points.drawCache != null )
        {
            vp.draw.polyline.withMod(this.mod);
            this.mod
                .offset.with(x, y)
            ;

            synchronized(this.points)
            {
                if( this.points.offsets )
                    vp.draw.polyline.offsets(this.points.drawCache);
                else
                    vp.draw.polyline.points(this.points.drawCache);
            }

            this.mod
                .offset.with(-x, -y)
            ;

            vp.draw.polyline.clearMod();
        }
    }
}