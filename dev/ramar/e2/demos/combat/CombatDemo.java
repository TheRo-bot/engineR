package dev.ramar.e2.demos.combat;

import dev.ramar.e2.demos.BaseDemo;
import dev.ramar.e2.demos.DemoManager.Demo;


import dev.ramar.e2.EngineR2;

public class CombatDemo extends BaseDemo
{


    public CombatDemo()
    {
        if( DeltaUpdater.getInstance() != null )
            DeltaUpdater.getInstance().start();
    }


    Player player = null;

    @Override
    public void initialise()
    {
        super.initialise();   
        this.init_player();
    }


    /* Player Methods
    --===---------------
    */

    private void init_player()
    {
        this.player = new Player();
        this.player.initialise();
    }


    private void connect_player(EngineR2 er, boolean mainstance)
    {
        if( mainstance )
        {
            // establish keybinds
            this.player.bindControl(er);
        }

        // viewing
        er.viewport.layers.mid.add(this.player);
    }

    private void disconnect_player(EngineR2 er, boolean mainstance)
    {
        if( mainstance )
        {
            // revoke keybinds
            this.player.unbindControl(er);
        }

        // viewing
        er.viewport.layers.mid.remove(this.player);
    }


    protected void connectMainstance(EngineR2 ms)
    {
        if( this.player != null )
            this.connect_player(ms, true);
    }


    protected void disconnectMainstance(EngineR2 ms)
    {
        if( this.player != null )
            this.disconnect_player(ms, true);
    }



    protected void connectNormstance(EngineR2 ns)
    {
        if( this.player != null )
            this.connect_player(ns, false);
    }


    protected void disconnectNormstance(EngineR2 ns)
    {
        if( this.player != null )
            this.disconnect_player(ns, false);
    }


}