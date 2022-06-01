package dev.ramar.e2.demos.combat;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.rect.RectMods;

import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.actions.Action;
import dev.ramar.e2.demos.combat.actions.ActionManager;

import dev.ramar.e2.rendering.control.MouseListener;
import dev.ramar.e2.rendering.control.KeyListener;
import dev.ramar.e2.rendering.control.KeyCombo;

import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolyline;

import dev.ramar.e2.demos.combat.actions.*;
/* Action, ActionManager */

import dev.ramar.e2.demos.combat.actions.movement.*;
/* MovementAction, MovementArgs */

import dev.ramar.e2.demos.combat.actions.shooting.ShootingAction;
import dev.ramar.e2.demos.combat.actions.shooting.ReloadAction;

import dev.ramar.e2.structures.Point;

import dev.ramar.e2.demos.combat.hitboxes.Rectbox;

import dev.ramar.e2.demos.combat.guns.semiauto.SemiAuto;

import dev.ramar.e2.demos.combat.guns.bullets.BaseBulletFactory;
import dev.ramar.e2.demos.combat.guns.bullets.Bullet;

import dev.ramar.utils.PairedValues;
import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.ArrayList;


public class Player implements Drawable, Point, Updatable
{
    public Player()
    {
        this.rmods
            .colour.with(255, 255, 255, 255)
            .fill.with()
        ;
        this.setup();

        this.move = new MoveHandler(this);
        this.box.drawing
            .fill.with()
            .colour.with(0, 0, 255, 255)
        ;

        // gun.shots.onAdd.add((Bullet b) -> 
        // {
        //     System.out.println("i got a bullet");
        // });
        this.gun
            .withBulletFactory(new BaseBulletFactory())
            .withOrigin(this)
        ;
        this.gun.onShoot.add((Bullet b) ->
        {   
            DeltaUpdater.getInstance().toUpdate.queueAdd(b);
            Player.this.bullets.add(b);
        });
    }


    public SemiAuto gun = new SemiAuto();

    private Vec2 pos = new Vec2(), 
                 vel = new Vec2();


    private void setup()
    {
        this.actions.add(new MovementAction(this.actions, this));

        ShootingAction sa = new ShootingAction(this);
        this.actions.add(sa);

        ReloadAction ra = new ReloadAction(this);
        ra.toBlock.add(sa);
        this.actions.add(ra);
    }

    public void startUpdate()
    {  DeltaUpdater.getInstance().toUpdate.add(this);  }

    public void stopUpdate()
    {  DeltaUpdater.getInstance().toUpdate.remove(this);  }


    /* Point Implementation
    --====--------------------
    */

    public double getX()  {  return this.pos.getX();  }
    public double getY()  {  return this.pos.getY();  }

    public double addX(double x)  {  return this.pos.addX(x);  }
    public double addY(double y)  {  return this.pos.addY(y);  }

    public double minX(double x)  {  return this.pos.minX(x);  }
    public double minY(double y)  {  return this.pos.minY(y);  }

    public double mulX(double x)  {  return this.pos.mulX(x);  }
    public double mulY(double y)  {  return this.pos.mulY(y);  }

    public double divX(double x)  {  return this.pos.divX(x);  }
    public double divY(double y)  {  return this.pos.divY(y);  }


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

        er.viewport.window.keys.bindRel(Keybinds.Player.reload, this.reloadListener);

        this.getAction_shooting().withTarget(er.viewport.window.mouse.getUpdatingVec());

        er.viewport.window.mouse.onPress.add(this.shootListener);
        er.viewport.window.mouse.onRelease.add(this.shootListener);
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

        er.viewport.window.keys.unbindRel(Keybinds.Player.reload, this.reloadListener);

