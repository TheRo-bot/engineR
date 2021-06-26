package dev.ramar.e2.structures.items.weapons.guns;


import dev.ramar.e2.backend.Moment;

import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitManager;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.ViewPort;

import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.HitBox;
import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.structures.hitboxes.Group;

import dev.ramar.e2.structures.renderables.entities.Tier1_FastEntity;

import dev.ramar.utils.Timer;

import java.util.*;
import java.io.*;


public abstract class Bullet extends Tier1_FastEntity
{
    @Override
    protected void initialise()
    {
        super.initialise();
    }

    private int layer;
    private String groupName;
    public Double givenAngle = null;
    public Double givenForce = null;

    public Bullet(int layer, String name, HitBox hb, double x, double y, double xv, double yv)
    {
        super(hb, new Vec2(x, y), new Vec2(xv, yv));
        this.layer = layer;
        this.groupName = name;
        initialise();
    }


    protected Bullet(int layer, String name, HitBox hb, Vec2 pos, Vec2 vel)
    {
        super(hb, pos, vel);
        this.layer = layer;
        this.groupName = name;
    }


    public Bullet(int layer, String name, HitShape hs, double x, double y, double xv, double yv)
    {
        super(new HitBox(hs, x, y), new Vec2(x, y), new Vec2(xv, yv));
        this.layer = layer;
        this.groupName = name;
        initialise();
    }

    
    public Bullet(Bullet b)
    {
        super(b);
        this.layer = b.layer;
        this.groupName = b.groupName;
    }


    public abstract Bullet clone();


    public Bullet bulletFactory()
    {
        return clone();
    }


    public void setGroup(String name)
    {
        this.groupName = name;
    }


    public String getGroup()
    {
        return groupName;
    }


    public void onShoot()
    {
        StaticInfo.Objects.getWorld().addLayerable(this, layer);
        startUpdating();
        registerToGroup(groupName);
    }


    @Override
    public void render(ViewPort vp)
    {
        drawSelf(null, vp);
    }



    @Override
    public void drawSelf(Vec2 v, ViewPort vp)
    {  
        super.drawSelf(v, vp);

    }



    /*
    Method: doMovement
     - Calculates how much to move dependent on how much time (delta)
       has passed
     - delta is a double where 1.0 represents 1 second, vel, maxSpeed,
       drag, etc. all define how much movement in one second. This means
       that we can use delta like a percentage; doMovement(0.5) == half of
       a second of movement, means 50% of all vel/maxSpeed/etc.
    */
    @Override
    protected void doMovement(double delta)
    {
        // unlock our full potential and go as fast as physically possible!!!

        double xMove = vel.getX() < 0 ? vel.getX() : vel.getX();            

        double yMove = vel.getY() < 0 ? vel.getY() : vel.getY();

        // modify xMove / yMove by how long it's been since the last update

        double deltaXMove = xMove * delta,
               deltaYMove = yMove * delta;

        // calculate how drag will effect us
               
        deltaXMove *= drag.getX() / (1 + delta);
        deltaYMove *= drag.getY() / (1 + delta);

        // remove the distance we're travelling from vel and apply it to pos

        setXVel(getXVel() * (drag.getX() / (1 + delta)) );
        setYVel(getYVel() * (drag.getY() / (1 + delta)) );

        setXPos(getXPos() + deltaXMove);
        setYPos(getYPos() + deltaYMove);

    }

}