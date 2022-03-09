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

        e2 = new EngineR2();
        e2.initialise(e2.setup()
            .withSize(1280, 720)
            .withFullscreenState(FullscreenState.WINDOWED)
            .withTitle("EngineR2 Main")
        );  
        instances.add(e2);


        // this is a test !
        TestDemos td = new TestDemos(instances);
        e2.console.parser.parseCommand("demo combat");
        e2.console.parser.parseCommand("stats show memory");
    }

    private boolean allDone = false;
    public void waitForClose()
    {
        try
        {
            e2.viewport.window.onClose.add(() ->
            {
                try
                {
                    dev.ramar.e2.demos.combat.DeltaUpdater.getInstance().close();
                }
                catch(InterruptedException e) 
                {
                    System.out.println("Couldn't wait for deltaupdater to close!!");
                }

                allDone = true;
            });

            while(! allDone )
                Thread.sleep(100);
        }
        catch(InterruptedException e) {}
        System.out.println("!!! closing");
    }
}