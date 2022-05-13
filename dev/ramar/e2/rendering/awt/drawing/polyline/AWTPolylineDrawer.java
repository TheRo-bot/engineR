package dev.ramar.e2.rendering.awt.drawing.polyline;

import dev.ramar.e2.rendering.drawing.polyline.PolylineDrawer;
import dev.ramar.e2.rendering.drawing.polyline.PolylineMods;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.enums.CapStyle;
import dev.ramar.e2.rendering.drawing.enums.JoinStyle;

import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;

import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;

public class AWTPolylineDrawer extends PolylineDrawer
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

    public AWTPolylineDrawer() {}


    // ok. so. realistically. we could change this to draw to a Graphics2D, and have
    // an alternate interface for information if it needs it.
    // the ability to render to a VolatileImage or a BufferedImage sounds fkn sick tho.
    private AWTViewPort vp = null;

    public AWTPolylineDrawer withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
            this.vp = vp;

        return this;
    }


    private Vec2 getOffset()
    {
        PolylineMods mod = this.getMod();
        if( mod != null )
            return mod.offset.get();

        return null;
    }


    /* Overidden Methods
    --===------------------
    */


    public void points(double originX, double originY, double... points)
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

        this.points(originX, originY, xs, ys);
    }


    public void points(double originX, double originY, Vec2... points)
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

        this.points(originX, originY, xs, ys);
    }


    // offsets as in this offset = last offset + x, y
    public void offsets(double originX, double originY, double... offsets)
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

        this.points(originX, originY, xs, ys);
    }


    public void offsets(double originX, double originY, Vec2... offsets)
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

        this.points(originX, originY, xs, ys);
    }


    public void points(int[] xs, int[] ys)
    {
        this.points(0, 0, xs, ys);
    }

    public void points(double xOff, double yOff, int[] xs, int[] ys)
    {
        Graphics2D g2d = this.vp.getGraphics();

        Colour colour = AWTPolylineDrawer.Defaults.COLOUR;

        float width = AWTPolylineDrawer.Defaults.Stroke.WIDTH;
        float miter = AWTPolylineDrawer.Defaults.Stroke.MITER;
        CapStyle cap = AWTPolylineDrawer.Defaults.Stroke.CAP;
        JoinStyle join = AWTPolylineDrawer.Defaults.Stroke.JOIN;

        PolylineMods mod = this.getMod();
        if( mod != null )
        {
            xOff += mod.offset.getX();
            yOff += mod.offset.getY();

            colour = mod.colour.get();

            width = mod.width.get();
            cap = mod.cap.get();
            join = mod.join.get();
            miter = mod.miter.get();
        }

        colour.fillG2D(g2d);

        Stroke old = g2d.getStroke();

        g2d.setStroke(new BasicStroke(width, cap.intify(), join.intify(), miter));

        AffineTransform at = g2d.getTransform();

        at.translate(xOff, yOff);
        g2d.setTransform(at);
        g2d.drawPolyline(xs, ys, Math.min(xs.length, ys.length));

        at.translate(-xOff, -yOff);
        g2d.setTransform(at);

        g2d.setStroke(old);
    }


    public Path2D genPath(Vec2... vecs) 
    {
        Path2D path = null;

        if( vecs.length > 0 )
        {
            path = new Path2D.Double();

            path.moveTo(vecs[0].getX(), vecs[0].getY());

            for(int ii = 1; ii < vecs.length; ii++ )
            {
                Vec2 curr = vecs[ii];
                path.lineTo(curr.getX(), curr.getY());
            }
        }


        return path;
    }

    public void points(double offX, double offY, AWTPolyline points)
    {
        Graphics2D g2d = this.vp.getGraphics();

        Colour colour = AWTPolylineDrawer.Defaults.COLOUR;

        float width = AWTPolylineDrawer.Defaults.Stroke.WIDTH;
        float miter = AWTPolylineDrawer.Defaults.Stroke.MITER;
        CapStyle cap = AWTPolylineDrawer.Defaults.Stroke.CAP;
        JoinStyle join = AWTPolylineDrawer.Defaults.Stroke.JOIN;

        PolylineMods mod = this.getMod();
        if( mod != null )
        {
            offX += mod.offset.getX();
            offY += mod.offset.getY();

            colour = mod.colour.get();

            width = mod.width.get();
            cap = mod.cap.get();
            join = mod.join.get();
            miter = mod.miter.get();
        }

        colour.fillG2D(g2d);

        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(width, cap.intify(), join.intify(), miter));

        AffineTransform oldAT = g2d.getTransform();

        AffineTransform modAT = new AffineTransform(oldAT);
        modAT.translate(offX, offY);
        g2d.setTransform(modAT);

        g2d.draw(points);

        g2d.setTransform(oldAT);

        g2d.setStroke(old);


    }
}