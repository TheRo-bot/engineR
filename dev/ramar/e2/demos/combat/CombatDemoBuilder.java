package dev.ramar.e2.demos.combat;

import dev.ramar.e2.TestDemos.Demo;
import dev.ramar.e2.EngineR2;

import java.util.List;

public class CombatDemoBuilder implements Demo
{
    public CombatDemoBuilder()
    {

    }
    private static CombatDemo currInstance;
    public void start(List<EngineR2> ers)
    {
        currInstance = new CombatDemo();
        currInstance.start(ers);
    }

    public void stop(List<EngineR2> ers)
    {
        currInstance.stop(ers);
        currInstance = null;
    }
}