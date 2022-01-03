package dev.ramar.e2.demos.combat;

import dev.ramar.e2.TestDemos.Demo;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyListener;

import java.util.List;
import java.util.ArrayList;

public class CombatDemo implements Demo
{
    private boolean initialised = false;

    private List<Drawable> drawables = new ArrayList<>();

    private EngineR2 er;
    
    private void initialise(EngineR2 er)
    {
        this.er = er;
        if( !initialised )
        {
            initialised = true;
            er.console.out.println("first time startup of combat demo");

            /* player */
            Player p = new Player();
            p.initialise(er);

            drawables.add(p);
        }

    }

    private class Player implements Drawable
    {
        double x = 0, y = 0, xm = 0, ym = 0;
        long time = -1;
        long moveTime = -1;
        boolean moving = false;
        public Player()
        {

        }

        public void initialise(EngineR2 er)
        {
            KeyListener moveListener = new KeyListener()
            {
                private double speed = 400.0;
                public void onPress(KeyCombo kc)
                {
                    moving = true;
                    switch(kc.getName())
                    {
                        case "up":
                            ym -= speed;
                            break;
                        case "down":
                            ym += speed;
                            break;
                        case "left":
                            xm -= speed;
                            break;
                        case "right":
                            xm += speed;
                            break;
                    }
                    moveTime = System.currentTimeMillis();
                }

                public void onRelease(KeyCombo kc)
                {
                    moving = false;
                    moveTime = -1;
                }
    
            };

            er.viewport.window.keys.bindPress(new KeyCombo("up")
                                          .withChar('w'), 
                                     moveListener);
            er.viewport.window.keys.bindRel(new KeyCombo("up")
                                          .withChar('w'),
                                     moveListener);

            er.viewport.window.keys.bindPress(new KeyCombo("down")
                                          .withChar('s'), 
                                     moveListener);
            er.viewport.window.keys.bindRel(new KeyCombo("down")
                                          .withChar('s'),
                                     moveListener);


            er.viewport.window.keys.bindPress(new KeyCombo("left")
                                          .withChar('a'), 
                                     moveListener);
            er.viewport.window.keys.bindRel(new KeyCombo("left")
                                          .withChar('a'), 
                                     moveListener);


            er.viewport.window.keys.bindPress(new KeyCombo("right")
                                          .withChar('d'), 
                                     moveListener);

            er.viewport.window.keys.bindRel(new KeyCombo("right")
                                          .withChar('d'), 
                                     moveListener);
        }

        public void drawAt(double xOff, double yOff, ViewPort vp)
        {
            long now = System.currentTimeMillis();
            if( time != -1 )
            {
                // start up
                long deltaMS = now - time;

                vp.draw.stateless.text.withMods(2)
                    .withSize(10)
                    .withColour(255, 255, 255, 255)
                    .withOffset(xOff, yOff)
                ;
                vp.draw.stateless.text.pos_c(0, 0, "" + Math.round(xm * 100) / 100.0 + ", " + Math.round(ym * 100) / 100.0);
                // vp.draw.stateless.text.pos_c(0, 0, "" + xm + ", " + ym);
                vp.draw.stateless.text.pos_c(0, 30, moving ? "moving" : "rolling");


                // draw
                vp.draw.stateless.rect.withMod()
                    .withColour(255, 255, 255, 255)
                    .withFill()
                    .withOffset(xOff, yOff)
                    .withOffset(x, y)
                ;

                vp.draw.stateless.rect.poslen(-5, -5, 10, 10);

                // close the thing
            }
            time = System.currentTimeMillis();


        }   
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
    }


}