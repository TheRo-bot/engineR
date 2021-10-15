package dev.ramar.e2;

import dev.ramar.e2.rendering.*;
import java.util.*;
import java.io.*;

import dev.ramar.e2.rendering.console.Console;
import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.drawing.stateful.*;
import dev.ramar.e2.rendering.drawing.stateless.*;
import dev.ramar.e2.rendering.drawing.stateless.LineDrawer.LineMods;

import dev.ramar.e2.rendering.control.MouseController.PressedListeners.PressedListener;
import dev.ramar.e2.rendering.control.MouseController.ReleasedListeners.ReleasedListener;

import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
import dev.ramar.e2.rendering.control.KeyController.KeyListener;


import dev.ramar.e2.structures.SyncPoint;

import dev.ramar.e2.rendering.ui.TextField;

public class Tests
{
    private ViewPort vp;
    private Random rd = new Random();

    public Tests()
    {

    }

    public void setup(ViewPort vp)
    {
        this.vp = vp;
    }

    private double a = -1, b = -1;

    public void imageTesting()
    {
        try
        {
            vp.draw.image.loadToCache(getClass(), "/resources/test.png", "test");

            Image i = vp.draw.image.get("test");

            if( i != null )
            {
                new Thread(() ->
                {
                    try
                    {
                        double aAmount = (double)(rd.nextInt(90) + 10) / 1000.0;
                        double bAmount = (double)(rd.nextInt(90) + 10) / 1000.0;
                        while(true)
                        {
                            a += aAmount;
                            b += bAmount;

                            if( a >= 1.0 || a <= -1.0)
                            {
                                aAmount = -aAmount;
                                a = Math.max(-1.0, Math.min(1.0, a));
                                double newAm = (double)(rd.nextInt(90) + 10) / 1000.0;
                                if( aAmount < 0 )
                                    newAm = -newAm;
                                aAmount = newAm;
                            } 
                            if( b >= 1.0 || b <= -1.0)
                            {
                                bAmount = -bAmount;
                                b = Math.max(-1.0, Math.min(1.0, b));
                                double newAm = (double)(rd.nextInt(90) + 10) / 1000.0;
                                if( bAmount < 0 )
                                    newAm = -newAm;
                                bAmount = newAm;
                            } 
                            Thread.sleep(10);
                        }
                    }
                    catch(InterruptedException e) {}
                }).start();

                vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
                {

                    vp.draw.stateless.image.withMod().withAlignment(a, b)
                                                     .withOffset(x, y)
                                                     // .withRotation(c)
                                                     // .withScale(a, b)
                                                     ;
                    vp.draw.stateless.image.pos_c(0, 0, i);

                    vp.draw.stateless.rect.withMod()
                                                    .withOffset(x, y)
                                                    .withColour(255, 255, 255, 255)
                                                    .withFill();
                    vp.draw.stateless.rect.poslen(-2, -2, 4, 4);
                });
            }
        }
        catch(IOException e) 
        {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private int am = 0;

    private class ClickTest implements PressedListener, ReleasedListener
    {
        public ClickTest()
        {

        }

        private ViewPort vp;

        public void setup(ViewPort vp)
        {
            this.vp = vp;
        }

        private Rect r = null;

        public void mousePressed(int button, double x, double y)
        {
            System.out.println("pressed");
            r = new Rect(10, 10);
            r.getMod()
                .withOffset(x - 5, y - 5)
                .withColour(255, 255, 255, 255)
                .withFill()
            ;

            vp.draw.stateful.shapes.add(r);
        }

        public void mouseReleased(int button, double x, double y)
        {
            vp.draw.stateful.shapes.remove(r);
        }

    }

    public void mouseClickingTest()
    {


        ClickTest ct = new ClickTest();
        ct.setup(vp);
        vp.window.mouse.onRelease.add(ct);
        vp.window.mouse.onPress.add(ct);
    }


    Thread worldCenterThread;
    public void worldCenterTest()
    {
        worldCenterThread = new Thread(() ->
        {
            try
            {
                while(true)
                {
                    vp.moveCenterX(0.1);
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) {}
        });
        worldCenterThread.start();

    }

    public void stopWorldCenterTest()
    {
        worldCenterThread.interrupt();
    }



    public void statefulTest()
    {
        Rect r = new Rect(10, 10, 10, 10);
        r.getMod()
            .withColour(255, 255, 255, 255).withFill()
            .withOffset(30, 30);

        vp.draw.stateful.shapes.add(r);
    }


    public void keyTest()
    {
        int moveSpeed = 3;
        Runnable moveUp = () ->
        {
            Rect r = new Rect(-5, -5, 10, 10);
            r.getMod()
                .withColour(255, 255, 255, 255)
                .withFill()
                .withOffset(0, -10)
            ;

            try
            {

                vp.draw.stateful.shapes.add(r);
                while(true)
                {
                    vp.moveCenterY(-moveSpeed);
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) 
            {
                vp.draw.stateful.shapes.remove(r);
            }
        };

        Runnable moveDown = () ->
        {
            Rect r = new Rect(-5, -5, 10, 10);
            r.getMod()
                .withColour(0, 255, 255, 255)
                .withFill()
                .withOffset(0, 10)
            ;
                
            
            try
            {
                vp.draw.stateful.shapes.add(r);
                while(true)
                {
                    vp.moveCenterY(moveSpeed);
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e)
            {
                vp.draw.stateful.shapes.remove(r);
            }
        };
        Runnable moveRight = () ->
        {
            Rect r = new Rect(-5, -5, 10, 10);
            r.getMod()
                .withColour(255, 0, 255, 255)
                .withFill()
                .withOffset(10, 0)
            ;

            try
            {
                vp.draw.stateful.shapes.add(r);
                while(true)
                {
                    vp.moveCenterX(moveSpeed);
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e)
            {
                vp.draw.stateful.shapes.remove(r);
            }
        };

        Runnable moveLeft = () ->
        {
            Rect r = new Rect(-5, -5, 10, 10);
            r.getMod()
                .withColour(255, 255, 0, 255)
                .withFill()
                .withOffset(-10, 0)
            ;

            try
            {
                vp.draw.stateful.shapes.add(r);

                while(true)
                {
                    vp.moveCenterX(-moveSpeed);
                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) 
            {
                vp.draw.stateful.shapes.remove(r);
            }
        };

        KeyListener walkListener = new KeyListener()
        {
            List<Thread> t = new ArrayList<>();
            Thread u, d, l, r;

            public void onPress(KeyCombo kb)
            {
                Runnable thisR = null;
                switch(kb.getName())
                {
                    case "walk_up":
                        u = new Thread(moveUp);
                        u.start();
                        break;
                    case "walk_down":
                        d = new Thread(moveDown);
                        d.start();
                        break;
                    case "walk_right":
                        r = new Thread(moveRight);
                        r.start();
                        break;
                    case "walk_left":
                        l = new Thread(moveLeft);
                        l.start();
                        break;
                }

            }

            public void onRelease(KeyCombo kb)
            {
                switch(kb.getName())
                {
                    case "walk_up":
                        u.interrupt();
                        break;
                    case "walk_down":
                        d.interrupt();
                        break;
                    case "walk_right":
                        r.interrupt();
                        break;
                    case "walk_left":
                        l.interrupt();
                        break;
                }
            }
        };

        vp.window.keys.bind(new KeyCombo("walk_up").withChar('w'), walkListener);

        vp.window.keys.bind(new KeyCombo("walk_down").withChar('s'), walkListener);

        vp.window.keys.bind(new KeyCombo("walk_left").withChar('a'), walkListener);

        vp.window.keys.bind(new KeyCombo("walk_right").withChar('d'), walkListener);


        Rect r = new Rect(-4, -4, 8, 8);
        r.getMod()
            .withColour(150, 150, 150, 255)
            .withFill()
        ;
        vp.draw.stateful.shapes.add(r);
    }


    private SyncPoint currSyncPoint;
    private boolean syncBool = false;

    public void syncPointTest()
    {
        SyncPoint sp1 = new SyncPoint()
        {
            private List<Shape> shapes = new ArrayList<>();

            public void setup()
            {
                Rect r = new Rect(-10, -10, 20, 20);
                r.getMod()
                    .withColour(125, 255, 100, 255).withFill();
                shapes.add(r);


            }


            public void load()
            {
                for( Shape s : shapes )
                {
                    vp.draw.stateful.shapes.add(s);
                }
            }


            public void unloadTo(SyncPoint sp)
            {
                for( Shape s : shapes )
                {
                    vp.draw.stateful.shapes.remove(s);
                }
                // here, we can go "if( sp instanceof SpecificAreaSyncPoint)"
                // unload *most* things, but allow partial back-tracking

            }


            public String getID()
            {
                return "syncPointTest1";
            }

        };

        SyncPoint sp2 = new SyncPoint()
        {
            private List<Shape> shapes = new ArrayList<>();

            public void setup()
            {

                Rect r = new Rect(-10, -10, 20, 20);
                r.getMod()
                    .withColour(255, 255, 125, 255)
                    .withFill()
                ;
                shapes.add(r);
                r = new Rect(-20, -20, 10, 10);
                r.getMod()
                    .withColour(125, 125, 255, 255)
                    .withFill()
                ;
                shapes.add(r);

            }


            public void load()
            {
                for( Shape s : shapes )
                {
                    vp.draw.stateful.shapes.add(s);
                }
            }


            public void unloadTo(SyncPoint sp)
            {
                for( Shape s : shapes )
                {
                    vp.draw.stateful.shapes.remove(s);
                }
                // here, we can go "if( sp instanceof SpecificAreaSyncPoint)"
                // unload *most* things, but allow partial back-tracking

            }


            public String getID()
            {
                return "syncPointTest2";
            }

        };

        // this'd be handled in GUI initialisation
        sp1.setup();
        sp2.setup();

        sp1.addNeighbour(sp2);


        vp.window.keys.bind(new KeyCombo("toggle_sp").withChar('v'), new KeyListener()
        {

            public void onPress(KeyCombo kb) {}



            public void onRelease(KeyCombo kb)
            {
                syncBool = ! syncBool;

                if( syncBool )
                {
                    sp1.unloadTo(sp2);
                    sp2.load();
                }
                else
                {
                    sp2.unloadTo(sp1);
                    sp1.load();
                }
                System.out.println("swapping syncpoints!!");

            }


        });
    }


    public void scaleTest(int fakeW, int fakeH)
    {
        final int moveAm = 10;
        final int waitTime = 10;
        Runnable width_up = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalWidth(vp.getLogicalWidth() + moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };

        Runnable width_down = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalWidth(vp.getLogicalWidth() - moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };

        Runnable height_up = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalHeight(vp.getLogicalHeight() + moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}

        };

        Runnable height_down = () ->
        {
            try
            {
                while(true)
                {
                    vp.setLogicalHeight(vp.getLogicalHeight() - moveAm);
                    Thread.sleep(waitTime);
                }
            }
            catch(InterruptedException e) {}
        };



        KeyListener kl = new KeyListener()
        {
            private Thread wUp, wDown, hUp, hDown;

            public void onPress(KeyCombo kc)
            {
                switch(kc.getName())
                {
                    case "width_up":
                        wUp = new Thread(width_up);
                        wUp.start();
                        break;
                    case "width_down":
                        wDown = new Thread(width_down);
                        wDown.start();
                        break;
                    case "height_up":
                        hUp = new Thread(height_up);
                        hUp.start();
                        break;
                    case "height_down":
                        hDown = new Thread(height_down);
                        hDown.start();
                        break;


                }
            }

            public void onRelease(KeyCombo kc)
            {
                switch(kc.getName())
                {
                    case "width_up":
                        wUp.interrupt();
                        break;
                    case "width_down":
                        wDown.interrupt();
                        break;
                    case "height_up":
                        hUp.interrupt();
                        break;
                    case "height_down":
                        hDown.interrupt();
                        break;
                }
                System.out.println("logical size: (" + vp.getLogicalWidth() + ", " + vp.getLogicalHeight() + ")");
            }
        };

        vp.window.keys.bind(new KeyCombo("height_up").withChar('i'), kl);
        vp.window.keys.bind(new KeyCombo("height_down").withChar('k'), kl);
        vp.window.keys.bind(new KeyCombo("width_up").withChar('l'), kl);
        vp.window.keys.bind(new KeyCombo("width_down").withChar('j'), kl);


        // vp.setLogicalWidth(fakeW);
        // vp.setLogicalHeight(fakeH);
        System.out.println("scaleTest end");
    }

    private double scale = 1.0, rotate = 0.0, rotateAm = 1.0;

    public void imageTest()
    {
        try
        {
            vp.draw.image.loadToCache(getClass(), "/resources/test.png", "test");
            Image i = vp.draw.image.get("test");
            ImageShape is = new ImageShape(i);

            is.getMod().withAlignment(0, 0);

            new Thread(() ->
            {
                try
                {
                    while(true)
                    {
                        is.getMod()
                            .withScale(scale, scale)
                            // .withRotation(rotate)
                        ;
                        scale -= 0.01;
                        Thread.sleep(10);
                    }
                }
                catch(InterruptedException e) {}
            }).start();

            vp.draw.stateful.shapes.add(is);
        }
        catch(IOException e) {}

    }


    private double textRot  = 0.0,
                   textSize = 20;

    private double renderTime = 0;
    public void textTest()
    {

        new Thread(() ->
        {
            try
            {
                while(true)
                {
                    textRot += 0.05;
                    // textSize += 1;

                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) {}
        }).start();

        vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        {
            long start = System.nanoTime();
            vp.draw.stateless.text.withMod()
                .withOffset(x, y)
                .withSize((int)textSize)
                .withAlignment(0, 0)
                .withRotation(textRot)
                .withColour(0, 255, 0, 255)
                // .withStyle(TextMods.FontStyles.PLAIN)
            ;
            vp.draw.stateless.text.pos_c(200, 200, "bruh");
            long end = System.nanoTime();

            renderTime = (end - start) * 0.000001;
        });
  

    }


    private int capStyle = 0, joinStyle = 0;
    public void lineTest()
    {
        Line l = new Line()
            .withStartPos(40, 40)
            .withEndPos(0, 0)
        ;
        l.getMod()
            .withColour(255, 0, 255, 255)
            .withThickness(1)
            .withCapStyle(LineMods.CapStyle.ROUND)
        ;

        new Thread(() ->
        {
            try
            {
                double a = 1;
                while(true)
                {
                    l.getMod().withThickness(Math.max(5, Math.min(l.getMod().getThickness() + rd.nextDouble() % a - a / 2, 8) ));
                    Thread.sleep(1);
                }

            }
            catch(InterruptedException e) {}
        }).start();
/*BUTT
ROUND
SQUARE

BEVEL
MITER
ROUND
*/


        vp.window.keys.bind(new KeyCombo("bruh").withChar('o'), new KeyListener()
        {

            public void onPress(KeyCombo kc)
            {

            }
            int style = 0;
            public void onRelease(KeyCombo kc)
            {
                style++;

                LineMods.CapStyle cs = LineMods.CapStyle.BUTT;
                switch(style)
                {
                    case 1:
                        cs = LineMods.CapStyle.BUTT;
                        break;
                    case 2:
                        cs = LineMods.CapStyle.ROUND;
                        break;

                    case 3:
                        cs = LineMods.CapStyle.SQUARE;
                        break;

                    default:
                        style = 0;
                        break;
                }

                l.getMod().withCapStyle(cs);
            }
        });

        new Thread(() ->
        {
            try
            {
                while(true)
                {
                    l.withEndPos(vp.window.mouse.getMouseX(),
                                 vp.window.mouse.getMouseY());

                    Thread.sleep(1);
                }
            }
            catch(InterruptedException e) {}
        }).start();

        vp.draw.stateful.shapes.add(l);
        // vp.draw.stateless.perm.add((double x, double y, ViewPort vp) -> 
        // {
        //     vp.draw.stateless.line.withMod()
        //         .withColour(255, 255, 255, 255)
        //         .withOffset(x, y)
        //     ;


        //     double mouseX = vp.window.mouse.getMouseX();
        //     double mouseY = vp.window.mouse.getMouseY();
        //     vp.draw.stateless.line.pospos(0, 0, mouseX, mouseY);
        // });

        vp.draw.stateless.perm.add(new Drawable()
        {  
            private double[] xs = new double[100], ys = new double[100];
            public void drawAt(double x, double y, ViewPort vp)
            {
                vp.draw.stateless.line.withMod()
                    .withOffset(x, y)
                    .withColour(255, 255, 0, 255)
                ;
                vp.draw.stateless.line.pos_linked(xs, ys);
            }

            private void update()
            {
                Random rd = new Random();
                for( int ii = 0; ii < xs.length; ii++ )
                    xs[ii] = rd.nextInt(vp.getLogicalWidth()) + rd.nextDouble();

                for( int ii = 0; ii < ys.length; ii++ )
                    ys[ii] = rd.nextInt(vp.getLogicalHeight()) + rd.nextDouble();

            }

        });
    }


    public void consoleTest()
    {
        Console c = new Console()
            .withPos(100, 40)
        ;

        vp.draw.stateless.perm.add(c);

        vp.window.keys.bindRel(new KeyCombo("bruh").withChar('`'), (KeyCombo kc) ->
        {
            System.out.println("AHHH");
            c.withVisibility(!c.getVisibility());
        });

        vp.window.keys.bind(new KeyCombo("bruh").withChar('`'), new KeyListener()
        {
            public void onPress(KeyCombo kc)
            {

            }

            public void onRelease(KeyCombo kc)
            {
                c.animation_SwapVisibility();
            }
        });
    }


    public void textTest2()
    {

        TextField tf;
        tf = new TextField(vp.getLogicalWidth() / 2, vp.getLogicalHeight() / 2)
            .withHint("AHHHH")
            .withInput("bruh moments")
            .withPos(80, 100)
        ;

        vp.draw.stateful.shapes.add(tf);

/*        TextShape ts = new TextShape("i am very confused");
        ts.getMod()
            .withColour(255, 255, 255, 255)
            .withOffset(40, 40)
            .withSize(50)
            .withAlignment(1, 0)
        ;

        vp.draw.stateful.shapes.add(ts);

        vp.draw.stateless.perm.add((double x, double y, ViewPort vp) ->
        {
            int size = 5;

            for( int ii = 0; ii < ts.getText().length(); ii++ )
            {
                double thisX = ts.getXAtChar(ii);
                // System.out.println("bruh(" + ii + "): " + thisX);
                vp.draw.stateless.rect.withMod()
                    .withColour(255, 255, 255, 255)
                    .withOffset(x, y)
                    .withFill()
                ;
                vp.draw.stateless.rect.poslen(ts.getMod().getOffX() + thisX - size/2, ts.getMod().getOffY() - size/2 - 50, size, size);
            }
        });*/

    }

/*    private static class XPBall implements Drawable
    {
        private static List<XPBall> balls = java.util.Collections.synchronizedList(new ArrayList<>());


        private double x, y;

        private int r = 255, g = 255, b = 255, a = 255;

        private double deltaPassed = 0.0;
        private long lastTime = 0;

        private int weight = 4;

        public XPBall(double x, double y)
        {
            this.x = x;
            this.y = y;
            balls.add(this);
        }

        public XPBall withColour(int r, int g, int b, int a)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }

        private void deltaUpdate()
        {

            if( deltaPassed > 0.001 && weight > 0 )
            {
                deltaPassed = 0;
                double dragRad = 20,
                       consumeRad = 10,
                       gravityStrength = 1.0;
                for( int ii = 0; ii < balls.size(); ii++ )
                {
                    XPBall ball = balls.get(ii);

                    if( ball != this )
                    {
                        double xComp = this.x - ball.x,
                               yComp = this.y - ball.y;

                        double moveX = xComp / 0.001,
                               moveY = yComp / 0.001;

                        if( xComp * xComp + yComp * yComp < (dragRad * (weight * 0.8)) * (dragRad * (weight * 0.8)) )
                        {
                            double maxDist = 0.004 * gravityStrength * (weight * 1.125);

                            // currently, vector {xComp, yComp} is the distance
                            // to get to ball2 from ball1

                            // make sure the distance the vector is travelling
                            // is under max

                            double hyp = Math.sqrt(moveX * moveX + moveY * moveY);
                            while(hyp > maxDist)
                            {
                                moveX *= 0.1;
                                moveY *= 0.1;
                                hyp = Math.sqrt(moveX * moveX + moveY * moveY);
                            }

                            ball.x += moveX;
                            ball.y += moveY;
                        }

                        if( xComp * xComp + yComp * yComp < (consumeRad * ((double)weight / 20.0)) * (consumeRad * ((double)weight / 100.0)))
                        {
                            weight += ball.weight;
                            ball.weight = 0;

                            int size = balls.size();
                            balls.remove(ball);
                            System.out.println(size + " => " + balls.size());
                            ball.withColour(ball.r, ball.g, ball.b, 0);
                        }
                    }

                 


                }


            }
        }


        public void drawAt(double vpX, double vpY, ViewPort vp)
        {
            long currTime = System.currentTimeMillis();
            if( lastTime != 0 )
                deltaPassed += (double)(currTime - lastTime) / 1000.0;

            deltaUpdate();
            lastTime = currTime;


            vp.draw.stateless.rect.withMod()
                .withColour(r, g, b, a)
                .withOffset(vpX - weight / 2, vpY - weight / 2)
                .withFill()
            ;

            vp.draw.stateless.rect.poslen(this.x, this.y, weight, weight);

            vp.draw.stateless.text.withMod()
                .withColour(255, 255, 255, 255)
                .withSize(10)
                .withOffset(vpX, vpY)
            ;

            vp.draw.stateless.text.pos_c(this.x, this.y + 10, "" + weight);
        }
    }

    public void gravityTest()
    {
        int squareNums = 200;

        double fieldWidth  = vp.window.width()  * 1.5,
               fieldHeight = vp.window.height() * 1.5;

        double halfWidth  = fieldWidth  / 2.0;
        double halfHeight = fieldHeight / 2.0;
        for( int ii = 0; ii < squareNums; ii++ )
        {

            XPBall thisBall = new XPBall(rd.nextInt((int)fieldWidth ) - halfWidth,
                                         rd.nextInt((int)fieldHeight) - halfHeight);
            thisBall.withColour(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255), 255);

            vp.draw.stateless.perm.add(thisBall);
        }
    }*/
}

