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


import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;

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

        tests.scaleTest(vp.window.width(), vp.window.height());

        // tests.mouseClickingTest();

        // tests.lineTest();

        tests.consoleTest();

        tests.textTest2();

	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}