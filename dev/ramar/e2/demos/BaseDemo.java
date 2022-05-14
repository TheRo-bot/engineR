package dev.ramar.e2.demos;

import dev.ramar.e2.demos.DemoManager.Demo;


import dev.ramar.e2.EngineR2;

public abstract class BaseDemo implements Demo
{


    public BaseDemo()
    {

    }


    /* Demo Implementation
    --===--------------------
    */

    private boolean initialised = false;
    public boolean isInitialised()
    {   return this.initialised;   }

    private EngineR2 mainstance;


    public void initialise()
    {
        this.initialised = true;

        System.out.println("initailised!");
    }


    public void connect(EngineR2 er)
    {
        if( this.mainstance == null )
        {
            this.mainstance = er;
            this.connectMainstance(er);
        }
        else
            this.connectNormstance(er);
    }


    public void disconnect(EngineR2 er)
    {
        if( er == this.mainstance )
        {
            this.mainstance = null;
            this.disconnectMainstance(er);
        }
        else
            this.disconnectNormstance(er);
    }

    protected abstract void connectMainstance(EngineR2 ms);
    protected abstract void disconnectMainstance(EngineR2 ms);

    protected abstract void connectNormstance(EngineR2 ns);
    protected abstract void disconnectNormstance(EngineR2 ns);

}