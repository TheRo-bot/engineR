package dev.ramar.e2.demos.combat;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.EngineR2;

import dev.ramar.e2.structures.Vec2;


import dev.ramar.e2.rendering.control.KeyListener;
import dev.ramar.e2.rendering.control.KeyCombo;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

public class Player implements Drawable
{
    // position and velocity
    protected double x = 0, y = 0, xv = 0, yv = 0;

    protected int r = 255, g = 255, b = 255;

    protected EngineR2 er;

    public Player()
    {
       
    }

    public Player(double x, double y)
    {
        this.x = x;
        this.y = y;
    } 

    public void setPos(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // up down left right
    private boolean[] directions = new boolean[]{false, false, false, false};

    protected final KeyCombo    up = new KeyCombo("up").withChar('w');

    protected final KeyCombo  down = new KeyCombo("down").withChar('s');

    protected final KeyCombo  left = new KeyCombo("left").withChar('a');

    protected final KeyCombo right = new KeyCombo("right").withChar('d');


    protected final KeyListener moveListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {
            processAction(kc.getName(), true);
        }

        public void onRelease(KeyCombo kc)
        {
            processAction(kc.getName(), false);
        }

        public void processAction(String name, boolean pressed)
        {
            switch(name)
            {
                case "up":
                    directions[0] = pressed;
                    break;

                case "down":
                    directions[1] = pressed;
                    break;

                case "left":
                    directions[2] = pressed;
                    break;

                case "right":
                    directions[3] = pressed;
                    break;
            }
        }
    };

    public void setdown(EngineR2 er)
    {
        er.viewport.window.keys.unbindPress(up, moveListener);
        er.viewport.window.keys.  unbindRel(up, moveListener);


        er.viewport.window.keys.unbindPress(down, moveListener);
        er.viewport.window.keys.  unbindRel(down, moveListener);



        er.viewport.window.keys.unbindPress(left, moveListener);
        er.viewport.window.keys.  unbindRel(left, moveListener);


        er.viewport.window.keys.unbindPress(right, moveListener);
        er.viewport.window.keys.  unbindRel(right, moveListener);

        t.interrupt();
    }

    Thread t = new Thread(() ->
    {
        try
        {
            long lastTime = System.currentTimeMillis();
            while(true)
            {
                Thread.sleep(1);
                long nowTime = System.currentTimeMillis();
                double delta = (nowTime - lastTime) / 1000.0; 
                ((Player)this).update(delta);
                lastTime = nowTime;
            }
        }   
        catch(InterruptedException e)
        {}
    });

    public void setup(EngineR2 er)
    {
        this.er = er;
        er.viewport.window.onClose.add(() ->
        {
            t.interrupt();
        });
        
        t.start();

        

        er.viewport.window.keys.bindPress(up, moveListener);
        er.viewport.window.keys.  bindRel(up, moveListener);


        er.viewport.window.keys.bindPress(down, moveListener);
        er.viewport.window.keys.  bindRel(down, moveListener);



        er.viewport.window.keys.bindPress(left, moveListener);
        er.viewport.window.keys.  bindRel(left, moveListener);


        er.viewport.window.keys.bindPress(right, moveListener);
        er.viewport.window.keys.  bindRel(right, moveListener);

    }

    private boolean doCameraTrack = false;

    public void trackToCamera()
    {
        doCameraTrack = true;
    }

    public void stopTracking()
    {
        doCameraTrack = false;
    }

    private void processDirection(double delta)
    {
        // how fast per ms
        int speed = 1250;
        if( directions[0] )
            yv -= speed * delta;
        if( directions[1] )
            yv += speed * delta;
        if( directions[2] )
            xv -= speed * delta;
        if( directions[3] )
            xv += speed * delta;
    }


    private long lastTime = -1;
    private double maxSpeed = 150.0;
    private double acceleration = 4;


    public void update(double delta)
    {
        processDirection(delta);

        if( Math.abs(xv) > 0.0001 || Math.abs(yv) > 0.0001 )
        {
            // once we calculate the hypotenuse we need to
            // ensure it's length is constrained

            // right now we have the 'unrestricted triangle',
            // defined by <xv> and <yv>, it isn't within the radius
            // that maxSpeed defines.

            // <xv> and <yv> define the triangle. calculate the
            // angle and hypotenuse

        
        
            double hyp = Math.sqrt(Math.pow(xv, 2) + Math.pow(yv, 2));

            double ang = Math.acos(xv / hyp);

            if( yv < 0 )
                ang *= -1;

            double dist = Math.min(maxSpeed, hyp);

            // we need to use the unrestricted triangle (UT) to
            // calculate theta and hypotenuse length so we can
            // create the restricted triangle (RT) and extract
            // its adjacent and opposite lengths for a bounded
            // step.

            double xm = Math.cos(ang) * dist,
                   ym = Math.sin(ang) * dist;

            xv = xv > 0 ? Math.min(xm, xv) :
                          Math.max(xm, xv) ;

            yv = yv > 0 ? Math.min(ym, yv) :
                          Math.max(ym, yv) ;  


            xm = xm * delta * acceleration;
            ym = ym * delta * acceleration;

            xv -= xm;
            yv -= ym;

            x += xm;
            y += ym;


        }
        
        if( doCameraTrack && er != null )
        {
            er.viewport.setCenterX(-x);
            er.viewport.setCenterY(-y);
        }
    }

    private double round(double a)
    {
        return ((int)a * 100) / 100.0;
    }

    private double lastX = 0, lastY = 0;

    public void drawAt(double xo, double yo, ViewPort vp)
    {
        // render

        int sqSize = 60;
        vp.draw.stateless.rect.withMod()
            .withColour(r, g, b, 255)
            .withFill()
            .withOffset(xo, yo)
            .withOffset(x, y)
        ;

        vp.draw.stateless.rect.poslen(-sqSize/2, -sqSize/2, sqSize, sqSize);

        vp.draw.stateless.line.withMods(4)
            .withColour(0, 255, 255, 255)
            .withOffset(xo + x, yo + y)
        ;

        vp.draw.stateless.line.pospos(0, 0,  xv,yv);
        vp.draw.stateless.line.pospos(0, 0,  xv, 0);
        vp.draw.stateless.line.pospos(xv, 0, xv, yv);

        vp.draw.stateless.text.withMod()
            .withOffset(xo + x, yo + y)
            .withSize(10)
        ;

        double hyp = Math.sqrt(Math.pow(xv, 2) + Math.pow(yv, 2));
        double ang = Math.acos(xv / hyp);

        if( yv < 0 )
            ang *= -1;

        double roundedAng = ((int)(Math.toDegrees(ang) * 100)) / 100.0;
        vp.draw.stateless.text.pos_c(0, 10, "" + roundedAng);


    }
}   