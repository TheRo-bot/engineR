package dev.ramar.e2;

import dev.ramar.e2.structures.*;

import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.Window.FullscreenState;

import java.io.*;

import java.net.*;
import java.util.*;


import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import dev.ramar.e2.rendering.awt.AWTImage;


import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

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
        List<EngineR2> instances = new ArrayList<>();

        for( int ii = 0; ii < 1; ii++ )
        {
            e2 = new EngineR2();
            e2.initialise(e2.setup()
                .withSize(1280, 720)
                .withFullscreenState(FullscreenState.WINDOWED)
                .withTitle("EngineR2 Main")
            );
            instances.add(e2);
        }

        e2.viewport.draw.layered.layers.mid.add(new Drawable()
        {
            private List<Vec2> vecs = new LinkedList<>();
            Vec2[] used = new Vec2[0];
            private double timer = 0.01;
            private double delta = timer;
            private long lastTime = System.currentTimeMillis();
            private int max = 500;
            private Random rd = new Random();

            public double rangeX(ViewPort vp)
            {
                return rd.nextInt(max) - max / 2.0  + rd.nextDouble();
            }

            public double rangeY(ViewPort vp)
            {
                return rd.nextInt(max) - max / 2.0 + rd.nextDouble();
            }


            public void drawAt(double x, double y, ViewPort vp)
            {
                long currTime = System.currentTimeMillis();

                delta -= (currTime - lastTime) / 1000.0;
                this.lastTime = currTime;

                if( delta <= 0 )
                {
                    delta = timer;
                    if( rd.nextDouble() >= 0.9 )
                        max = rd.nextInt(1000) + 1;

                    vecs.add(new Vec2(rangeX(vp), rangeY(vp)));
                    used = new Vec2[vecs.size()];
                    vecs.toArray(used);

                    // for( Vec2 v : used )
                    // {
                    //     int xFlip = rd.nextBoolean() ? 1 : -1,
                    //         yFlip = rd.nextBoolean() ? 1 : -1;

                    //     v.add((rd.nextInt(1) + rd.nextDouble()) * xFlip, (rd.nextInt(1) + rd.nextDouble()) * yFlip);
                    // }
                }

                vp.draw.layered.polygon.withMod()
                    .colour.with(255, 0, 0, 255)
                    .thickness.with(2)
                    .fill.with()
                    .offset.with(vp.getLogicalWidth() / 2, vp.getLogicalHeight() / 2)
                    // .cap.with(dev.ramar.e2.rendering.drawing.CapStyle.Round)
                    // .join.with(dev.ramar.e2.rendering.drawing.JoinStyle.Round)
                ;

                vp.draw.layered.polygon.points(used);

                vp.draw.layered.polygon.clearMod();
            }
        });


        // this is a test !
        TestDemos td = new TestDemos(instances);
    }

    private boolean allDone = false;
    public void waitForClose()
    {
        try
        {
            e2.viewport.window.onClose.add(() ->
            {
                // try
                // {
                //     dev.ramar.e2.demos.combat.DeltaUpdater.getInstance().close();
                // }
                // catch(InterruptedException e) 
                // {
                //     System.out.println("Couldn't wait for deltaupdater to close!!");
                // }
                allDone = true;
            });

            while(! allDone )
                Thread.sleep(100);
        }
        catch(InterruptedException e) {}
        System.out.println("!!! closing");
    }
}