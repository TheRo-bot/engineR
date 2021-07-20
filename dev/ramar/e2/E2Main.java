package dev.ramar.e2;



import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;
import dev.ramar.e2.structures.WindowSettings;

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

    public Image getImageAbs(String path) throws IOException
    {
        return new AWTImage(ImageIO.read(new File(path)));
    }


    public Image getImage(Class c, String resPath) throws IOException
    {
        Image exp = null;
        System.out.println(c + ", " + resPath);
        System.out.println("AHH: " + c.getResource(""));

        URL url = c.getResource("");
        System.out.println("URL: " + url);
        if( url != null )
        {
            String path = url.getFile();
            if( resPath.charAt(0) == '/' )
                resPath = resPath.substring(1);

            path += resPath;
            System.out.println("path: " + path);
            BufferedImage bi = ImageIO.read(new File(path));
            exp = new AWTImage(bi);            
        }

        return exp;
    }

    private int a = -1, b = -1;
    private boolean c = false;
    private double d = 0.0, e = 0.0, f = 0.0;
    public void start()
    {
        // new BufferedReader(new InputStreamReader());
        // Image im = vp.draw.image.load(new File(""));
        // vp.draw.image.cache(im, "test1");

        try
        {
            // Image i = getImage(getClass(), "/resources/test.png");
            Image i = getImageAbs("D:/Users/Robot/D - File Gateway/Local/Documents/Private/Coding/RAMAR/Engine R2/code/dev/ramar/e2/resources/test.png");
            System.out.println("image: " + i);
            new Thread(() ->
            {
                try
                {
                    while(true)
                    {

                        c = !c;
                        if( c )
                        {
                            a++;
                            if( a == 2 ) a = -1;

                        }
                        else
                        {
                            b++;
                            if( b == 2 ) b = -1;
                        }


                        Thread.sleep(500);
                    }
                }
                catch(InterruptedException e) {}
            }).start();

            new Thread(() ->
            {
                try
                {
                    while(true)
                    {
                        d += 0.1;
                        e += 0.001;
                        if( e > 1.9)
                            e = 0;

                        f += 0.001;
                        if( f > 1.9)
                            f = 0;
                        Thread.sleep(1);
                    }
                }
                catch(InterruptedException e) {}
            }).start();

            vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
            {

                vp.draw.stateless.image.withMod().withAlignment(a, b)
                                                 .withOffset(100, 100)
                                                 .withScale(e, f)
                                                 .withRotation(d);
                vp.draw.stateless.image.pos_c(x, y, i);   

                vp.draw.stateless.rect.withMod().withOffset(100, 100)
                                                .withOffset(x, y)
                                                .withColour(255, 255, 255, 255)
                                                .withFill();
                vp.draw.stateless.rect.poslen(-2, -2, 4, 4);
            });
        }
        catch(IOException e) 
        {
            System.out.println("IOException: " + e.getMessage());
        }

        vp.guis.requestGUI(new GUI()
        {
            @Override
            public boolean requestAccess(GUI g)
            {
                return true;

            }

            @Override
            public void prepSwapTo(GUI g)
            {

            }


            public void initiateGUI(ViewPort vp)
            {
                super.initiateGUI(vp);
                viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                {
                    thisVP.draw.stateless.rect.withMod().withColour(255, 255, 0, 255).withFill();
                    thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                });
            }

        });

        GUI[] guis = new GUI[]
        {   
            new GUI()
            {
                @Override
                public boolean requestAccess(GUI g)
                {
                    return true;
                }

                @Override
                public void prepSwapTo(GUI g)
                {

                }


                public void initiateGUI(ViewPort vp)
                {
                    super.initiateGUI(vp);
                    viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                    {
                        thisVP.draw.stateless.rect.withMod().withColour(255, 255, 0, 255).withFill();
                        thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                    });
                }
            }, 
            new GUI()
            {
                @Override
                public boolean requestAccess(GUI g)
                {
                    return true;
                }

                @Override
                public void prepSwapTo(GUI g)
                {

                }


                public void initiateGUI(ViewPort vp)
                {
                    super.initiateGUI(vp);
                    viewport.draw.stateless.perm.add((double x, double y, ViewPort thisVP) ->
                    {
                        thisVP.draw.stateless.rect.withMod().withColour(255, 0, 255, 255).withFill();
                        thisVP.draw.stateless.rect.poslen(30, 30, 20, 20);
                    });
                }
            }
        };

        while(true)
        {
            for( int ii = 0; ii < guis.length; ii++ )
            {
                vp.guis.requestGUI(guis[ii]);
                try
                {
                    Thread.sleep(1500);
                }
                catch(InterruptedException e)
                {}
            }
        }

	}


    public void waitForClose()
    {
        vp.waitForClose();
    }
}