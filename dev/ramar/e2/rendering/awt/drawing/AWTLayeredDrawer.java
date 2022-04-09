package dev.ramar.e2.rendering.awt.drawing;

import dev.ramar.e2.rendering.drawing.LayeredDrawer;
import dev.ramar.e2.rendering.drawing.LayeredDrawer.LayeredBuilder;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.awt.AWTWindow;

import java.util.*;

import dev.ramar.e2.rendering.awt.drawing.rect.AWTRectDrawer;
import dev.ramar.e2.rendering.awt.drawing.text.AWTTextDrawer;
import dev.ramar.e2.rendering.awt.drawing.image.AWTImageDrawer;

import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygonDrawer;
import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolylineDrawer;


import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;


public class AWTLayeredDrawer extends LayeredDrawer
{

    private AWTViewPort vp;

    public AWTLayeredDrawer()
    {
        super(new LayeredBuilder()
                .withRect(     new AWTRectDrawer() )
                .withText(     new AWTTextDrawer() )
               .withImage(    new AWTImageDrawer() )
             .withPolygon(  new AWTPolygonDrawer() )
            .withPolyline( new AWTPolylineDrawer() )
        );
    }


    public void withViewPort(AWTViewPort avp)
    {
        if( vp == null )
        {
            vp = avp;
            ((AWTRectDrawer)rect).  withViewPort(vp);
            ((AWTImageDrawer)image).withViewPort(vp);
            ((AWTTextDrawer)text).  withViewPort(vp);
            ((AWTPolygonDrawer)polygon).withViewPort(vp);
            ((AWTPolylineDrawer)polyline).withViewPort(vp);
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