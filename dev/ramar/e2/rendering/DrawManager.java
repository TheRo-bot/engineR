package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager
{
    public StatelessDrawer stateless;
    // public StatefulDrawer stateful;


    protected DrawManager(StatelessDrawer sd)
    {
        stateless = sd;
    }

    
}