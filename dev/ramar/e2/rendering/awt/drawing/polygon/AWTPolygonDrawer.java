package dev.ramar.e2.rendering.awt.drawing.polygon;


import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.rendering.drawing.polygon.PolygonMods;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.enums.CapStyle;
import dev.ramar.e2.rendering.drawing.enums.JoinStyle;

import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class AWTPolygonDrawer extends PolygonDrawer
{

    public static class Defaults
    {
        public static final Colour COLOUR = new Colour(255, 255, 255, 255);

        public static class Stroke
        {
            public static final float WIDTH = 1.0f;
            public static final float MITER = 10.0f;
            public static final CapStyle CAP = CapStyle.Round;
            public static final JoinStyle JOIN = JoinStyle.Round;
        }
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


    /* Overidden Methods
    --===------------------
    */


    public void points(double originX, double originY, double... points)
    {
        int[] xs = new int[points.length / 2 + 1];
        int[] ys = new int[points.length / 2 + 1];


        double offX = 0.0,
               offY = 0.0;


        int count = 0;
        for( int ii = 0; ii < points.length; ii += 2 )
        {
            xs[count] = (int)(points[ii] + offX);
            ys[count] = (int)(points[ii + 1] + offY);

            count++;
        }

        this.points(originX, originY, xs, ys);
    }


    public void points(double originX, double originY, Vec2... points)
    {
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];


        double offX = 0.0,
               offY = 0.0;


        int ii = 0;
        for( Vec2 v : points )
        {
            xs[ii] = (int)(v.getX() + offX);
            ys[ii] = (int)(v.getY() + offY);
            ii++;
        }

        this.points(originX, originY, xs, ys);
    }


    // offsets as in this offset = last offset + x, y
    public void offsets(double originX, double originY, double... offsets)
    {
        int[] xs = new int[offsets.length / 2 + 1];
        int[] ys = new int[offsets.length / 2 + 1];

        double x = 0.0,
               y = 0.0;

        int count = 0;
        for( int ii = 0; ii < offsets.length; ii += 2 )
        {
            x += offsets[ii];
            y += offsets[ii + 1];

            xs[count] = (int)x;
            ys[count] = (int)y;

            count++;
        }

        this.points(originX, originY, xs, ys);
    }


    public void offsets(double originX, double originY, Vec2... offsets)
    {
        int[] xs = new int[offsets.length];
        int[] ys = new int[offsets.length];

        double x = 0.0,
               y = 0.0;

        int ii = 0;
        for( Vec2 v : offsets )
        {
            x += v.getX();
            y += v.getY();

            xs[ii] = (int)x;
            ys[ii] = (int)y;
            ii++;
        }

        this.points(originX, originY, xs, ys);
    }




    public void points(double offX, double offY, int[] xs, int[] ys)
    {
        Graphics2D g2d = this.vp.getGraphics();

        boolean fill = false;
        Colour colour = AWTPolygonDrawer.Defaults.COLOUR;

        PolygonMods mod = this.getMod();
        if( mod != null )
        {
            offX += mod.offset.getX();
            offY += mod.offset.getY();

            fill = mod.fill.get();
            colour = mod.colour.get();
        }

        colour.fillG2D(g2d);

        AffineTransform trans = g2d.getTransform();
        // trans.translate(offX, offY);

        if( fill )
            g2d.fillPolygon(xs, ys, Math.min(xs.length, ys.length));
        else
            g2d.drawPolygon(xs, ys, Math.min(xs.length, ys.length));

        // trans.translate(-offX, offY);


    }



    public void points(double offX, double offY, AWTPolygon points)
    {
        Graphics2D g2d = this.vp.getGraphics();

        Colour colour = AWTPolygonDrawer.Defaults.COLOUR;
        boolean fill = false;

        PolygonMods mod = this.getMod();
        if( mod != null )
        {
            offX += mod.offset.getX();
            offY += mod.offset.getY();

            fill = mod.fill.get();
            colour = mod.colour.get();
        }

        colour.fillG2D(g2d);

        AffineTransform oldAT = g2d.getTransform();

        AffineTransform modAT = new AffineTransform(oldAT);
        modAT.translate(offX, offY);
        g2d.setTransform(modAT);

        if( fill )
            g2d.fill(points);
        else
            g2d.draw(points);

        g2d.setTransform(oldAT);


    }
}