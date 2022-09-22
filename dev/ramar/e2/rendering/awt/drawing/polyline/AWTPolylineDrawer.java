package dev.ramar.e2.rendering.awt.drawing.polyline;

import dev.ramar.e2.rendering.awt.AWTViewport;

import dev.ramar.e2.rendering.drawing.polyline.*;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Graphics2D;

public class AWTPolylineDrawer extends PolylineDrawer
{
    public static class Defaults
    {
        public static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
    }

    public AWTPolylineDrawer() {}


    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }


    public void points(double... points)
    {
        // get an offset!
        double offX = 0,
               offY = 0;

        PolylineMods pm = this.getMod();
        if( pm != null )
        {
            offX = pm.getOffX();
            offY = pm.getOffY();
        }

        // fill the arrays!
        int[] xs = new int[points.length / 2];
        int[] ys = new int[points.length / 2];

        int count = 0;
        for( int ii = 0; ii < points.length; ii += 2)
        {
            xs[count] = (int)(points[ii    ] + offX);
            ys[count] = (int)(points[ii + 1] + offY);
            count++;
        }

        this.absolutePoints(xs, ys);
    }


    public void points(Vec2... points)
    {
        // get an offset!
        double offX = 0,
               offY = 0;

        PolylineMods pm = this.getMod();
        if( pm != null )
        {
            offX = pm.getOffX();
            offY = pm.getOffY();
        }

        // fill the arrays!
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];

        int count = 0;
        for( Vec2 point : points )
        {
            xs[count] = (int)(point.getX() + offX);
            ys[count] = (int)(point.getY() + offY);
            count++;
        }

        this.absolutePoints(xs, ys);
    }



    public void offsets(double... offsets)
    {
        // get an offset!
        double offX = 0,
               offY = 0;

        PolylineMods pm = this.getMod();
        if( pm != null )
        {
            offX = pm.getOffX();
            offY = pm.getOffY();
        }

        // fill the arrays!
        int[] xs = new int[offsets.length / 2 + 1];
        int[] ys = new int[offsets.length / 2 + 1];

        xs[0] = (int)offX;
        ys[0] = (int)offY;
        int count = 1;
        for( int ii = 0; ii < offsets.length; ii += 2 )
        {
            offX += offsets[ii    ];
            offY += offsets[ii + 1];

            xs[count] = (int)(offX);
            ys[count] = (int)(offY);
            count++;
        }

        this.absolutePoints(xs, ys);
    }


    public void offsets(Vec2... offsets)
    {
        // get an offset!
        double offX = 0,
               offY = 0;

        PolylineMods pm = this.getMod();
        if( pm != null )
        {
            offX = pm.getOffX();
            offY = pm.getOffY();
        }

        // fill the arrays!
        int[] xs = new int[offsets.length + 1];
        int[] ys = new int[offsets.length + 1];

        xs[0] = (int)offX;
        ys[0] = (int)offY;
        int count = 1;
        for( Vec2 off : offsets )
        {
            offX += off.getX();
            offY += off.getY();
            
            xs[count] = (int)(offX);
            ys[count] = (int)(offY);
            count++;
        }

        this.absolutePoints(xs, ys);
    }


    public void absolutePoints(int[] xs, int[] ys)
    {
        Graphics2D g2d = this.graphics;

        PolylineMods mods = this.getMod();

        Colour colour = AWTPolylineDrawer.Defaults.DEFAULT_COLOUR;

        float thickness = 1.0f;
        int capStyle = BasicStroke.CAP_ROUND;
        int joinStyle = BasicStroke.JOIN_BEVEL;
        float miter = 10.0f;

        if( mods != null )
        {
            colour = mods.getColour();
            
            thickness = mods.getThickness();
            capStyle = mods.getCapStyle().intify();
            joinStyle = mods.getJoinStyle().intify();
            miter = mods.getMiter();
        }

        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(thickness, capStyle, joinStyle, miter));

        colour.fillG2D(g2d);

        g2d.drawPolyline(xs, ys, xs.length);

        g2d.setStroke(old);
    }

}