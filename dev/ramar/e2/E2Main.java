package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;
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