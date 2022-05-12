package dev.ramar.e2.rendering.awt.drawing.polyline;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.polyline.PolylineMods;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.ObservableVec2;

import java.util.Arrays;
import java.util.Iterator;
import java.lang.Iterable;

public class AWTPolyline implements Drawable, Iterable<Vec2>
{
    private int[] xs = new int[0],
                  ys = new int[0];

    public final PolylineMods mods = new PolylineMods();

    public AWTPolyline()
    {

    }

    public int size()
    {
        return Math.min(this.xs.length, this.ys.length);
    }

    public Iterator<Vec2> iterator()
    {
        return new PolyLineIterator(this);
    }


    public class PolyLineIterator implements Iterator<Vec2>
    {
        private AWTPolyline pline;
        private int currDex = 0;
        public PolyLineIterator(AWTPolyline pline)
        {
            this.pline = pline;
        }

        public PolyLineIterator(AWTPolyline pline, int startDex)
        {
            if( startDex < 0 || startDex >= pline.size() )
                throw new IndexOutOfBoundsException(startDex + " for bounds 0-" + startDex);

            this.currDex = startDex;
        }

        public boolean hasNext()
        {
            int nextDex = this.currDex + 1;
            return 0 <= nextDex && nextDex <= pline.size();
        }

        public Vec2 next()
        {
            final int dex = this.currDex;
            ObservableVec2 out = new ObservableVec2(this.pline.xs[this.currDex], this.pline.ys[this.currDex]);

            out.listeners.onX.add((double x) -> 
            {
                xs[dex] = (int)x;
            });
            out.listeners.onY.add((double y) -> 
            {
                ys[dex] = (int)y;
            });

            this.currDex += 1;
            return out;
        }
    } 


    public AWTPolyline addPoint(double x, double y)
    {
        this.xs = Arrays.copyOf(this.xs, this.xs.length + 1);
        this.xs[this.xs.length - 1] = (int)x;

        this.ys = Arrays.copyOf(this.ys, this.ys.length + 1);
        this.ys[this.ys.length - 1] = (int)y;

        return this;
    }

    public AWTPolyline addOffset(double x, double y)
    {
        double xOff = 0.0,
               yOff = 0.0;
        if( this.xs.length > 0 )
            xOff = this.xs[this.xs.length - 1];

        if( this.ys.length > 0 )
            yOff = this.ys[this.ys.length - 1];

        return this.addPoint(x + xOff, y + yOff);
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        AWTPolylineDrawer pline = (AWTPolylineDrawer)vp.draw.polyline;
        pline.withMod(mods);
        mods.offset.with(x, y);
        pline.points(this.xs, this.ys);
        mods.offset.with(-x, -y);
        pline.clearMod();
    }
}