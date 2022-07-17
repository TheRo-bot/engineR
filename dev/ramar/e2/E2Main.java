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


import dev.ramar.e2.rendering.awt.AWTWindow_NEW;
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
        });
	}

    EngineR2 e2;
    Random rd = new Random();


	public E2Main()
	{

        AWTWindow_NEW w1 = new AWTWindow_NEW();

        w1.withScreenSize(0.5, 0.5);
        w1.createScreen();

        AWTWindow_NEW w2 = new AWTWindow_NEW();

        w2.withScreenSize(1.0, 1.0);
        w2.createScreen();
        // WindowBuilder<AWTWindow_NEW> builder = new WindowBuilder<>(AWTWindow_NEW.class);
        // builder.res.set(1920, 1080);
        // builder.size.set(1920, 1080);
        // AWTWindow_NEW newWindow = builder.build();

        // AWTWindow_NEW newWindow = new AWTWindow_NEW();

        // e2 = new EngineR2();
        // e2.initialise(e2.setup()
        //     .withSize(1280, 720)
        //     .withFullscreenState(FullscreenState.WINDOWED)
        //     .withTitle("EngineR2 Main")
        // );


        // DemoManager dm = DemoManager.build();
        // dm.bind(e2);
        // dm.swapToDemo("combat");
    }


}