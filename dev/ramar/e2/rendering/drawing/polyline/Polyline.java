package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;



public class Polyline implements Drawable
{

    public Polyline() 
    {
        this.points = new GenericPolylinePoints();
    }

    protected Polyline(PolylinePoints override)
    {
        if( override == null )
            this.points = new GenericPolylinePoints();
        else
            this.points = override;
    }


    public final PolylinePoints points;


    protected PolylineMods mod = new PolylineMods();
    public PolylineMods getMod()
    {   return this.mod;   }


    public void drawAt(double x, double y, ViewPort vp)
    {
        if( this.points instanceof GenericPolylinePoints )
        {
            GenericPolylinePoints gPoints = (GenericPolylinePoints)this.points;

            if( gPoints.drawCache != null )
            {
                vp.draw.polyline.withMod(this.mod);

                synchronized(gPoints)
                {
                    if( gPoints.offsets )
                        vp.draw.polyline.offsets(x, y, gPoints.drawCache);
                    else
                        vp.draw.polyline.points(x, y, gPoints.drawCache);
                }

                vp.draw.polyline.clearMod();
            }
        }
    }


    public static class GenericPolylinePoints extends PolylinePoints
    {
        protected GenericPolylinePoints() {}

        private List<Vec2> list = new ArrayList<>();

        private Vec2[] drawCache = null;

        private Vec2[] getArray()
        {   return this.drawCache;   }



        /* PolylinePoints Implementation
        --===-----------------------------
        */
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


        public int size()
        {   return this.list.size();   }

        public double getX(int ii)
        {   return this.list.get(ii).getX();   }
        public double getY(int ii)
        {   return this.list.get(ii).getY();   }


        public void set(int ii, double x, double y)
        {   this.list.get(ii).set(x, y);   }

        public void setX(int ii, double val)
        {   this.list.get(ii).setX(val);   }
        public void setY(int ii, double val)
        {   this.list.get(ii).setY(val);   }

    }

    public static abstract class PolylinePoints
    {
        public abstract PolylinePoints add(double x, double y);
        public abstract PolylinePoints add(Vec2 v);
        public abstract PolylinePoints makeOffsets(boolean offsets);

        public abstract int size();
        public abstract double getX(int ii);
        public abstract double getY(int ii);

        public abstract void set(int ii, double x, double y);
        public abstract void setX(int ii, double val);
        public abstract void setY(int ii, double val);
    }
}