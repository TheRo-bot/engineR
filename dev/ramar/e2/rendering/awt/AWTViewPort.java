package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;

import java.util.*;

import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;


public class AWTViewPort extends ViewPort
{
    private static Color DEFAULT_COLOR = new Color(0, 0, 0, 255);


    public AWTViewPort()
    {
        super(new AWTDrawManager(), new AWTWindow());
        ((AWTDrawManager)draw).withViewPort(this);
    }

    public AWTWindow getAWTWindow()
    {
        return (AWTWindow)window;
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
    private boolean closed = false;

    public void waitForClose()
    {
        if( inner != null )
        {
            try
            {
                while(!closed)
                {
                    Thread.sleep(100);
                }
            }
            catch(InterruptedException e) {}
        }
    }


    @Override
    public void start()
    {
        System.out.println("start!");
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
        g2d.setColor(DEFAULT_COLOR);
        g2d.fillRect(0, 0, window.width(), window.height());
        draw.stateless.drawAt(0, 0, draw);

        getAWTLess().shutdownDrawing();
        g2d.dispose();
        bs.show();
    }





}