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
            .withSize(1280, 720)
            .withFullscreenState(FullscreenState.WINDOWED)
            .withTitle("EngineR2 Main")
        );

        final Random rd = new Random();
        e2.viewport.layers.mid.queueAdd((double x, double y, ViewPort vp) ->
        {
            vp.draw.rect.withMod()
                .colour.with(255, 255, 0, 255)
                .offset.with(x, y)
                // .offset.with(rd.nextInt(100) - 50, rd.nextInt(100) - 50)
                .fill.with()
            ;

            vp.draw.rect.pospos(10, 10, 20, 20);
            vp.draw.rect.getMod()
                .offset.with(0, 40)
            ;
            vp.draw.rect.poslen(10, 10, 10, 10);

            vp.draw.rect.clearMod();
        });

        final Vec2[] points = new Vec2[]
        {
            new Vec2(0, 0),     new Vec2(50, -10), new Vec2(100, 0),
            new Vec2(100, 100),                    new Vec2(0, 100),

            new Vec2(50, 50)
        };

        final Vec2[] offsets = new Vec2[]
        {
            new Vec2(0, 0),     new Vec2(50, -10), new Vec2(50, 10),
            new Vec2(0, 100),                    new Vec2(-100, 0),

            new Vec2(50, -50)
        };

        e2.viewport.layers.mid.queueAdd((double x, double y, ViewPort vp) ->
        {
            vp.draw.polygon.withMod()
                .offset.with(x, y)
                .colour.with(150, 150, 0, 255)
                .fill.with()
            ;

            vp.draw.polygon.points(points);

            vp.draw.polygon.getMod()
                .offset.with(0, 150)
            ;

            vp.draw.polygon.offsets(offsets);

            vp.draw.polygon.clearMod();
        });
        // e2.viewport.layers.mid.queueAdd(new Drawable()
        Drawable d = new Drawable()
        {
            List<Vec2> vecs = new ArrayList<>();
            Vec2[] curr = null;
            private double timer = 0.02;
            private double countdown = 0.0;
            private long lastTime = System.currentTimeMillis();

            public void drawAt(double x, double y, ViewPort vp)
            {
                long thisTime = System.currentTimeMillis();
                countdown -= (thisTime - lastTime) / 1000.0;
                lastTime = thisTime;


                if( this.countdown <= 0.0 )
                {
                    this.countdown = this.timer;
                    if( rd.nextDouble() <= 0.9 )
                    {
                        vecs.add(new Vec2(rd.nextInt((int)(vp.window.width() * 0.4)), rd.nextInt((int)(vp.window.height() * 0.4))));
                        curr = vecs.toArray(new Vec2[vecs.size()]);
                    }
                }

                if( curr != null )
                {
                    vp.draw.polygon.withMod()
                        .offset.with(x, y)
                        .colour.with(0, 255, 255, 255)
                        .fill.with()
                    ;

                    vp.draw.polygon.points(curr);

                    vp.draw.polygon.clearMod();               
                }
            }
        };
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