package dev.ramar.e2.demos.combat.actions.shooting;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Point;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.demos.combat.guns.semiauto.SemiAuto;

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


    private SemiAuto gun;
    public ShootingAction(SemiAuto g)
    {
        super(ShootingAction.NAME);
        if( g == null )
            new Exception().printStackTrace();
        this.gun = g;
    }

    private Point targetPoint;
    public synchronized ShootingAction withTarget(Point point)
    {
        this.targetPoint = point;
        return this;
    }

    private double waitTime = 0.0;
    private int shotCount = 0;

    private long endTime = -1;

    private ActionManager[] toCheck = null;

    public boolean update(double delta)
    {
        boolean kill = false;
        if( !this.isBlocked(toCheck) && shotCount < this.gun.stats.chainShotAmount )
        {
            // if we've lagged enough to actually shoot (time blocked doesn't count)
            if( waitTime < 0.0 )
            {
                // if we've lagged enough for the chainshot's end lag
                if( endTime == -1 || endTime + (this.gun.stats.chainShotEndLag * 1000) <= DeltaUpdater.getInstance().nowTime() )
                {
                    if( this.gun.canShoot() )
                    {   
                        synchronized(this)
                        {
                            if( this.targetPoint != null )
                            {
                                this.gun.shootAt(targetPoint.getX(), targetPoint.getY());
                                this.waitTime = this.gun.stats.shootDelay;
                                shotCount++;
                            }
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
        DeltaUpdater.getInstance().toUpdate.queueAdd(this);
    }

    public void stopShooting()
    {
        DeltaUpdater.getInstance().toUpdate.queueRemove(this);
        this.endTime = DeltaUpdater.getInstance().nowTime();
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