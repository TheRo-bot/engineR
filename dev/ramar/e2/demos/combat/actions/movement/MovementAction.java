package dev.ramar.e2.demos.combat.actions.movement;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.PairedValues;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

/*
Action: MovementAction
 - Makes a player move 
*/
public class MovementAction extends Action
{

    public static final String NAME = "ability:player:movement";

    Player player = null;

    private Vec2 vec = null;
    private boolean[] directions = new boolean[4];

    private static final double SPEED = 1.2;
    private static final double ACCEL = 3;

    public double speed = MovementAction.SPEED;
    public double accel = MovementAction.ACCEL;

    public MovementAction(Player p)
    {
        super(MovementAction.NAME);

        this.player = p;
        this.vec = this.player.vecs.create((double val) ->
        {
            return val * this.accel;
        });

        DeltaUpdater.getInstance().toUpdate.queueAdd((double delta) -> 
        {
            boolean stop = false;
            double x = 0.0,
                   y = 0.0;
            synchronized(this)
            {
                if( MovementAction.this.ams == null || !this.isBlocked(MovementAction.this.ams) )
                {
                    if( directions[0] )
                        y -= 1.0;
                    if( directions[1] )
                        y += 1.0;
                    if( directions[2] )
                        x -= 1.0;
                    if( directions[3] )
                        x += 1.0;
                    double abs = Math.sqrt(x * x + y * y);
                    if( abs > 0 )
                        this.vec.add(x / abs * this.speed, y / abs * this.speed);

                    // TODO: limit speed?
                    // not needed, we have good movement code B)
                }
            }

            return stop;
        }); 
    }

    /* Actually Moving Methods
    --===------------------------
    */

    public synchronized void up(boolean doing)
    {  this.directions[0] = doing;  }

    public synchronized void down(boolean doing)
    {  this.directions[1] = doing;  }

    public synchronized void left(boolean doing)
    {  this.directions[2] = doing;  }

    public synchronized void right(boolean doing)
    {  this.directions[3] = doing;  }


    public boolean doingNothing()
    {
        boolean doingNothing = true;
        for( boolean b : this.directions )
        {
            if( b )
            {
                doingNothing = false;
                break;
            }
        }
        return doingNothing;
    }


    public int doingIdentity()
    {
        int out = 0;

        if( directions[0] )
            out += 3;
        if( directions[1] )
            out =+ 7;
        if( directions[2] )
            out += 11;
        if( directions[3] )
            out += 13;

        return out;
    }

    private ActionManager[] ams;

    public synchronized void blockedUp(boolean doing, ActionManager... ams)
    {
        // if we're not doing anything and we know we're about to do something
        if( doing && this.doingNothing() )
        {
            this.toBlock.block(ams);
            this.ams = ams;
        }

        int identity = this.doingIdentity();
        this.up(doing);

        // if we're doing something and we know we're about to not do something
        if( !doing && identity == 3 )
            this.toBlock.unblock(ams);

    }


    public synchronized void blockedDown(boolean doing, ActionManager... ams)
    {
        // if we're not doing anything and we know we're about to do something
        if( doing && this.doingNothing() )
        {
            this.toBlock.block(ams);
            this.ams = ams;
        }

        int identity = this.doingIdentity();
        this.down(doing);
        
        // if we're doing something and we know we're about to not do something
        if( !doing && identity == 7 )
            this.toBlock.unblock(ams);
    }


    public synchronized void blockedLeft(boolean doing, ActionManager... ams)
    {
        // if we're not doing anything and we know we're about to do something
        if( doing && this.doingNothing() )
        {
            this.toBlock.block(ams);
            this.ams = ams;
        }

        int identity = this.doingIdentity();
        this.left(doing);
        
        // if we were doing something and we know we're about to not do something
        if( !doing && identity == 11 )
            this.toBlock.unblock(ams);
    }


    public synchronized void blockedRight(boolean doing, ActionManager... ams)
    {
        // if we're not doing anything and we know we're about to do something
        if( doing && this.doingNothing() )
        {
            this.toBlock.block(ams);
            this.ams = ams; 
        }

        int identity = this.doingIdentity();
        this.right(doing);
        
        // if we were doing something and we know we're about to not do something
        if( !doing && identity == 13 )
            this.toBlock.unblock(ams);
    }


}