package dev.ramar.e2.rendering.awt.drawing.stateless;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;

import dev.ramar.e2.rendering.awt.drawing.stateless.*;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.awt.AWTWindow;

import java.util.*;



import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;


public class AWTStatelessDrawer extends StatelessDrawer
{

    private AWTViewPort vp;

    public AWTStatelessDrawer()
    {
        super(new AWTRectDrawer() ,
              new AWTImageDrawer(),
              new AWTTextDrawer() ,
              new AWTLineDrawer());
    }


    public void withViewPort(AWTViewPort avp)
    {
        if( vp == null )
        {
            vp = avp;
            ((AWTRectDrawer)rect).  withViewPort(vp);
            ((AWTImageDrawer)image).withViewPort(vp);
            ((AWTTextDrawer)text).  withViewPort(vp);
            ((AWTLineDrawer)line).  withViewPort(vp);
        }
    }


    private Graphics2D graphics;

    public Graphics2D getGraphics()
    {   return graphics;   }


    public void setupDrawing(Graphics2D g2d)
    {
        synchronized(this)
        {
            graphics = g2d;
        }
    }


    public void shutdownDrawing()
    {
        synchronized(this)
        {
            graphics = null;
        }
    }

    protected AffineTransform topTransform = new AffineTransform();

    @Override
    protected void drawTop(double x, double y, ViewPort vp)
    {
        if( graphics != null )
        {
            AffineTransform bef = graphics.getTransform();

            graphics.setTransform(topTransform);

            super.drawTop(x, y, vp);

            graphics.setTransform(bef);
        }
    }
}