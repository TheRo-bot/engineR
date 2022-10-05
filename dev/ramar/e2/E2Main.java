package dev.ramar.e2;

import dev.ramar.e2.awt.rendering.AWTWindow;
import dev.ramar.e2.core.rendering.Window;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;
import dev.ramar.e2.awt.rendering.AWTLayerManager.AWTLayer;

import dev.ramar.e2.core.drawing.line.Line;

import dev.ramar.e2.core.structures.Vec2;
import dev.ramar.e2.core.structures.Colour;

import dev.ramar.e2.core.control.MouseManager;
import dev.ramar.e2.core.control.KeyboardManager;


import dev.ramar.e2.core.objects.*;
import dev.ramar.e2.awt.objects.*;

import java.util.*;
/* List, ArrayList */

import javax.swing.JFrame;

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
        Container container = new Container();
        AWTWindow window = new AWTWindow();
        window.setSize(0.75, 0.75);
        window.setResolution(1920, 1080);
        window.show();
        window.viewport.layers.add(container);


        container.children.add(new RObject()
        {
            public void drawAt(double x, double y, Viewport vp)
            {
                vp.draw.rect.withMod()
                    .offset.with(x, y)
                    .colour.with(125, 125, 125, 255)
                    .fill.with()
                ;

                vp.draw.rect.poslen(-10, -10, 20, 20);

                vp.draw.rect.clearMod();
            }
        });

        for( int ii = 0; ii < 1; ii++ )
        {

            BufferedText text = new BufferedText();

            // new Thread(() ->
            // {
            // //     try
            // //     {

            // //         double dtg = 0.01;
            // //         long lastTime = System.currentTimeMillis();
            // //         while(true)
            // //         {
            // //             long thisTime = System.currentTimeMillis();
            // //             double delta = (thisTime - lastTime) / 1000.0;
            // //             dtg -= delta;
            // //             if( dtg <= 0 )
            // //             {
            // //                 dtg = 0.01;
            // //                 size += 0.1;
            // //             }

            // //             Thread.sleep(1);
            // //         }
            // //     }
            // //     catch (InterruptedException e) {}
            // // }).start();

            double size = 150;

            double width = 12 * 5;

            text.textMods
                .font.with("Calibri")
                .size.with((int)Math.round(size))
            ;
            text.imageMods
                .scale.with(width / size)
            ;

            text.updateBuffer();

            if( text.getText() == null )
                text.setText("ayo whatup");


            container.children.add(text);

            // container.children.add((double x, double y, Viewport vp) ->
            // {
            //     vp.draw.text.withMod()
            //         .font.with("Calibri")
            //         .size.with(75)
            //         .offset.with(x, y)
            //     ;

            //     vp.draw.text.at(0, 0, "a123;[]'\\`,./pdIi");

            //     vp.draw.text.clearMod();
            // });
        }




        Test t = new Test()
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

            public void onWheel(double x, double y, double power)
            {
                double force = 0.25;

                double w = window.getResolutionW() * (1 + (power * force));
                double h = window.getResolutionH() * (1 + (power * force));

                // window.viewport.setCenter(window.mouse.toRawX(x), window.mouse.toRawY(y));
                window.setResolution(w, h);
            }
        };

        window.mouse.add(t, 1, 3);

        window.waitForClose();
    }


    public void poggers()
    {
        AWTWindow window = new AWTWindow();
        window.setSize(0.75, 0.75);
        window.setResolution(1920, 1080);
        window.show();

        Test t = new Test()
        {
            public void onMove(double x, double y)
            {
                synchronized(this)
                {
                    this.pos.set(x, y);
                }
            }
            public void onWheel(double x, double y, double power)
            {
                double force = 0.25;

                double w = window.getResolutionW() * (1 + (power * force));
                double h = window.getResolutionH() * (1 + (power * force));

                window.setResolution(w, h);
            }
        };

        window.mouse.add(t, 1, 3);

        Test t1 = new Test()
        {
            public void onMove(double x, double y)
            {
                synchronized(this)
                {
                    this.pos.set(x, y);
                }
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

    public class Test extends RObject implements MouseManager.MouseListener, KeyboardManager.KeyListener
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