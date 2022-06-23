package dev.ramar.e2.rendering.awt.drawing.rect;

import dev.ramar.e2.rendering.drawing.rect.RectDrawer;
import dev.ramar.e2.rendering.drawing.rect.RectMods;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import java.awt.Graphics2D;

import java.awt.Stroke;
import java.awt.BasicStroke;

/*
RectDrawer: AWTRectDrawer
 - AWT rect drawer, draws rect to <this.vp>.getGraphics() 
*/
public class AWTRectDrawer extends RectDrawer
{
    public static class Defaults
    {
        public static final Colour COLOUR = new Colour(255, 255, 255, 255);
    }

    public AWTRectDrawer() {}


    // ok. so. realistically. we could change this to draw to a Graphics2D, and have
    // an alternate interface for information if it needs it.
    // the ability to render to a VolatileImage or a BufferedImage sounds fkn sick tho.
    private AWTViewPort vp = null;

    public AWTRectDrawer withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
            this.vp = vp;

        return this;
    }



    /* Overidden Methods
    --===------------------
    */


    public void pospos(double x1, double y1, double x2, double y2)
    {
        this.poslen(x1, y1, x2 - x1, y2 - y1);
    }


    public void poslen(double x, double y, double w, double h)
    {
        if( this.vp != null )
        {
            Graphics2D g2d = this.vp.getGraphics();

            //// setup default values

            double originX = x,
                   originY = y;

            Colour colour = AWTRectDrawer.Defaults.COLOUR;
            boolean fill = false;
            Stroke stroke = g2d.getStroke();

            //// try to modify the default values
            RectMods rm = this.getMod();
            if( rm != null )
            {
                originX += rm.offset.getX();
                originY += rm.offset.getY();

                colour = rm.colour.get();
                fill = rm.fill.get();
                stroke = rm.stroke.get();
            }

            //// draw what we have!

            // g2d.setColor(...)
            colour.fillG2D(g2d);

            Stroke oldStroke = g2d.getStroke();
            if( oldStroke != stroke )
                g2d.setStroke(stroke);

            g2d.drawRect((int)originX, (int)originY, (int)w, (int)h);
            if( fill )
                g2d.fillRect((int)originX, (int)originY, (int)w, (int)h);

            if( oldStroke != stroke )
                g2d.setStroke(oldStroke);
        }
    }   
}