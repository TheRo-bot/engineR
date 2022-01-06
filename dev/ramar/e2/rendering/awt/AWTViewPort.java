package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;
import dev.ramar.e2.rendering.drawing.stateful.*;

import dev.ramar.e2.structures.WindowSettings;
import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.EngineR2;


import java.util.*;

import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import java.awt.geom.AffineTransform;

public class AWTViewPort extends ViewPort
{
    private static Color DEFAULT_COLOR = new Color(0, 0, 0, 255);

    private Vec2 worldCenter = new Vec2(0, 0);

    public final Rect BACKGROUND = new Rect(0, 0, 2000, 2000)
        .withColour(0, 0, 0, 255)
        .withFill()
    ;

    public AWTViewPort()
    {
        super(new AWTDrawManager(), new AWTWindow());
        ((AWTDrawManager)draw).withViewPort(this);
        ((AWTWindow)window).withViewPort(this);
    }

    public AWTWindow getAWTWindow()
    {
        return (AWTWindow)window;
    }

    public void doEngineStuff(EngineR2 er)
    {
        /** TO DO **/
        /*
        Create an FPS counter debug command
        */
    }


    /* World Center related abstract methods
    -===---------------------------------------
    */

    // Accessors
    @Override
    public double getCenterX()
    {
        return worldCenter.getX();
    }

    @Override
    public double getCenterY()
    {
        return worldCenter.getY();
    }


    public Graphics2D getGraphics()
    {
        if( draw != null && draw.stateless != null )
            return ((AWTStatelessDrawer)draw.stateless).getGraphics();
        return null;
    }


    // Modifiers
    @Override
    public void moveCenterX(double x)
    {
        worldCenter.setX(worldCenter.getX() + x);
    }


    @Override
    public void moveCenterY(double y)
    {
        worldCenter.setY(worldCenter.getY() + y);
    }



    // Mutators
    @Override
    public void setCenterX(double x)
    {
        worldCenter.setX(x);
    }


    @Override
    public void setCenterY(double y)
    {
        worldCenter.setY(y);
    }

    private int lWidth = -1, lHeight = -1;

    // private AffineTransform at = new AffineTransform();


    public int getLogicalWidth()
    {
        return lWidth;
        // return (int)(at.getScaleX() * window.width());
    }


    public int getLogicalHeight()
    {
        return lHeight;
        // return (int)(at.getScaleY() * window.height());
    }



    public void setLogicalWidth(int w)
    {
        if( w <= 0 )
            throw new IllegalArgumentException("AWTViewport doesn't support < 1 widths");
        synchronized(this)
        {
            lWidth = w;
        }
    }


    public void setLogicalHeight(int h)
    {
        if( h <= 0 )
            throw new IllegalArgumentException("AWTViewport doesn't support < 1 heights");
        synchronized(this)
        {
            lHeight = h;
        }
    }



    public void init()
    {
        window.onClose.add(() ->
        {
            stop();
        });
        super.init();
    }


    @Override
    public void init(int screenW, int screenH, String title, FullscreenState fs)
    {
        init();
        window.resize(screenW, screenH);
        window.setTitle(title);
        window.setFullscreenState(fs);
        window.init();
        
        setLogicalWidth(screenW);
        setLogicalHeight(screenH);
        window.onResize.add((int w, int h) ->
        {
            BACKGROUND.setW(w);
            BACKGROUND.setH(h);
        });

        Graphics2D g2d = getAWTWindow().getDrawGraphics();

        g2d.setColor(new Color(0, 0, 0, 255));
        g2d.fillRect(0, 0, screenW, screenH);

    }


    private Thread inner;



    @Override
    public void start()
    {
        inner = new Thread(() -> 
        {

            long lastTime = System.currentTimeMillis();
            int timeToSecond = 0, frameCount = 0;
            while(! inner.isInterrupted() )
            {
                render();
                frameCount++;

                long thisTime = System.currentTimeMillis();
                long diffTime = thisTime - lastTime;
                timeToSecond += diffTime;
                if( timeToSecond >= 1000 )
                {
                    frameCount = 0;
                    timeToSecond = 0;
                }

                lastTime = thisTime;
            }
        });
        inner.start();
        super.start();
    }


    @Override
    public void stop()
    {
        long time = System.currentTimeMillis();
        if( inner != null )
        {
            inner.interrupt();
            try
            {
                inner.join();
            }
            catch(InterruptedException e) {}
        }
        super.stop();
        
        System.out.println("[VP] stopped! (took " + (System.currentTimeMillis() - time) + "ms)");

    }


    @Override
    public void interrupt()
    {
        super.interrupt();
    }


    public AWTStatelessDrawer getAWTLess()
    {
        return (AWTStatelessDrawer)draw.stateless;
    }


    /* Rendering Methods
    -==--------------------
    */

    private void render()
    {
        if( window.isRenderable() )
        {
            BufferStrategy bs = getAWTWindow().getBufferStrategy();

            Graphics2D g2d = getAWTWindow().getDrawGraphics();

            if( g2d != null )
            {
                getAWTLess().setupDrawing(g2d);

                AffineTransform at = new AffineTransform();
                synchronized(this)
                {
                    double scaleX = 1, scaleY = 1;
                    if( lWidth > 0 )
                        scaleX = (double)window.width() / (double)lWidth;

                    if( lHeight > 0 )
                        scaleY = (double)window.height() / (double)lHeight;

                    // System.out.println("(" + scaleX + ", " + scaleY + ") scaling (" + window.width() + ", " + window.height() + ") to (" + (window.width() * scaleX) + ", " + (window.height() * scaleY) + ")");
                    at.scale(scaleX, scaleY); 
                }


                g2d.setTransform(at);

                double offX = worldCenter.getX() + getLogicalWidth() / 2,
                       offY = worldCenter.getY() + getLogicalHeight() / 2;

                BACKGROUND.drawAt(0, 0, this);

                draw.stateless.rect.withMod()
                    .withColour(0, 0, 0, 255)
                    .withFill()
                ;

                draw.stateless.rect.poslen(0, 0, getLogicalWidth(), getLogicalHeight());

                draw.stateless.drawAt(offX, offY, this);
                draw.stateful.drawAt(offX, offY, this);

                getAWTLess().shutdownDrawing();
                g2d.dispose();
                bs.show();
            }
        }
    }



}