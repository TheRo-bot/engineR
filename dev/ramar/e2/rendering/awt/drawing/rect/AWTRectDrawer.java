package dev.ramar.e2.rendering.awt.drawing.rect;

import dev.ramar.e2.rendering.drawing.rect.*;

import dev.ramar.e2.rendering.awt.AWTViewPort;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import java.awt.Stroke;
import java.awt.BasicStroke;

import java.awt.Graphics2D;

public class AWTRectDrawer extends RectDrawer
{

    public static class Defaults
    {
        public static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
        public static final int CAP_STYLE = BasicStroke.CAP_ROUND;
        public static final int JOIN_STYLE = BasicStroke.JOIN_ROUND;
        public static final float MITER = 10.0f;
    }

    public AWTRectDrawer()
    {

    }

    private AWTViewPort vp = null;

    public AWTRectDrawer withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        return this;
    }


    public void pospos(double x1, double y1, double x2, double y2)
    {
        this.poslen(x1, y1, x2 - x1, y2 - y1);

    }

    public void poslen(double x, double y, double w, double h)
    {
        Graphics2D g2d = this.vp.getGraphics();
        RectMods mod = this.getMod();

        boolean fill = false;
        Colour colour = AWTRectDrawer.Defaults.DEFAULT_COLOUR;

        int thickness = 1;

        if( mod != null )
        {
            x = mod.offset.getX();
            y = mod.offset.getY();

            colour = mod.colour.get();
        }   

        colour.fillG2D(g2d);

        g2d.setStroke(new BasicStroke(thickness, AWTRectDrawer.Defaults.CAP_STYLE, AWTRectDrawer.Defaults.JOIN_STYLE, AWTRectDrawer.Defaults.MITER));

        if( fill )
            g2d.fillRect((int)x, (int)y, (int)w, (int)h);
        else
            g2d.drawRect((int)x, (int)y, (int)w, (int)h);
    }
}