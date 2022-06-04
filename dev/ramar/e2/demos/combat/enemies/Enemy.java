package dev.ramar.e2.demos.combat.enemies;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.rect.RectMods;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.demos.combat.hitboxes.Hitter;
import dev.ramar.e2.demos.combat.hitboxes.Rectbox;

import dev.ramar.e2.demos.combat.guns.bullets.Bullet;
import dev.ramar.e2.demos.combat.guns.bullets.BaseBulletFactory;

import dev.ramar.e2.demos.combat.CombatDemo;

import dev.ramar.e2.demos.combat.actions.shooting.ReloadAction;
import dev.ramar.e2.demos.combat.actions.shooting.ShootingAction;
import dev.ramar.e2.demos.combat.actions.ActionManager;

import dev.ramar.e2.demos.combat.guns.semiauto.SemiAuto;

import dev.ramar.e2.structures.Point;
import dev.ramar.e2.structures.Vec2;

public class Enemy implements Drawable, Point, Updatable
{
    public final Hitter<Enemy, Rectbox> hitter = new Hitter<>(this);

    public Enemy(double x, double y)
    {
        this.pos.set(x, y);
        this.hitter.box = new Rectbox(10, 10)
            .withAnchor(this.pos)
        ;
        this.hitter.onHit.add((Hitter collidee) -> 
        {
            Object owner = collidee.getOwner();
            if( owner instanceof Bullet )
                this.onShot((Bullet)owner);
        });

        this.hitter.box.drawing
            .colour.withA((this.health + 1) / (double)this.maxHealth)
        ;   

        this.actions
            .withAdd(new ShootingAction(this.gun))
            .withAdd(new ReloadAction(this.gun))
            .withAdd(new EnemyBrainAction(this))
        ;

        this.setupGun();
        this.setupActions();
    }




    public final ActionManager actions = new ActionManager();

    public ShootingAction getAction_shooting()
    {   return (ShootingAction)this.actions.get(ShootingAction.NAME);   }

    public ReloadAction getAction_reload()
    {   return (ReloadAction)this.actions.get(ReloadAction.NAME);   }

    public EnemyBrainAction getAction_brain()
    {  return (EnemyBrainAction)this.actions.get(EnemyBrainAction.NAME);   }



    public void setupActions()
    {
        EnemyBrainAction brain = this.getAction_brain();
        ReloadAction reload = this.getAction_reload();
        ShootingAction shoot = this.getAction_shooting();

        reload.toBlock.add(shoot, brain);
        shoot.toBlock.add(brain);
    }



    public int maxHealth = 5,
               health = new java.util.Random().nextInt(this.maxHealth);

    public void onShot(Bullet b)
    {
        this.health--;

        this.hitter.box.drawing
            .colour.withA((this.health + 1) / (double)(this.maxHealth))
        ;

        b.kill(this.demo);

        if( this.health <= 0 )
            this.kill();

    }


    public void kill()
    {
        this.demo.enemies.remove(this);

        DeltaUpdater.getInstance().toUpdate.queueRemove(this);

        for( EngineR2 instance : this.demo.instances )
            instance.viewport.layers.mid.remove(this);

        this.demo.hitman.queueRemove("enemy:bodies", this.hitter);
    }


    public Enemy withTarget(Point target)
    {   
        this.getAction_shooting().withTarget(target);
        return this;
    }



    private CombatDemo demo = null;
    public Enemy withDemo(CombatDemo cd)
    {
        this.demo = cd;
        return this;
    }

    /* Drawable Implementation
    --===------------------------
    */

    public void drawAt(double x, double y, ViewPort vp)
    {
        if( hitter.box != null )
            hitter.box.drawAt(x, y, vp);

        if( this.getAction_reload().isReloading() )
        {
            vp.draw.rect.withMod()
                .colour.with(94, 45, 142, 255)
                .fill.with()
                .offset.with(x, y)
                .offset.with(0, 10)
                .offset.with(this.getX(), this.getY())
            ;

            int w = 30,
                h =  4;
            vp.draw.rect.poslen(w * -0.5, h * -0.5, w, h);
            vp.draw.rect.clearMod();
        }
    }

    /* Updatable Implementation
    --===-------------------------
    */


    public SemiAuto gun = new SemiAuto();

    private void setupGun()
    {
        this.gun
            .withBulletFactory(new BaseBulletFactory()
            {
                public Bullet make(double xv, double yv)
                {
                    Bullet out = super.make(xv, yv);
                    out.mods
                        .colour.with(181, 66, 84, 255)
                    ;
                    return out;
                }
            })
            .withOrigin(this)
        ;
        this.gun.stats.clipSize = 10;
        this.gun.stats.shootDelay = 0.075;
        this.gun.stats.chainShotAmount = 3;
        this.gun.reload();
        this.gun.onShoot.add((Bullet b) ->
        {   
            DeltaUpdater.getInstance().toUpdate.queueAdd(b);
            Enemy.this.demo.hitman.add("enemy:bullets", b.hitter);

            for( EngineR2 instance : Enemy.this.demo.instances )
                instance.viewport.layers.top.add(b);

            b.onCease.add(() ->
            {
                DeltaUpdater.getInstance().toUpdate.queueRemove(b);
                Enemy.this.demo.hitman.queueRemove("enemy:bullets", b.hitter);

                for( EngineR2 instance : Enemy.this.demo.instances )
                    instance.viewport.layers.top.remove(b);
            });
        });
    }

    private double waitTime = 1.5,
                   delta = 0.0;

    public boolean update(double d)
    {
        boolean kill = false;

        this.delta -= d;

        if( this.delta <= 0.0 )
        {
            this.delta = this.waitTime;

            this.getAction_brain().blockedThink(this.actions);
        }


        return kill;
    }



    /* Point Implementation
    --===---------------------
    */  

    private final Vec2 pos = new Vec2();

    public double getX()
    {   return this.pos.getX();   }

    public double getY()
    {   return this.pos.getY();   }


    public double addX(double x)
    {   return this.pos.addX(x);   }

    public double addY(double y)
    {   return this.pos.addY(y);   }


    public double minX(double x)
    {   return this.pos.minX(x);   }

    public double minY(double y)
    {   return this.pos.minY(y);   }


    public double mulX(double x)
    {   return this.pos.mulX(x);   }

    public double mulY(double y)
    {   return this.pos.mulY(y);   }


    public double divX(double x)
    {   return this.pos.divX(x);   }

    public double divY(double y)
    {   return this.pos.divY(y);   }

}