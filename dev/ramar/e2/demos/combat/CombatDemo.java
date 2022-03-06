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

    private List<EngineR2> ers = new ArrayList<>();
    

    private Player player = new Player();
    // private Player test = new Player()
    // {

    //     @Override
    //     public void setup(List<EngineR2> ers)
    //     {
    //         super.setup(ers);
    //         up.clearChars();
    //         up.withChar('i');
    //         down.clearChars();
    //         down.withChar('k');
    //         right.clearChars();
    //         right.withChar('l');
    //         left.clearChars();
    //         left.withChar('j');

    //         moveToPoint.clearChars();
    //         moveToPoint.withChar('v');

    //         this.g = 0;
    //         this.b = 0;
    //     }
    // };

    private void initialise(List<EngineR2> ers)
    {
        this.ers = ers;
        if( !initialised )
        {
            initialised = true;
            for( EngineR2 er : ers )
                er.console.out.println("first time startup of combat demo");

            player.setup(ers);
            // test.setup(ers);

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
            // drawables.add(test);


        }

    }


    private void uninitialise(List<EngineR2> ers)
    {
        player.setdown(ers);
        // test.setdown(ers);

        drawables.clear();
    }

    public void start(List<EngineR2> ers)
    {
        initialise(ers);
        for( EngineR2 er : ers )
            for( Drawable d : drawables )
                er.viewport.draw.stateless.perm.add(d);

    }


    public void stop(List<EngineR2> ers)
    {
        for( EngineR2 er : ers )
            for( Drawable d : drawables )
                er.viewport.draw.stateless.perm.remove(d);

        uninitialise(ers);
    }


}