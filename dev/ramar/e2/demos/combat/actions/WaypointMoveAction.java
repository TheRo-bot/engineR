package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

import dev.ramar.e2.demos.combat.Player;

import java.util.List;
import java.util.ArrayList;

public class WaypointMoveAction extends Action
{
    private Player p;

    public WaypointMoveAction(Player p)
    {
        this.p = p;
    }

    private double tx, ty;

    private Thread actor = null;

    private double ups = 2000;

    public String getName() { return "waypoint-move"; } 

    @Override
    public boolean act(ActionManager am, Object[] o)
    {
        synchronized(this)
        {
            tx = (double)o[0];
            ty = (double)o[1];
        }

        if( actor == null )
        {
            this.blockAll(am);

            actor = new Thread(() -> 
            {
                try
                {
                    p.setXV(0);
                    p.setYV(0);
                    while(true)
                    {
                        double tarX = 0;
                        double tarY = 0;
                        synchronized(WaypointMoveAction.this)
                        {
                            tarX = tx;
                            tarY = ty;
                        }

                        double x = p.getX(),
                               y = p.getY();

                        double w = tarX - x,
                               h = tarY - y;

                        double hyp = Math.sqrt(Math.pow(w, 2) + Math.pow(h, 2));

                        double dist = Math.min(hyp, ups / 1000);

                        if( hyp > 1 )
                        {
                            double ang = Math.acos(w / hyp);
                            if( h < 0 )
                                ang *= -1;

                            w = Math.cos(ang) * dist;
                            h = Math.sin(ang) * dist;
                        }
                        else
                            break;

                        p.setX(p.getX() + w);
                        p.setY(p.getY() + h);
                        Thread.sleep(1);
                    }
                }
                catch(InterruptedException e) {}

                this.unblockAll(am);
                actor = null;
            });
            
            actor.start();
        }
        return actor != null && actor.isAlive();
    }

    public boolean act()
    {
        return false;
    }

    @Override
    public boolean blockedAct(ActionManager am, Object[] o)
    { 
        return false;
    }
}