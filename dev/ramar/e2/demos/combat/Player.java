package dev.ramar.e2.demos.combat;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.structures.Vec2;

public class Player implements Drawable, Anchor, Updatable
{
    public Player()
    {

    }

    private Vec2 pos = new Vec2(), 
                 vel = new Vec2();



    /* Control Methods
    --====--------------- 
    */

    public void bindControl(EngineR2 er)
    {

    }   

    public void unbindControl(EngineR2 er)
    {

    }
    
    /* Hit Methods
    --====-----------
    */



    /* Updating Methods
    --====----------------
    */

    public void update(double delta)
    {

    }

    /* Drawing Methods
    --====---------------
    */



    public void drawAt(double x, double y, ViewPort vp)
    {

    }
}