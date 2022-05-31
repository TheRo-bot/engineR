package dev.ramar.e2.demos.combat.actions.shooting;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;


import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import java.util.Arrays;

public class ReloadAction extends Action implements Updatable
{
    public static final String NAME = "ability:player:reload";


    public ReloadAction(Player player)
    {
        super(ReloadAction.NAME);
        this.player = player;
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

    private ActionManager[] toCheck = null;

    public void onReloadTimer()
    {
        this.player.gun.reload();
        this.toBlock.unblock(toCheck);
        System.out.println("load!");
    }


    public void reload()
    {   
        System.out.println("re...");
        this.waitTime = this.player.gun.stats.reloadTime;    
        DeltaUpdater.getInstance().toUpdate.add(this);
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