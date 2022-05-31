package dev.ramar.e2.demos.combat.actions.shooting;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Point;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.utils.PairedValues;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

/*
Action: ShootingAction
 - Makes a player move 
*/
public class ShootingAction extends Action implements Drawable, Updatable
{
    public static final String NAME = "ability:player:shoot";


    private Player player;
    public ShootingAction(Player player)
    {
        super(ShootingAction.NAME);
        this.player = player;
    }

    private Point targetPoint;
    public synchronized ShootingAction withTarget(Point point)
    {
        this.targetPoint = point;
        return this;
    }

    private double waitTime = 0.0;
    private int shotCount = 0;


    private ActionManager[] toCheck = null;

    public boolean update(double delta)
    {
        boolean kill = false;

        if( !this.isBlocked(toCheck) && shotCount < this.player.gun.stats.chainShotAmount )
        {
            if( waitTime < 0.0 )
            {
                if( this.player.gun.canShoot() )
                {   
                    synchronized(this)
                    {
                        if( this.targetPoint != null )
                        {
                            this.player.gun.shootAt(targetPoint.getX(), targetPoint.getY());
                            this.waitTime = this.player.gun.stats.shootDelay;
                            shotCount++;
                        }
                    }
                }
            }
            else 
                waitTime -= delta;
        }
        else
            kill = true;

        if( kill )
            this.onStopShooting();

        return kill;
    }
    


    private void onStopShooting()
    {
        this.toBlock.unblock();
    }

    public void startShooting()
    {   
        shotCount = 0;
        waitTime = 0;
        DeltaUpdater.getInstance().toUpdate.add(this);
    }

    public void stopShooting()
    {
        DeltaUpdater.getInstance().toUpdate.remove(this);
    }

    public synchronized void blockedStartShooting(ActionManager... ams)
    {
        if( !this.isBlocked(ams) )
        {
            this.toCheck = ams;
            this.toBlock.block(ams);
            this.startShooting();
        }
    }

    public synchronized void blockedStopShooting(ActionManager... ams)
    {
        if( !this.isBlocked(ams) )
        {
            this.stopShooting();
            this.toBlock.unblock(ams);
        }

    }




    public void drawAt(double x, double y, ViewPort vp)
    {
        
    }
}