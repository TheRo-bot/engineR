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

	public E2Main()
	{
        vp = new AWTViewPort();
        vp.init(new WindowSettings(1280, 720, false, "bruh"));
        vp.start();
        List<Drawable> temps = new ArrayList<>();

        int tempCount = 100;
        int[][] positions = new int[tempCount][2];
        for( int ii = 0; ii < positions.length; ii++ )
        {
            positions[ii][0] = vp.window.width() / 2;
            positions[ii][1] = vp.window.height() / 2;
        }
        vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        {
            vp.draw.stateless.rect.withMod().
                                   withOffset(
                                        vp.window.width() / 2 - 10,
                                        vp.window.height() / 2 - 10).
                                   withColour(100, 100, 150, 255).
                                   withFill();

            vp.draw.stateless.rect.poslen(x, y, 20, 20);
        });

        for( int ii = 0; ii < tempCount; ii++ )
        {
            final int thisIndex = ii;

                Drawable d = new Drawable()
                {
                    private int off = thisIndex;
                    private Random rand = new Random();

                    public void drawAt(double x, double y, ViewPort vp)
                    {
                        RectMods rm = vp.draw.stateless.rect.withMod();
                        rm.withColour(150, 150, 150, 255).withFill();

                        positions[off][0] += rand.nextBoolean() ? 1 : -1;
                        positions[off][1] += rand.nextBoolean() ? 1 : -1;

                        rm.withOffset(positions[off][0], positions[off][1]);

                        vp.draw.stateless.rect.poslen(x, y, 20, 20);
                    }
                };

                temps.add(d);
                vp.draw.stateless.perm.add(d);

            // }
            // catch(InterruptedException e) {}
        }
        vp.draw.stateless.perm.add(new Drawable()
        {
            private double offX = vp.window.width() / 2, 
                           offY = vp.window.height() / 2;

            public void drawAt(double x, double y, ViewPort vp)
            {
                RectMods rm = vp.draw.stateless.rect.withMod();
                rm.withColour(255, 255, 255, 255).withFill();

                double xAv = 0.0, yAv = 0.0;

                for( int ii = 0; ii < positions.length; ii++ )
                {
                    xAv += positions[ii][0];
                    yAv += positions[ii][1];
                }

                xAv /= positions.length;
                yAv /= positions.length;

                rm.withOffset(xAv, yAv);

                vp.draw.stateless.rect.poslen(x, y, 20, 20);
            }
        });


	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}