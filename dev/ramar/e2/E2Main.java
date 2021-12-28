package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.rendering.drawing.stateful.Rect;
import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.drawing.stateful.*;

import java.io.*;

import java.net.*;
import java.util.*;


import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;

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
    ViewPort vp;
    Random rd = new Random();


	public E2Main()
	{

        e2 = new EngineR2();
        e2.initialise(e2.setup()
            .withSize(1280, 720)
            .withFullscreenState(FullscreenState.WINDOWED)
            .withTitle("EngineR2 Main")
        );

        Tests tests = new Tests();
        tests.setup(e2.viewport);
        // tests.newKeyComboTest();
        // tests.consoleTest();

        vp = e2.viewport;

        vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        {
            vp.draw.stateless.rect.withMod()
                .withColour(255, 255, 255, 255)
                .withFill()
                .withOffset(x, y)
            ;
            vp.draw.stateless.rect.poslen(-10, -10, 20, 20);
        });




        KeyCombo kc = new KeyCombo("test").withChar('`').withTShift(Directionality.LEFT);



        // new Thread(() ->
        // {
        //     try
        //     {
        //         int sleepTime = 3000;
        //         e2.viewport.window.setFullscreenState(FullscreenState.FULLSCREEN);
        //         Thread.sleep(sleepTime);
        //         e2.viewport.window.setFullscreenState(FullscreenState.WINDOWED);
        //         Thread.sleep(sleepTime);
        //         e2.viewport.window.setFullscreenState(FullscreenState.WINDOWED_BORDERLESS);
        //     }
        //     catch(InterruptedException e) {}
        // }).start();

        new Thread(() ->
        {
            try
            {
                while(true)
                {
                    FullscreenState current = ((AWTWindow)(e2.viewport.window)).getFullscreenState();

                    FullscreenState randState;
                    switch(current)
                    {
                        case FULLSCREEN: 
                            randState = rd.nextBoolean() ? 
                                FullscreenState.WINDOWED :
                                FullscreenState.WINDOWED_BORDERLESS;
                            break;
                        case WINDOWED:
                            randState = rd.nextBoolean() ?
                                FullscreenState.FULLSCREEN :
                                FullscreenState.WINDOWED_BORDERLESS;
                            break;

                        case WINDOWED_BORDERLESS:
                            randState = rd.nextBoolean() ?
                                FullscreenState.WINDOWED :
                                FullscreenState.FULLSCREEN;
                            break;

                        default:
                            randState = FullscreenState.WINDOWED;
                    }

                    e2.viewport.window.setFullscreenState(randState);
                    Thread.sleep(2000);
                }
            }
            catch(InterruptedException e) {}
        });

        // for( int ii = 0; ii < 5; ii++ )
        // {
        //     EngineR2 engine = new EngineR2();
        //     engine.initialise(engine.setup()
        //         .withSize(200, 200)
        //         .withFullscreenState(FullscreenState.WINDOWED)
        //         .withTitle("EngineR2 Main")
        //     );
        //     instances.add(engine);
        //     vp = engine.viewport;
        // }


        
    }

    private double a = -1, b = -1;
    private double rotation = 0.0;
    public void start()
    {
        
        Tests tests = new Tests();
        tests.setup(vp);
        // tests.imageTesting();

        tests.keyTest();

        tests.scaleTest(vp.window.width(), vp.window.height());

        // tests.mouseClickingTest();

        // tests.lineTest();

        tests.consoleTest();

        tests.textTest2();

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