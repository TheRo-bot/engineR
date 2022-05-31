package dev.ramar.e2.demos.combat.actions.shooting;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Point;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.utils.PairedValues;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

/*
Action: ShootingAction
 - Makes a player move 
*/
public class ShootingAction extends Action implements Drawable
{
    public static final String NAME = "ability:player:shoot";


    private Player player;
    public ShootingAction(Player player)
    {
        this.player = player;
    }

    private Point targetPoint;
    public ShootingAction withPoint(Point point)
    {
        this.mousePoint = point;
        return this;
    }

    

    public void shoot(double x, double y, ActionManager... ams)
    {
        
    }

    public synchronized void blockedShoot(double x, double y, ActionManager... ams)
    {
        this.toBlock.block(ams);
        this.shoot(x, y, ams);
        this.toBlock.unblock(ams);
    }



    public void drawAt(double x, double y, Viewport vp)
    {
        
    }
}