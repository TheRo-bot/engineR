package dev.ramar.e2.rendering.awt.drawing.polygon;

import dev.ramar.e2.rendering.awt.AWTViewport;

import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.polygon.*;


import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;


public class AWTPolygonDrawer extends PolygonDrawer
{
    public static class Defaults
    {
        public static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
    }


    public AWTPolygonDrawer() 
    {

    }

    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }



    public void points(Vec2... points)
    {
        int[] xs = new int[points.length];
        int[] ys = new int[points.length];

        double offX = 0,
               offY = 0;

        PolygonMods mods = this.getMod();
        if( mods != null )
        {
            offX = mods.offset.getX();
            offY = mods.offset.getY();
        }

        int ii = 0;
        for( Vec2 point : points )
        {
            xs[ii] = (int)(point.getX() + offX);
            ys[ii] = (int)(point.getY() + offY);
            ii++;
        }

        this.absolutePoints(xs, ys);
    }

    public void offsets(Vec2... offsets)
    {
        double offX = 0,
               offY = 0;

        PolygonMods mods = this.getMod();
        if( mods != null )
        {
            offX = mods.offset.getX();
            offY = mods.offset.getY();
        }

        int[] xs = new int[offsets.length],
              ys = new int[offsets.length];

        int count = 0;
        for( Vec2 v : offsets )
        {
            offX += v.getX();
            offY += v.getY();
            xs[count] = (int)offX;
            ys[count] = (int)offY;
            count++;
        }

        this.absolutePoints(xs, ys);
    }


    public void points(double... points)
    {
        int[] xs = new int[points.length / 2];
        int[] ys = new int[points.length / 2];


        double offsetX = 0;
        double offsetY = 0;

        PolygonMods mods = this.getMod();
        if( mods != null )
        {
            offsetX += mods.offset.getX();
            offsetY += mods.offset.getY();
        }

        int count = 0;
        for( int ii = 0; ii < points.length; ii += 2)
        {
            xs[count] = (int)(points[ii]     + offsetX);
            ys[count] = (int)(points[ii + 1] + offsetY);
            count++;
        }
        System.out.println(java.util.Arrays.toString(xs) + " || " + java.util.Arrays.toString(ys));

        this.absolutePoints(xs, ys);
    }

    public void offsets(double... offsets)
    {
        int[] xs = new int[offsets.length / 2 + 1];
        int[] ys = new int[offsets.length / 2 + 1];

        double offsetX = 0;
        double offsetY = 0;

        PolygonMods mods = this.getMod();
        if( mods != null )
        {
            offsetX += mods.offset.getX();
            offsetY += mods.offset.getY();
        }

        xs[0] = (int)offsetX;
        ys[0] = (int)offsetY;
        int count = 1;
        for(int ii = 0; ii < offsets.length; ii += 2 )
        {
            offsetX += offsets[ii    ];
            offsetY += offsets[ii + 1];
            xs[count] = (int)offsetX;
            ys[count] = (int)offsetY;
            count++;
        }

        this.absolutePoints(xs, ys);
    }


    public void absolutePoints(int xs[], int ys[])
    {
        PolygonMods mod = this.getMod();

        Graphics2D g2d = this.graphics;

        // offset should be handled by prior methods

        boolean fill = false;
        Colour colour = AWTPolygonDrawer.Defaults.DEFAULT_COLOUR;

        float thickness = 1.0f;
        int capStyle = BasicStroke.CAP_ROUND;
        int joinStyle = BasicStroke.JOIN_BEVEL;
        float miter = 10.0f;


        if( mod != null )
        {
            colour = mod.colour.get();   
            fill = mod.fill.get();

            thickness = (float)mod.thickness.get();
            capStyle = mod.cap.get().intify();
            joinStyle = mod.join.get().intify();
            miter = mod.miter.get();
        }

        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(thickness, capStyle, joinStyle, miter));

        colour.fillG2D(g2d);


        if( fill )
            g2d.fillPolygon(xs, ys, xs.length);
        else
            g2d.drawPolygon(xs, ys, xs.length);


        g2d.setStroke(old);
    }

}