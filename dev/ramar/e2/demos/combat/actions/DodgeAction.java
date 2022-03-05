package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

import dev.ramar.e2.demos.combat.Player;

public class DodgeAction extends Action
{
    private Player player;

    public DodgeAction(Player p)
    {   
        this.player = p;
    }

    public String getName()
    {
        return "dodge";
    }


    Thread t;

    private static final double DODGE_DIST = 400;
    private static final double DODGE_DURA = 0.6;
    public boolean act(ActionManager am, Object[] info)
    {
        boolean out = false;


        double xVel = this.player.getXV(),
               yVel = this.player.getYV();

        if( t != null && Math.abs(xVel) > 1.0 || Math.abs(yVel) > 1.0 )
        {
            double abs = Math.sqrt(xVel * xVel + yVel * yVel);

            final double xVelNorm = xVel / abs,
                         yVelNorm = yVel / abs;

            this.blockAll(am);

            t = new Thread(() -> 
            {
                try
                {
                    long lastTime = System.currentTimeMillis();
                    double delta = 0.0;
                    while(delta < this.DODGE_DURA)
                    {
                        long currTime = System.currentTimeMillis();
                        double d = (currTime - lastTime) / 1000.0;
                        lastTime = currTime;
                        delta += d;

                        // x distance in seconds
                        double xMove = xVelNorm * DODGE_DIST;
                        double yMove = yVelNorm * DODGE_DIST;

                        xMove *= d;
                        yMove *= d;

                        this.player.modX(xMove);
                        this.player.modY(yMove);

                        Thread.sleep(1);
                    }
                }
                catch(InterruptedException e) {}
                this.unblockAll(am);
            });

            t.start();

            out = true;            
        }

        return out || (t != null && t.isAlive());
    }


    public boolean actDEP()
    {
        boolean out = false;
        final double  xv = this.player.getXV() * 0.5,
                      yv = this.player.getYV() * 0.5,
                    time = 5.0;
        System.out.println("act (" + xv + ", " + yv + ")");
        if( Math.abs(xv) > 10 || Math.abs(yv) > 10 )
        {
            t = new Thread(() -> 
            {
                System.out.println("dodge time for " + this.player + ":)");
                try
                {
                    double delta = 0.0,
                           mod = 1.0; 
                    long lastTime = System.currentTimeMillis();
                    while(delta < time)
                    {
                        long currTime = System.currentTimeMillis();
                        double d = (currTime - lastTime) / 1000.0;
                        delta += d;

                        mod = 1 - (delta / time);
                        this.player.setX(this.player.getX() + (xv * mod * d));
                        this.player.setY(this.player.getY() + (yv * mod * d));
                        Thread.sleep(10);
                    }
                }
                catch(InterruptedException e) {}
            });
            t.start();
            out = true;
        }
        return out;
    }

    public boolean blockedAct(Object... args)
    {
        return blockedAct();
    }


    public boolean blockedAct()
    {
        if( t != null )
            t.interrupt();
        return true;
    }

    public void stop()
    {
        if( t != null )
            t.interrupt();
    }
}