package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.*;

import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.drawing.stateful.*;

import java.io.*;

import java.net.*;
import java.util.*;

import dev.ramar.e2.rendering.drawing.polyline.Polyline;
import dev.ramar.e2.rendering.drawing.polygon.Polygon;
import dev.ramar.e2.rendering.drawing.rect.Rect;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import dev.ramar.e2.rendering.awt.AWTImage;


public class E2Main
{

    public static void main(String[] args)
    {
        new E2Main();
    }

	public static void setup()
	{
		Main.Entrypoints.addEntrypoint((String[] args) ->
        {
        	E2Main em = new E2Main();
            try
            {
                em.waitForClose();
            	System.out.println("E2 close!");
            }
            catch(Exception e) 
            {
                System.out.println("A fatal exception occurred:");
                e.printStackTrace();
                System.out.println("Waiting for window close");
                em.waitForClose();
            }
        });
	}

    EngineR2 e2;
    Random rd = new Random();


	public E2Main()
	{

        e2 = new EngineR2();
        e2.initialise(e2.setup()
            .withSize(1920, 1080)
            .withFullscreenState(FullscreenState.FULLSCREEN)
            .withTitle("EngineR2 Main")
        );

        //// setup the polygon!
        Polygon pg = new Polygon();
        pg.points
            .add(0, 0)
            .makeOffsets(false)
        ;
        pg.getMod()
            .colour.with(255, 0, 0, 125)
            .fill.with()
        ;
        e2.viewport.layers.mid.queueAdd(pg);


        //// setup the polyline!
        Polyline pl = new Polyline();
        pl.points
            .add(0, 0)
            .makeOffsets(false)
        ;

        pl.getMod()
            .colour.with(255, 0, 0, 255)
            .width.with(3)
        ;
        e2.viewport.layers.mid.queueAdd(pl);

        // make the thread that'll do what we want!
        Thread t = new Thread(() ->
        {
            try
            {
                long lastTime = System.currentTimeMillis();
                double countdown = 0.2;
                while(true)
                {
                    long currTime = System.currentTimeMillis();
                    countdown -= (currTime - lastTime) / 1000.0;
                    lastTime = currTime;
                    if( countdown <= 0 )
                    {
                        countdown = 0.2;
                        if( rd.nextDouble() > 0.2 )
                        {
                            int w = e2.viewport.window.width();
                            int h = e2.viewport.window.height();

                            double x = rd.nextInt((int)(w)) - w * 0.5;
                            double y = rd.nextInt((int)(h)) - h * 0.5;

                            pg.points.add(x, y);
                            pl.points.add(x, y);
                        }
                    }

                    for( int ii = 0; ii < pg.points.size(); ii++ )
                    {
                        double xOff = (rd.nextDouble() * 0.5) - 0.25;
                        double yOff = (rd.nextDouble() * 0.5) - 0.25;

                        pg.points.get(ii).add(xOff, yOff);
                        pl.points.get(ii).add(xOff, yOff);

                    }

                    Thread.sleep(1);
                }
            }
            catch(InterruptedException e) {}
        });

        t.start();

        e2.viewport.window.onClose.add(() ->
        {
            t.interrupt();
        });


    }


    public void waitForClose()
    {
        try
        {
            boolean allDone = false;
            while(! allDone )
            {
                allDone = e2.viewport.isRunning();
                Thread.sleep(50);
            }
        }
        catch(InterruptedException e) {}
        System.out.println("!!! closing");
    }
}