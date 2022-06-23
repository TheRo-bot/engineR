package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.*;

import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.drawing.stateful.*;

import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygon;

import dev.ramar.e2.demos.DemoManager;

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

import dev.ramar.e2.console.ConsoleOutput;
import dev.ramar.e2.console.ConsoleInput;

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

        PrintStream normOut = System.out;
        System.setOut(e2.console.out);

        Thread t = new Thread(() ->
        {
            try
            {
                while(true)
                {
                    normOut.println(e2.console.out);
                    Thread.sleep(1000);
                }
            }
            catch(InterruptedException e) {}
        });

        t.start();
        e2.viewport.window.onClose.add(() ->
        {
            t.interrupt();
        });
        DemoManager dm = DemoManager.build();
        dm.bind(e2);
        dm.swapToDemo("combat");
    }


}