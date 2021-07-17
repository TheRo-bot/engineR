package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;
import dev.ramar.e2.rendering.drawing.StatefulDrawer;

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
    public StatefulDrawer stateful;

    /*
    Keeps images loaded by a name identifier
    */
    public ImageCache image;

    protected DrawManager(StatelessDrawer sl)
    {
        stateless = sl;
    }


    protected DrawManager(StatelessDrawer sl, StatefulDrawer sf)
    {
        stateless = sl;
        stateful = sf;
    }

    
}