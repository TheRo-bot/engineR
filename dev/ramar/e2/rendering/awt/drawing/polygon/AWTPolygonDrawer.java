package dev.ramar.e2.rendering.awt.drawing.polygon;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.polygon.*;


import java.awt.Graphics2D;

public class AWTPolygonDrawer extends PolygonDrawer
{
    public static class Defaults
    {
        public static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
    }


    public AWTPolygonDrawer() 
    {

    }

    private AWTViewPort vp = null;


    public AWTPolygonDrawer withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        return this;
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
            offX = mods.getOffX();
            offY = mods.getOffY();
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
            offX = mods.getOffX();
            offY = mods.getOffY();
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
            offsetX += mods.getOffX();
            offsetY += mods.getOffY();
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
            offsetX += mods.getOffX();
            offsetY += mods.getOffY();
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

        Graphics2D g2d = this.vp.getGraphics();

        // offset should be handled by prior methods

        boolean fill = false;
        Colour colour = AWTPolygonDrawer.Defaults.DEFAULT_COLOUR;

        if( mod != null )
        {
            colour = mod.getColour();   
            fill = mod.getFill();
        }

        colour.fillG2D(g2d);


        if( fill )
            g2d.fillPolygon(xs, ys, xs.length);
        else
            g2d.drawPolygon(xs, ys, xs.length);
    }

}