package dev.ramar.e2.demos.combat;

import dev.ramar.e2.TestDemos.Demo;
import dev.ramar.e2.EngineR2;

public class CombatDemoBuilder implements Demo
{
    public CombatDemoBuilder()
    {

    }
    private CombatDemo currInstance;
    public void start(EngineR2 er)
    {
        currInstance = new CombatDemo();
        currInstance.start(er);
    }

    public void stop(EngineR2 er)
    {
        currInstance.stop(er);
        currInstance = null;
    }
}