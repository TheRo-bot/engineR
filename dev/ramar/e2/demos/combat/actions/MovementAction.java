package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;
import dev.ramar.e2.demos.combat.actions.ActionManager.Action;


public class MovementAction extends Action
{
    Player player = new Player();

    private double xFactor = 0.0,
                   yFactor = 0.0;

    // units per second
    private static final double MOVE_SPEED = 200.0;

    public MovementAction(Player p)
    {
        this.player = p;
        t = new Thread(() -> {
            try
            {
                long lastTime = System.currentTimeMillis();
                while(true)
                {
                    long currTime = System.currentTimeMillis();
                    double delta = currTime = lastTime;
                    lastTime = currTime;

                    double xF = 0.0,
                           yF = 0.0;
                    synchronized(MovementAction.this)
                    {
                        xF = xFactor * MovementAction.this.MOVE_SPEED;
                        yF = yFactor * MovementAction.this.MOVE_SPEED;
                    }

                    double abs = Math.sqrt(xF * xF + yF * yF);

                    double xDist = xF / abs,
                           yDist = yF / abs; 

                    this.player.setXV(xF);
                    this.player.setYV(yF);

                    Thread.sleep(10);
                }
            }
            catch(InterruptedException e) {}
        });

        t.start();
    }

    public String getName()
    {   return "movement";   }

    Thread t;


    public boolean act(ActionManager am, Object[] o)
    {
        boolean out = false;

        if( o != null && o.length > 1 )
        {
            String name = (String)o[0];
            boolean state = (boolean)o[1];
            boolean didSomething = true;
            synchronized(this)
            {
                switch(name)
                {
                    case "up":
                        yFactor += state ? -MOVE_SPEED : MOVE_SPEED;
                        break;
                    case "down":
                        yFactor += state ? MOVE_SPEED : -MOVE_SPEED;
                        break;
                    case "left":
                        xFactor += state ? -MOVE_SPEED : MOVE_SPEED; 
                        break;
                    case "right":
                        xFactor += state ? MOVE_SPEED : -MOVE_SPEED;
                        break;
                    default:
                        didSomething = false;
                }
            }

            out = didSomething;
        }

        return out;
    }


    public boolean blockedAct(ActionManager am, Object[] o)
    {
        boolean out = false;
        if( o != null && o.length > 0 )
        {
            String name = (String)o[0];

            boolean didSomething = true;
            synchronized(this)
            {
                switch(name)
                {
                    case "up":
                        yFactor += 1.0;
                        break;
                    case "down":
                        yFactor += -1.0;
                        break;
                    case "left":
                        xFactor += 1.0; 
                        break;
                    case "right":
                        xFactor += -1.0;
                        break;
                    default:
                        didSomething = false;
                }
            }
            out = didSomething;
        }

        return out;
    }

}