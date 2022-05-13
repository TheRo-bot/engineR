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


import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygonDrawer;
import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygon;

import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolylineDrawer;
import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolyline;

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

        List<AWTPolygon> polygons = new ArrayList<>();

        int  width = 800,
            height = 300;

        for( int ii = 0; ii < 10; ii++ )
        {
            AWTPolygon pg = new AWTPolygon()
                .addPoint(rd.nextInt(width) - width / 2, rd.nextInt(height) - height / 2)
            ;

            int am = 3;

            for( int jj = 0; jj < am; jj++ )
            {
                pg.addPoint(rd.nextInt(width) - width / 2, rd.nextInt(height) - height / 2);
            }

            pg.mods
                .colour.with(255, 0, 0, 180)
            ;

            polygons.add(pg);
            e2.viewport.layers.mid.add(pg);
        }


        Thread t = new Thread(() -> 
        {
            try
            {
                while(true)
                {
                    for( AWTPolygon pg : polygons )
                        for( int ii = 0; ii < pg.size(); ii++ )
                            pg.modPoint(ii, rd.nextDouble() * 3 - 1.5, rd.nextDouble() * 3 - 1.5);

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