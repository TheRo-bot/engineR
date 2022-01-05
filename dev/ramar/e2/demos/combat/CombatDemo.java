package dev.ramar.e2.demos.combat;

import dev.ramar.e2.TestDemos.Demo;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyListener;

import dev.ramar.e2.rendering.drawing.stateless.LineDrawer.LineMods;

import java.util.List;
import java.util.ArrayList;

public class CombatDemo
{
    private boolean initialised = false;

    private List<Drawable> drawables = new ArrayList<>();

    private EngineR2 er;
    

    private Player player = new Player();
    private Player test = new Player()
    {

        @Override
        public void setup(EngineR2 er)
        {
            super.setup(er);

            up.clearChars();
            up.withChar('i');
            down.clearChars();
            down.withChar('k');
            right.clearChars();
            right.withChar('l');
            left.clearChars();
            left.withChar('j');

            this.g = 0;
            this.b = 0;
        }
    };

    private void initialise(EngineR2 er)
    {
        this.er = er;
        if( !initialised )
        {
            initialised = true;
            er.console.out.println("first time startup of combat demo");

            /* player */
            player.setup(er);
            player.startCameraTracking();
            test.setup(er);

            Drawable grid = new Drawable()
            {
                double cx = 0, cy = 0;


                int cellXDist = 500,
                    cellYDist = 500;
                int gridW = 10000;
                int gridH = 10000;

                int r = 125, g = 125, b = 125;
                public void drawAt(double x, double y, ViewPort vp)
                {
                    LineMods lm = new LineMods()
                        .withColour(r, g, b, 255)
                        .withOffset(x, y)
                    ;
                    vp.draw.stateless.line.withTempMod(lm);

                    int startX = (int)(cx - gridW/2);

                    while(startX <= cx + gridW/2)
                    {
                        vp.draw.stateless.line.pospos(startX, -gridH/2, startX, gridH/2);
                        startX += cellXDist;
                    }

                    int startY = (int)(cy - gridH/2);
                    while(startY <= cy + gridH/2)
                    {
                        vp.draw.stateless.line.pospos(-gridW/2, startY, gridW/2, startY);
                        startY += cellYDist;
                    }


                    vp.draw.stateless.line.clearTempMod();

                }
            };

            drawables.add(grid);

            drawables.add(player);
            drawables.add(test);


        }

    }


    private void uninitialise(EngineR2 er)
    {
        player.setdown(er);
        test.setdown(er);

        drawables.clear();
    }

    public void start(EngineR2 er)
    {
        initialise(er);

        for( Drawable d : drawables )
        {
            er.viewport.draw.stateless.perm.add(d);
        }
    }


    public void stop(EngineR2 er)
    {
        for( Drawable d : drawables )
        {
            er.viewport.draw.stateless.perm.remove(d);
        }

        uninitialise(er);
    }


}