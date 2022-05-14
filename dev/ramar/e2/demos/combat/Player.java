package dev.ramar.e2.demos.combat;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.rect.RectMods;

import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.actions.Action;
import dev.ramar.e2.demos.combat.actions.ActionManager;

import dev.ramar.e2.rendering.control.KeyListener;
import dev.ramar.e2.rendering.control.KeyCombo;

import dev.ramar.e2.demos.combat.actions.*;
/* Action, ActionManager */

import dev.ramar.e2.demos.combat.actions.movement.*;
/* MovementAction, MovementArgs */

import dev.ramar.utils.PairedValues;

import java.util.List;
import java.util.ArrayList;


public class Player implements Drawable, Anchor, Updatable
{
    public Player()
    {
        this.rmods
            .colour.with(255, 255, 255, 255)
        ;
    }

    private Vec2 pos = new Vec2(), 
                 vel = new Vec2();


    public void initialise()
    {
        this.actions.add(new MovementAction(this.actions, this));
        DeltaUpdater.getInstance().toUpdate.add(this);
    }

    /* Anchor Implementation
    --====---------------------
    */

    public double getX()
    {   return this.pos.getX();  }

    public double getY()
    {   return this.pos.getY();  }

    /* Control Methods
    --====--------------- 
    */

    public void bindControl(EngineR2 er)
    {
        er.viewport.window.keys.bindPress(Keybinds.Player.up,    this.moveListener);
        er.viewport.window.keys.bindPress(Keybinds.Player.down,  this.moveListener);
        er.viewport.window.keys.bindPress(Keybinds.Player.left,  this.moveListener);
        er.viewport.window.keys.bindPress(Keybinds.Player.right, this.moveListener);

        er.viewport.window.keys.bindRel(Keybinds.Player.up,    this.moveListener);
        er.viewport.window.keys.bindRel(Keybinds.Player.down,  this.moveListener);
        er.viewport.window.keys.bindRel(Keybinds.Player.left,  this.moveListener);
        er.viewport.window.keys.bindRel(Keybinds.Player.right, this.moveListener);
    }   

    public void unbindControl(EngineR2 er)
    {
        er.viewport.window.keys.unbindPress(Keybinds.Player.up,    this.moveListener);
        er.viewport.window.keys.unbindPress(Keybinds.Player.down,  this.moveListener);
        er.viewport.window.keys.unbindPress(Keybinds.Player.left,  this.moveListener);
        er.viewport.window.keys.unbindPress(Keybinds.Player.right, this.moveListener);

        er.viewport.window.keys.unbindRel(Keybinds.Player.up,    this.moveListener);
        er.viewport.window.keys.unbindRel(Keybinds.Player.down,  this.moveListener);
        er.viewport.window.keys.unbindRel(Keybinds.Player.left,  this.moveListener);
        er.viewport.window.keys.unbindRel(Keybinds.Player.right, this.moveListener);
    }
    

    /* Hit Methods
    --====-----------
    */



    /* Updating Methods
    --====----------------
    */

    public Anchor mouseAnchor = null;


    public boolean update(double delta)
    {   
        boolean stop = false;
        // Strategy for: arbitrarily moving the player around the world each update
        //  - accumulate a vector which consists of Vec2 being modified by their VecModifier in <vecs>
        //  

        double thisXV = 0.0,
               thisYV = 0.0;

        if( this.mouseAnchor != null )
        {
            double mouseX = this.mouseAnchor.getX(),
                   mouseY = this.mouseAnchor.getY();

            // this.gun.aim(mouseX, mouseY);
        }


        for( PairedValues<Vec2, VecModifier> vs : Player.this.vecs.getList() )
        {
            Vec2 v = vs.getK();
            double _x = v.getX() * delta,
                   _y = v.getY() * delta;

            if( vs.getV() != null )
            {
                _x = vs.getV().modify(_x);
                _y = vs.getV().modify(_y);
            }
            v.take(_x, _y);

            thisXV += _x;
            thisYV += _y;
        }

        this.vel.set(thisXV, thisYV);
        this.pos.add(thisXV, thisYV);


        // for( EngineR2 instance : Player.this.trackstances )
        // {
        //     instance.viewport.setCenterX(-this.pos.getX());
        //     instance.viewport.setCenterY(-this.pos.getY());
        // }

        return stop;
    }



    /* Actions Updating Methods
    --=====-----------------------
    */

    public final LocalVecList vecs = new LocalVecList();

    public class LocalVecList extends VecList
    {
        private List<PairedValues<Vec2, VecModifier>> getList()
        {
            return this.list;
        }
    }


    private ActionManager actions = new ActionManager();

    private KeyListener moveListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {
            MovementAction act = (MovementAction)actions.get(MovementAction.NAME);
            if( act != null )
                actions.blockedRun(act, new MovementArgs(kc.getName(), true));
        }

        public void onRelease(KeyCombo kc)
        {
            MovementAction act = (MovementAction)actions.get(MovementAction.NAME);
            if( act != null )
                actions.blockedRun(act, new MovementArgs(kc.getName(), false));
        }
    };


    /* Drawing Methods
    --====---------------
    */

    private RectMods rmods = new RectMods();

    public void drawAt(double x, double y, ViewPort vp)
    {
        this.rmods
            .offset.with(x, y)
            .offset.with(this.pos.getX() * -0.5, this.pos.getY() * -0.5)
        ;

        vp.draw.rect.withMod(this.rmods);
        vp.draw.rect.poslen(0, 0, 10, 10);
        vp.draw.rect.clearMod();

        this.rmods
            .offset.with(-x, -y)
            .offset.with(this.pos.getX() * 0.5, this.pos.getY() * 0.5)

        ;
    }
}