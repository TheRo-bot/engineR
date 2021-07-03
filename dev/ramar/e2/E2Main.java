package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;

import dev.ramar.e2.structures.WindowSettings;


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


	public E2Main()
	{
        vp = new AWTViewPort();
        vp.init(new WindowSettings(1280, 720, 1280, 720, false, "bruh"));
        vp.start();
	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}