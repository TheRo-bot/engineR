package dev.ramar.e2.awt.drawing.rect;

import dev.ramar.e2.core.drawing.rect.*;

import dev.ramar.e2.awt.rendering.AWTViewport;


import dev.ramar.e2.core.structures.Vec2;
import dev.ramar.e2.core.structures.Colour;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;

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


    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }


    public void pospos(double x1, double y1, double x2, double y2)
    {
        this.poslen(x1, y1, x2 - x1, y2 - y1);

    }

    public void poslen(double x, double y, double w, double h)
    {
        Graphics2D g2d = this.graphics;
        if( g2d == null )
            return;

        RectMods mod = this.getMod();

        boolean fill = false;
        Colour colour = AWTRectDrawer.Defaults.DEFAULT_COLOUR;

        int thickness = 1;

        if( mod != null )
        {
            x += mod.offset.getX();
            y += mod.offset.getY();

            colour = mod.colour.get();
            fill = mod.fill.get();
        }   

        colour.fillG2D(g2d);

        AffineTransform origTrans = g2d.getTransform();
        AffineTransform at = new AffineTransform(origTrans);
        at.translate(x, y);


        Stroke origStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(thickness, AWTRectDrawer.Defaults.CAP_STYLE, AWTRectDrawer.Defaults.JOIN_STYLE, AWTRectDrawer.Defaults.MITER));
        g2d.setTransform(at);

        if( fill )
            g2d.fillRect(0, 0, (int)w, (int)h);
        else
            g2d.drawRect(0, 0, (int)w, (int)h);

        g2d.setTransform(origTrans);
        g2d.setStroke(origStroke);
    }
}