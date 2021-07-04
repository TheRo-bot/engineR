package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.structures.WindowSettings;


import java.util.*;


public class E2Main
{

	public static void setup()
	{
		Main.Entrypoints.addEntrypoint((String[] args) ->
        {
        	E2Main em = new E2Main();
            em.waitForClose();
        	System.out.println("E2 close!");
        });
	}

    ViewPort vp;
    Random rd = new Random();
    double offX = 0, offY = 0;

	public E2Main()
	{
        vp = new AWTViewPort();
        vp.init(new WindowSettings(1280, 720, false, "bruh"));
        vp.start();


        vp.draw.stateless.addDrawable((double x, double y, DrawManager dm) ->
        {
            // System.out.println("ayo: " + dm.stateless.rect);
            RectMods rm = dm.stateless.rect.withMod();
            rm.withColour(150, 150, 150, 255).withFill();

            rm.withOffset(offX, offY);
            offX = rd.nextInt(vp.window.width());
            offY = rd.nextInt(vp.window.height());

            dm.stateless.rect.poslen(x, y, rd.nextInt(20) + 30, rd.nextInt(20) + 30);
        });
	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}