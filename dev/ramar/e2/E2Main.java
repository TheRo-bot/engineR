package dev.ramar.e2;

import dev.ramar.e2.rendering.awt.AWTWindow;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.Viewport;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.control.MouseManager;

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

    public void start()
    {
        AWTWindow window = new AWTWindow();
        window.setSize(0.75, 0.75);
        window.setResolution(1920, 1080);
        window.show();

        window.viewport.layers.add((double x, double y, Viewport vp) ->
        {
            vp.draw.rect.withMod()
                .colour.with(125, 125, 125, 255)
                .fill.with()
                .offset.with(x, y)
            ;
            double w = 30;
            vp.draw.rect.poslen(w * -0.5, w * -0.5, w, w);

            vp.draw.rect.clearMod();
        });


        // Test t = new Test();
        // window.mouse.wheel.add(t);
        // window.viewport.layers.add(t);

        Test t2 = new Test()
        {
            private boolean pressing = false;
            public void onMove(double x, double y)
            {
                System.out.println("move! " + x + ", " + y);    
                if( pressing )
                    window.viewport.setCenter(x, y);
                else
                    this.pos.set(x, y);
            }
            public void onPress(int btn, double x, double y)
            {
                if( btn == 1 )
                    this.pressing = true;
            }
            public void onRelease(int btn, double x, double y)
            {
                if( btn == 1 )
                    this.pressing = false;
            }
        };

        window.mouse.move.add(t2);
        window.mouse.press.add(t2, 1);
        window.mouse.release.add(t2, 1);
        window.mouse.wheel.add(t2);

        window.viewport.layers.add(t2);

        window.waitForClose();
    }

    public class Test implements MouseManager.MouseListener, Drawable
    {
        protected Vec2 pos = new Vec2(0);
        protected Vec2 off = new Vec2(0);

        public void onWheel(double x, double y, double power)
        {
            synchronized(this)
            {
                pos.set(x, y);
                off.add(0, power * 10);
            }
        }

        public void drawAt(double x, double y, Viewport vp)
        {
            synchronized(this)
            {
                vp.draw.rect.withMod()
                    .colour.with(255, 255, 255, 255)
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