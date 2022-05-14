package dev.ramar.e2.demos.combat;

import dev.ramar.e2.demos.BaseDemo;
import dev.ramar.e2.demos.DemoManager.Demo;


import dev.ramar.e2.EngineR2;

public class CombatDemo extends BaseDemo
{


    public CombatDemo()
    {

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
    }


    private void connect_player(EngineR2 er, boolean mainstance)
    {
        if( mainstance )
        {
            // establish keybinds
        }

        // viewing
    }

    private void disconnect_player(EngineR2 er, boolean mainstance)
    {
        if( mainstance )
        {
            // revoke keybinds
        }

        // viewing
    }


    protected void connectMainstance(EngineR2 ms)
    {
        if( this.player != null )
        {

        }
    }


    protected void disconnectMainstance(EngineR2 ms)
    {

    }



    protected void connectNormstance(EngineR2 ns)
    {

    }


    protected void disconnectNormstance(EngineR2 ns)
    {

    }


}