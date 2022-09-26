package dev.ramar.e2;

import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.Viewport;

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

        window.mouse.move.add((double x, double y) ->
        {
            System.out.println("bruh(" + x + ", " + y + ")");
        });

        window.waitForClose();
    }

    private class MouseTest implements Drawable, MouseManager.MoveListener
    {
        
        public void onMove(double x, double y)
        {
            synchronized(this)
            {
                
            }
        }

        public void drawAt(double x, double y, Viewport vp)
        {
            synchronized(this)
            {
                vp.draw.polyline.withMod()
                    .colour.with(255, 0, 0, 255)
                    .offset.with(x, y)
                ;

                vp.draw.polyline.offsets()
            }
        }
    }
}