package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.*;

import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.drawing.stateful.*;

import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygon;

import java.io.*;

import java.net.*;
import java.util.*;


import dev.ramar.e2.rendering.drawing.polyline.Polyline;
import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolylineDrawer;
import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolyline;

import dev.ramar.e2.rendering.awt.drawing.polyline.Shapeline;

import dev.ramar.e2.rendering.drawing.polygon.Polygon;
import dev.ramar.e2.rendering.drawing.rect.Rect;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import dev.ramar.e2.rendering.awt.AWTImage;

import java.awt.geom.Path2D;

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
            .withSize(1280, 720)
            .withFullscreenState(FullscreenState.WINDOWED)
            .withTitle("EngineR2 Main")
        );
        AWTPolylineDrawer pline = (AWTPolylineDrawer)e2.viewport.draw.polyline;            

        AWTPolygon pg = new AWTPolygon()
            .addPoint(-30, -30)
            .addPoint(-30, 30)
            .addPoint(-60, 30)
        ;

        pg.mods
            .colour.with(0, 255, 0, 255)
            .fill.with()
        ;

        Shapeline sl = new Shapeline()
            .addPoint(3, 3)
            .addPoint(0, 30)
        ;

        Thread t = new Thread(() -> 
        {
            try
            {
                while(true)
                {
                    for(int ii = 0; ii < sl.size(); ii++ )
                    {
                        sl.modPoint(ii, rd.nextDouble() - 0.5, rd.nextDouble() - 0.5);
                        pg.modPoint(ii, rd.nextDouble() - 0.5, rd.nextDouble() - 0.5);
                    }

                    Thread.sleep(10);

                    if( rd.nextDouble() > 0.4 )
                    {
                        sl.addPoint(rd.nextInt(300) - 150, rd.nextInt(300) - 150);
                        pg.addPoint(rd.nextInt(300) - 150, rd.nextInt(300) - 150);
                    }
                    else if( sl.size() > 2 )
                    {
                        sl.removePoint(rd.nextInt(Math.max(0, sl.size() - 1)));
                        pg.removePoint(rd.nextInt(Math.max(0, sl.size() - 1)));
                    }
                }
            }
            catch(InterruptedException e) {}
        });

        t.start();

        e2.viewport.window.onClose.add(() ->
        {
            t.interrupt();
        });


        e2.viewport.layers.mid.add(sl);
        e2.viewport.layers.mid.add(pg);
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