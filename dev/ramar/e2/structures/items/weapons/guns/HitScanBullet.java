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
import dev.ramar.e2.structures.renderables.entities.FastEntity;

import dev.ramar.utils.Timer;

import java.util.*;
import java.io.*;

public class HitScanBullet extends Bullet
{


    @Override
    protected void initialise()
    {
        super.initialise();
        drag.set(1.0);
    }


    protected double maxDistance = 1000;


    public HitScanBullet(int layer, String name, HitBox hb, double x, double y, double xv, double yv)
    {
        super(layer, name, hb, new Vec2(x, y), new Vec2(xv, yv));
        initialise();
    }


    protected HitScanBullet(int layer, String name, HitBox hb, Vec2 pos, Vec2 vel)
    {
        super(layer, name, hb, pos, vel);
    }


    public HitScanBullet(int layer, String name, HitShape hs, double x, double y, double xv, double yv)
    {
        super(layer, name, new HitBox(hs, x, y), new Vec2(x, y), new Vec2(xv, yv));
        initialise();
    }


    protected HitScanBullet(HitScanBullet nb)
    {
        super(nb);

        maxDistance = nb.maxDistance;
    }


    private double accDelta = 0.0,
                   fadeWaitTime = 0.025;
    private int fadeAmount = 255;

    @Override
    public void update(double delta)
    {
        accDelta += delta;

        if( fadeAmount > 0 && accDelta > fadeWaitTime )
        {
            fadeAmount = Math.max(0, fadeAmount - 1);
            accDelta = 0.0;
            fadeWaitTime *= 0.975;

            if( fadeAmount == 0 )
                doKill = true;
        }



        hitbox.getMainColour().setAlpha(fadeAmount);
        hitbox.getSecondaryColour().setAlpha(fadeAmount);

        getMainColour().setAlpha(fadeAmount);
        getSecondaryColour().setAlpha(fadeAmount);

    }


    @Override
    public void onKill()
    {
        super.onKill();
    }


    public HitScanBullet clone()
    {
        HitScanBullet b = new HitScanBullet(this);
        b.initialise();
        return b;
    }



    @Override
    public void onShoot()
    {
        super.onShoot();

        double distance = 0.0;

        double boundingW = hitbox.getBoundingBox().getWidth(),
               boundingH = hitbox.getBoundingBox().getHeight(),
               mockDelta = 0.00125;


        double xMove = getXPos(), yMove = getYPos();
        origX = xMove;
        origY = yMove;

        boolean continue_ = true;

        while(continue_ && distance < maxDistance)
        {
            double movedThisStep = 0.0;


            setXPos(getXPos() + getXVel() * mockDelta);
            setYPos(getYPos() + getYVel() * mockDelta);


            double xDist = getXPos() - xMove,
                   yDist = getYPos() - yMove;

            // this method is only really here so you can create piercing
            // shots
            hitbox.setPos(pos);
            doCollision();
            continue_ = shootStep();

            if(! continue_ )
                break;

            movedThisStep = Math.abs(Math.sqrt(xDist * xDist + yDist * yDist));
            distance += movedThisStep; 

            xMove = getXPos();
            yMove = getYPos();

            renderPositions.add(new Vec2(xMove, yMove));

        }
    }



    /*
    Method: shootStep
     - Does actual collision and controls what to do if we do collide
     - This can be used to make shots pierce / bounce / do anything
     - The return value indicates if we should stop hit-scanning 
    */
    public boolean shootStep()
    {
        return ! isColliding();
    }


    private double origX, origY;

    private List<Vec2> renderPositions = java.util.Collections.synchronizedList(new ArrayList<>());

    @Override
    public void render(ViewPort vp)
    {
        drawSelf(null, vp);
    }


    @Override
    public void drawSelf(Vec2 p, ViewPort vp)
    {
        // super.drawSelf(p, vp);

        double x = getXPos(),
               y = getYPos();

        if( p != null )
        {
            x = p.getX();
            y = p.getY();
        }


        Colour c = vp.getColour();

        vp.setColour(mainColour);

        int size = 5;
        vp.drawRect(x - size/2, y - size/2, size, size);



        vp.setColour(secondaryColour);

        // synchronized(renderPositions)
        // {
        //     int size = 4;
        //     for( Vec2 v : renderPositions )
        //         vp.drawRect(v.getX() - (size / 2), v.getY() - (size / 2), size, size);
        //     size -= 0.1;
        // }

        synchronized(renderPositions)
        {
            vp.drawSpline(renderPositions);
        }

        if( hitbox != null )
            hitbox.drawSelf(p, vp);

        vp.setColour(c);



    }

}