package dev.ramar.e2.rendering.awt.drawing.stateless;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;

import dev.ramar.e2.rendering.awt.drawing.stateless.*;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.awt.AWTWindow;

import java.util.*;


import java.awt.Graphics2D;


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
}