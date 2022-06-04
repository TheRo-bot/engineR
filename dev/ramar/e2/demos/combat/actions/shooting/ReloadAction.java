package dev.ramar.e2.demos.combat.actions.shooting;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.demos.combat.guns.Gun;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import java.util.Arrays;

public class ReloadAction extends Action implements Updatable
{
    public static final String NAME = "ability:player:reload";

    private Gun gun;

    public ReloadAction(Gun g)
    {
        super(ReloadAction.NAME);
        this.gun = g;
    }


    private Player player;

    private double waitTime = 0.0;

    public boolean update(double delta)
    {
        waitTime -= delta;
        
        boolean out = false;
        if( waitTime < 0.0 )
        {
            out = true;
            this.onReloadTimer();
        }

        return out; 
    }

    private boolean reloading = false;
    public boolean isReloading()
    {   return this.reloading;   }

    private ActionManager[] toCheck = null;

    public void onReloadTimer()
    {
        this.reloading = false;
        this.gun.reload();
        this.toBlock.unblock(toCheck);
        System.out.println("load!");
    }


    public void reload()
    {   
        this.reloading = true;
        System.out.println("re...");
        this.waitTime = this.gun.stats.reloadTime;    
        DeltaUpdater.getInstance().toUpdate.queueAdd(this);
    }


    public void blockedReload(ActionManager... ams)
    {
        if( !this.isBlocked(ams) )
        {
            this.toCheck = ams;
            this.toBlock.block(ams);
            this.reload();
        }
    }
}