package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;

import dev.ramar.e2.structures.Vec2;

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





    @Override
    public void init(WindowSettings ws)
    {

        System.out.println("init!");
        getAWTWindow().init(ws);


        window.onClose.add(() ->
        {
            System.out.println("Window closed! stopping..");
            long time = System.currentTimeMillis();
            stop();
            System.out.println("stopped! (took " + (System.currentTimeMillis() - time) + "ms)");
        });

        Graphics2D g2d = getAWTWindow().getDrawGraphics();

        g2d.setColor(new Color(0, 0, 0, 255));

        g2d.fillRect(0, 0, ws.screenW, ws.screenH);

        super.init(ws);

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

                    System.out.println("FPS: " + frameCount);
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
        super.stop();
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
        BufferStrategy bs = getAWTWindow().getBufferStrategy();
        Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();


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


        draw.stateless.rect.withMod().withColour(0, 0, 0, 255).withFill();
        draw.stateless.rect.poslen(0, 0, window.width(), window.height());

        draw.stateless.drawAt(worldCenter.getX() + window.width() / 2, worldCenter.getY() + window.height() / 2, this);
        draw.stateful.drawAt(worldCenter.getX() + window.width() / 2, worldCenter.getY() + window.height() / 2, this);

        getAWTLess().shutdownDrawing();
        g2d.dispose();
        bs.show();
    }





}