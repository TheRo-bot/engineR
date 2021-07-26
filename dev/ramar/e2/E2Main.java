package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.rendering.drawing.stateful.Rect;
import dev.ramar.e2.structures.WindowSettings;

import dev.ramar.e2.rendering.drawing.stateful.*;

import java.io.*;

import java.net.*;
import java.util.*;


import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import dev.ramar.e2.rendering.awt.AWTImage;


public class E2Main
{


	public static void setup()
	{
		Main.Entrypoints.addEntrypoint((String[] args) ->
        {
            System.out.println("ah");
        	E2Main em = new E2Main();
            try
            {
                em.start();
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

    ViewPort vp;
    Random rd = new Random();

	public E2Main()
	{
        vp = new AWTViewPort();
        vp.init(new WindowSettings(1280, 720, false, "EngineR"));
        vp.start();
    }

    private double a = -1, b = -1;
    private double rotation = 0.0;
    public void start()
    {
        Tests tests = new Tests();
        tests.setup(vp);
        // tests.imageTesting();

        tests.keyTest();
        tests.scaleTest(1280, 720);

        tests.mouseClickingTest();

        Rect r = new Rect(30, 30, 30, 30);
        r.getMod()
            .withOffsetOnly(true)
            .withColour(255, 255, 255, 255)
            .withFill()
        ;

        vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        {
            vp.draw.stateless.rect.withMod()
                .withColour(255, 255, 255, 255)
                .withFill()
            ;
            vp.draw.stateless.rect.poslen(30, 30, 30, 30);
        });

        // tests.worldCenterTest();
        // tests.statefulTest();
        // tests.keyTest();

        // tests.syncPointTest();
        // tests.imageTest();

        // vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        // {
        //     vp.draw.stateless.rect.withMod()
        //         .withOffset(x, y)
        //         .withColour(255, 255, 255, 255).withFill()
        //     ;
        //     vp.draw.stateless.rect.poslen(-30, -30, 60, 60);
        // }); 

        // Rect r = new Rect(0, 0, 10, 10);
        // r.withColour(150, 150, 150, 255).withFill();
        // vp.draw.stateful.shapes.add(r);
        
        int rW = 16, rH = 9;
        int scale = 80;
        // tests.scaleTest(rW * scale, rH * scale);

        tests.textTest();



/*
        TextShape ts = new TextShape("yOu're cringe");
        ts.getMod()
            .withColour(255, 0, 0, 255)
            .withRotation(rotation)
            .withAlignment(-1, -1)
            .withSize(30)
        ;

        new Thread(() ->
        {
            try
            {
                while(true)
                {
                    a += 0.01;
                    b += 0.01;

                    if( a >= 1 ) a = -1;

                    if( b >= 1 ) b = -1;
                    rotation += 2;
                    ts.getMod()
                        .withRotation(rotation)
                        // .withSize((int)((a + 1) * 60))
                        // .withAlignment(a, b)
                    ;
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) {}

        }).start(); */

        // vp.draw.stateful.shapes.add(ts);
	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}