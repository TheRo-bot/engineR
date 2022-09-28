package dev.ramar.e2;

import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.Window;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.Viewport;
import dev.ramar.e2.rendering.awt.AWTLayerManager.AWTLayer;

import dev.ramar.e2.rendering.drawing.line.Line;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.control.MouseManager;
import dev.ramar.e2.control.KeyboardManager;


import java.util.*;
/* List, ArrayList */

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
            E2Main main = new E2Main();
            main.start();
        });
	}

	public E2Main()
	{}


    class Test2 extends Line implements MouseManager.MouseListener
    {
        public Test2(double x, double y, double x2, double y2)
        {
            super(x, y, x2, y2);
        }
        public void onMove(double x, double y)
        {
            this.to.set(x, y);
        }
    }

    public void start()
    {
        AWTWindow window = new AWTWindow();
        window.setSize(0.75, 0.75);
        window.setResolution(1920, 1080);
        window.show();

        List<Test2> test2s = new ArrayList<>();
        Random rd = new Random();
        for( int ii = 0; ii < 15000; ii++ )
        {
            Line t2 = new Line();
            t2.to = window.mouse.position;
            t2.getMod()
                .colour.with(rd.nextDouble(), rd.nextDouble(), rd.nextDouble(), 255)
                .width.with(1)
                .offset.with(
                    rd.nextInt((int)window.getResolutionW()) - window.getResolutionW() * 0.5,
                    rd.nextInt((int)window.getResolutionH()) - window.getResolutionH() * 0.5
                )
            ;
            window.viewport.layers.addTo(1, t2);
        }

        Test t = new Test()
        {
            public void onMove(double x, double y)
            {
                this.pos.set(x, y);
            }
        };

        window.mouse.move.add(t);
        window.mouse.wheel.add(t);
        window.mouse.press.add(t, 1, 3);
        window.mouse.release.add(t, 1, 3);
        window.viewport.layers.add(t);

        Test t1 = new Test()
        {
            public void onMove(double x, double y)
            {
                this.pos.set(x, y);
            }

            public void onPress(String key)
            {
                System.out.println("PRESS: " + key);
            }
            public void onRelease(String key)
            {
                System.out.println("RELEASE: " + key);
            }
        };

        window.mouse.press.add(t1, 1, 3);
        window.mouse.release.add(t1, 1, 3);
        window.viewport.layers.add(t1);

        window.keys.add(t1, "ESCAPE", "CNTRL", "ENTER", "w", "a", "s", "d");

        Test t2 = new Test()
        {
            private Vec2 start = null, origin = null;
            public synchronized void onMove(double x, double y)
            {
                if( start != null )
                {
                    x = window.mouse.toRawX(x);
                    y = window.mouse.toRawY(y);
                    double xdist = (x - origin.getX()),
                           ydist = (y - origin.getY());

                    double cx = start.getX() + xdist,
                           cy = start.getY() + ydist;

                    window.viewport.setCenter(cx, cy);
                }
            }

            public synchronized void onPress(int btn, double x, double y)
            {
                if( btn == 1 )
                {
                    this.start = new Vec2(window.viewport.getCenterX(), window.viewport.getCenterY());
                    this.origin = new Vec2(window.mouse.toRawX(x), window.mouse.toRawY(y));
                }
            }

            public synchronized void onRelease(int btn, double x, double y)
            {
                if( btn == 1 )
                {
                    this.start = null;
                    this.origin = null;
                }
            }
        };


        window.mouse.press.add(t2, 1);
        window.mouse.release.add(t2, 1);
        window.mouse.move.add(t2);


        window.waitForClose();
    }

    public class Test implements MouseManager.MouseListener, Drawable, KeyboardManager.KeyListener
    {
        protected Vec2 pos = new Vec2(0);
        protected Vec2 off = new Vec2(0);
        protected Colour colour = new Colour(255, 255, 255, 255);

        public void onWheel(double x, double y, double power)
        {
            synchronized(this)
            {
                off.add(0, power * 10);
            }
        }

        public void onPress(String keyCode)
        { }
        public void onRelease(String keyCode)
        { }

        public void onPress(int btn, double x, double y)
        {   
            if( btn == 1 )
                this.colour.set(255, 0, 0, 255);
            else if( btn == 3 )
                this.colour.set(0, 255, 0, 255);
        }

        public void onRelease(int btn, double x, double y)
        {
            this.colour.set(255, 255, 255, 255);
        }

        public void drawAt(double x, double y, Viewport vp)
        {
            synchronized(this)
            {
                vp.draw.rect.withMod()
                    .colour.with(this.colour)
                    .fill.with()
                    .offset.with(x, y)
                    .offset.with(pos.getX(), pos.getY())
                    .offset.with(off.getX(), off.getY())
                ;   

                int s = 30;
                vp.draw.rect.poslen(s * -0.5, s * -0.5, s, s);

                vp.draw.rect.clearMod();
            }
        }
    }

}