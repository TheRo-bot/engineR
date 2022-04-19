package dev.ramar.e2.rendering.awt.drawing.polygon;


import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.rendering.drawing.polygon.PolygonMods;

import dev.ramar.e2.rendering.awt.AWTViewPort;


import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;

import java.awt.Graphics2D;
import java.util.Arrays;
public class AWTPolygonDrawer extends PolygonDrawer
{

    public static class Defaults
    {
        public static final Colour COLOUR = new Colour(255, 255, 255, 255);
    }

    public AWTPolygonDrawer() {}


    // ok. so. realistically. we could change this to draw to a Graphics2D, and have
    // an alternate interface for information if it needs it.
    // the ability to render to a VolatileImage or a BufferedImage sounds fkn sick tho.
    private AWTViewPort vp = null;

    public AWTPolygonDrawer withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
            this.vp = vp;

        return this;
    }


    private Vec2 getOffset()
    {
        PolygonMods mod = this.getMod();
        if( mod != null )
            return mod.offset.get();

        return null;
    }


    /* Overidden Methods
    --===------------------
    */


    public void points(double... points)
    {
        int[] xs = new int[points.length / 2 + 1];
        int[] ys = new int[points.length / 2 + 1];


        Vec2 off = this.getOffset();
        double offX = off != null ? off.getX() : 0.0,
               offY = off != null ? off.getY() : 0.0;


        int count = 0;
        for( int ii = 0; ii < points.length; ii += 2 )
        {
            xs[count] = (int)(points[ii] + offX);
            ys[count] = (int)(points[ii + 1] + offY);

            count++;
        }

        this.points(xs, ys);
    }


    public void points(Vec2... points)
    {
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];


        Vec2 off = this.getOffset();
        double offX = off != null ? off.getX() : 0.0,
               offY = off != null ? off.getY() : 0.0;


        int ii = 0;
        for( Vec2 v : points )
        {
            xs[ii] = (int)(v.getX() + offX);
            ys[ii] = (int)(v.getY() + offY);
            ii++;
        }

        this.points(xs, ys);
    }


    // offsets as in this offset = last offset + x, y
    public void offsets(double... offsets)
    {
        int[] xs = new int[offsets.length / 2 + 1];
        int[] ys = new int[offsets.length / 2 + 1];

        Vec2 off = this.getOffset();
        double x = off != null ? off.getX() : 0.0,
               y = off != null ? off.getY() : 0.0;

        int count = 0;
        for( int ii = 0; ii < offsets.length; ii += 2 )
        {
            x += offsets[ii];
            y += offsets[ii + 1];

            xs[count] = (int)x;
            ys[count] = (int)y;

            count++;
        }

        this.points(xs, ys);
    }


    public void offsets(Vec2... offsets)
    {
        int[] xs = new int[offsets.length];
        int[] ys = new int[offsets.length];

        Vec2 off = this.getOffset();
        double x = off != null ? off.getX() : 0.0,
               y = off != null ? off.getY() : 0.0;

        int ii = 0;
        for( Vec2 v : offsets )
        {
            x += v.getX();
            y += v.getY();

            xs[ii] = (int)x;
            ys[ii] = (int)y;
            ii++;
        }

        this.points(xs, ys);
    }




    public void points(int[] xs, int[] ys)
    {
        Graphics2D g2d = this.vp.getGraphics();

        boolean fill = false;
        Colour colour = AWTPolygonDrawer.Defaults.COLOUR;

        PolygonMods mod = this.getMod();
        if( mod != null )
        {
            fill = mod.fill.get();
            colour = mod.colour.get();
        }

        colour.fillG2D(g2d);

        if( fill )
            g2d.fillPolygon(xs, ys, Math.min(xs.length, ys.length));
        else
            g2d.drawPolygon(xs, ys, Math.min(xs.length, ys.length));


    }
}