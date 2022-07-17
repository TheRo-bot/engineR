package dev.ramar.e2;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;
import dev.ramar.e2.rendering.awt.control.AWTKeyController;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;

import dev.ramar.e2.structures.WindowSettings;
import dev.ramar.e2.structures.Vec2;


import java.awt.Color;
import java.awt.Graphics2D;


import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.console.Console;

public class EngineR2
{

    public final AWTViewPort viewport;
    public final Console console;


    public EngineR2()
    {
        viewport = new AWTViewPort();
        console = new Console()
            .withPos(100, 40)
        ;

        viewport.layers.mid.add(console);

        ((AWTKeyController)viewport.window.keys).doEngineStuff(this);            


        viewport.window.keys.bindRel(new KeyCombo("console_toggle")
                                         .withChar('`'),
        (KeyCombo kc) ->
        {
            console.animation_SwapVisibility();
        });
    }



    /* Window Initialisation
    -===-----------------------
    */

    public static class WindowInitialiser
    {
        public Vec2 screenRes = new Vec2(1920, 1080);
        public Vec2 screenSize = new Vec2(1920, 1080);
        public double ppmm = 96;

        public String title = "EngineR2";
        public FullscreenState fs = FullscreenState.WINDOWED;

        public WindowInitialiser() {}

        public WindowInitialiser withRes(double w, double h)
        {
            this.screenRes.set(w, h);
            return this;
        }

        public WindowInitialiser withSize(int w, int h)
        {
            screenSize.set(w, h);
            return this;
        }

        public WindowInitialiser withTitle(String s)
        {
            title = s;
            return this;
        }

        public WindowInitialiser withFullscreenState(FullscreenState fs)
        {
            this.fs = fs;
            return this;
        }


        public void build(ViewPort vp)
        {
            vp.init((int)this.screenSize.getX(), (int)this.screenSize.getY(), title, fs);
        }
    } 

    public WindowInitialiser setup()
    {
        return new WindowInitialiser();
    }


    public void initialise(WindowInitialiser wi)
    {
        wi.build(viewport);
        viewport.start();
    }


    public void stop()
    {
        viewport.stop();
    }

}