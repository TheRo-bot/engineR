package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;

public abstract class DrawManager
{
    public StatelessDrawer stateless;
    // public StatefulDrawer stateful;


    protected DrawManager(StatelessDrawer sd)
    {
        stateless = sd;
    }

    
}