        er.viewport.window.mouse.onPress.remove(this.shootListener);
        er.viewport.window.mouse.onRelease.remove(this.shootListener);
    }
    

    /* Hit Methods
    --====-----------
    */



    /* Updating Methods
    --====----------------
    */

    public Anchor mouseAnchor = null;

    public final LocalList<Rectbox> hitboxes = new LocalList<>();

    public class LocalList<E> extends HiddenList
    {
        private List<E> getList() {  return this.list;  }
    } 

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


        boolean collides = false;
        for( Rectbox hb : hitboxes.getList() )
            if( hb.collidesWith(this.box) )
            {
                collides = true;
                break;
            }
        if( collides )
            this.box.drawing
                .colour.with(255, 0, 0, 255)
            ;   
        else
            this.box.drawing
                .colour.with(0, 0, 255, 255)
            ;
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


    public final ActionManager actions = new ActionManager();

    /* Movement Actions Methods
    --=====-----------------------
    */

    public MovementAction getAction_movement()
    {  return (MovementAction)this.actions.get(MovementAction.NAME);  }


    private KeyListener moveListener = new KeyListener()
    {
        public void proc(String name, boolean press)
        {
            switch(name)
            {
                case "up":
                    Player.this.move.up(press);  break;
                case "down":
                    Player.this.move.down(press);  break;
                case "left":
                    Player.this.move.left(press);  break;
                case "right":
                    Player.this.move.right(press);  break;
            }
        }

        public void onPress(KeyCombo kc)
        {  this.proc(kc.getName(), true);  }

        public void onRelease(KeyCombo kc)
        {  this.proc(kc.getName(), false);  }
    };

    public final MoveHandler move;

    public class MoveHandler
    {
        public Player p;
        public MoveHandler(Player p)
        {
            this.p = p;
        }

        public void up(boolean doing)
        {
            MovementAction ma = this.p.getAction_movement();
            if( ma != null )
                ma.blockedUp(doing, this.p.actions);
        }

        public void down(boolean doing)
        {
            MovementAction ma = this.p.getAction_movement();
            if( ma != null )
                ma.blockedDown(doing, this.p.actions);
        }

        public void left(boolean doing)
        {
            MovementAction ma = this.p.getAction_movement();
            if( ma != null )
                ma.blockedLeft(doing, this.p.actions);
        }

        public void right(boolean doing)
        {
            MovementAction ma = this.p.getAction_movement();
            if( ma != null )
                ma.blockedRight(doing, this.p.actions);
        }

    };


    /* Shooting Action Methods
    --=====----------------------
    */

    public ShootingAction getAction_shooting()
    {   return (ShootingAction)this.actions.get(ShootingAction.NAME);   }

    public ReloadAction getAction_reload()
    {   return (ReloadAction)this.actions.get(ReloadAction.NAME);   }

    private MouseListener shootListener = new MouseListener()
    {
        public void mousePressed(int bID, double x, double y)
        {
            if( bID == 1 )
            {
                ShootingAction sa = Player.this.getAction_shooting();
                sa.blockedStartShooting(Player.this.actions);
            }
        }

        public void mouseReleased(int bID, double x, double y)
        {
            if( bID == 1 )
            {
                ShootingAction sa = Player.this.getAction_shooting();
                sa.blockedStopShooting(Player.this.actions);
            }
        }

    };

    private KeyListener reloadListener = new KeyListener()
    {
        public void onPress(KeyCombo kc)
        {}

        public void onRelease(KeyCombo kc)
        {
            ReloadAction ra = Player.this.getAction_reload();
            ra.blockedReload(Player.this.actions);
        }
    };

    /* Drawing Methods
    --====---------------
    */

    Rectbox box = new Rectbox(this, 15, 15);

    private AWTPolyline pline = new AWTPolyline();

    private RectMods rmods = new RectMods();

    private List<Bullet> bullets = new ArrayList<>();
    public void drawAt(double x, double y, ViewPort vp)
    {
        this.box.drawAt(x, y, vp);

        for( int ii = 0; ii < this.bullets.size(); ii++ )
        {
            Bullet b = this.bullets.get(ii);
            b.drawAt(x, y, vp);
        }
    }
}