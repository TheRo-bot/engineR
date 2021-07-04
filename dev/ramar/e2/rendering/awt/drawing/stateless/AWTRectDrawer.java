package dev.ramar.e2.rendering.awt.drawing.stateless;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

import java.awt.Color;

import java.awt.Graphics2D;



public class AWTRectDrawer extends RectDrawer
{
    private AWTViewPort vp;

    public AWTRectDrawer()
    {
    }


    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
        }
    }

    public Graphics2D getViewPortGraphics()
    {
        if( vp == null )
            throw new NullPointerException("Viewport not set. RectDrawer isn't setup to draw right now!");

        return ((AWTStatelessDrawer)vp.draw.stateless).getGraphics();
    }


    @Override
    public void pospos(double x1, double y1, double x2, double y2)
    {
        poslen(x1, y1,x2 - x1,y2 - y1);
    }  


    public void poslen(double x, double y, double w, double h)
    {
        // get the graphics from the viewport
        // (if we're not meant to draw now, this is what'll throw
        //  an exception)
        Graphics2D g2d = getViewPortGraphics();

        // setup default behaviour (x, y, w, h are already default-ed coords)
        boolean fill = false;
        RectMods rm = getMod();

        if( rm != null )
        {
            x = rm.modX(x);
            y = rm.modY(y);

            fill = rm.doFill();

            if( rm.hasColour() )
                g2d.setColor(new Color(rm.getR(), rm.getG(), rm.getB(), rm.getA()));
            else
                g2d.setColor(new Color(DEFAULT_COLOR.getRed(),
                                       DEFAULT_COLOR.getGreen(),
                                       DEFAULT_COLOR.getBlue(),
                                       DEFAULT_COLOR.getAlpha()));
        }


        if( fill )
            g2d.fillRect((int)x, (int)y, (int)w, (int)h);   
        else
            g2d.drawRect((int)x, (int)y, (int)w, (int)h);   

    }


}