package dev.ramar.e2;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;


import dev.ramar.e2.structures.WindowSettings;


import java.awt.Color;
import java.awt.Graphics2D;


import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
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

        viewport.draw.stateless.perm.add(console);

        viewport.window.keys.bindRel(new KeyCombo("console_toggle")
                                             .withChar('`'),
        (KeyCombo kc) ->
        {
            System.out.println("AHHHHH");
            console.withVisibility(!console.getVisibility());
        });
    }

    /* Window Initialisation
    -===-----------------------
    */

    public static class WindowInitialiser
    {
        

        public int screenW, screenH;
        public String title = "EngineR2";
        public FullscreenState fs = FullscreenState.WINDOWED;

        public WindowInitialiser() {}

        public WindowInitialiser withSize(int w, int h)
        {
            screenW = w;
            screenH = h;
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
            vp.init(screenW, screenH, title, fs);
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