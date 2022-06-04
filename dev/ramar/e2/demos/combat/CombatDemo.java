package dev.ramar.e2.demos.combat;

import dev.ramar.e2.demos.BaseDemo;
import dev.ramar.e2.demos.DemoManager.Demo;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.demos.combat.hitboxes.Rectbox;
import dev.ramar.e2.demos.combat.actions.Action;

import dev.ramar.e2.demos.combat.hitboxes.HitManager;

import dev.ramar.e2.demos.combat.enemies.Enemy;

import java.util.List;
import java.util.ArrayList;

public class CombatDemo extends BaseDemo
{
    public CombatDemo()
    {
        if( DeltaUpdater.getInstance() != null )
            DeltaUpdater.getInstance().start();
    }


    public final List<Player> players = new ArrayList<>();
    public final List<Enemy>  enemies = new ArrayList<>();

    public final List<EngineR2> instances = new ArrayList<>();
    public EngineR2 mainstance = null;

    Thread testThread;

    @Override   
    public void initialise()
    {
        super.initialise();   
        this.init_player();

        java.util.Random rd = new java.util.Random();

        int dist = 500;

        for( int ii = 0; ii < 100; ii++ )
        {
            double  an = rd.nextInt(360),
                   pow = rd.nextInt(dist);

            double x = Math.sin(an) * pow,
                   y = Math.cos(an) * pow;

            Enemy enemy = new Enemy(x, y)
                .withDemo(this)
            ;

            this.hitman.add("enemy:bodies", enemy.hitter);

            this.enemies.add(enemy);
        }


    }


    public final HitManager hitman = new HitManager();

    /* Player Methods
    --===---------------
    */

    private void init_player()
    {
        java.util.Random rd = new java.util.Random();


        for( int ii = 0; ii < 1; ii++ )
        {
            Player p = new Player()
                .withDemo(this)
            ;

            p.getAction_movement().speed += rd.nextDouble();
            p.getAction_movement().accel += rd.nextDouble();
            int dist = 100;
            p.addX(rd.nextInt(dist) - dist * 0.5);
            p.addY(rd.nextInt(dist) - dist * 0.5);
            this.players.add(p);
            p.startUpdate();
        }
        System.out.println(this.players.size());
    }

    /* new shit
    --===---------
    */

    protected void connectMainstance(EngineR2 main)
    {
        this.mainstance = main;

        for( Player p : this.players )
            p.bindControl(main);

        this.connectNormstance(main);
        main.viewport.window.onClose.add(() ->
        {
            DeltaUpdater.getInstance().interrupt();
        });        
    }

    protected void disconnectMainstance(EngineR2 main)
    {
        this.mainstance = null;

        for( Player p : this.players )
            p.unbindControl(main);
        
        this.disconnectNormstance(main);
        main.viewport.window.onClose.add(() -> { if( testThread != null ) testThread.interrupt(); } );
    }


    protected void connectNormstance(EngineR2 norm)
    {
        for( Player p : this.players )
            norm.viewport.layers.top.add(p);

        for( Enemy e : this.enemies )
            norm.viewport.layers.mid.add(e);

        this.instances.add(norm);
    }

    protected void disconnectNormstance(EngineR2 norm)
    {
        for( Player p : this.players )
            norm.viewport.layers.top.remove(p);

        for( Enemy e : this.enemies )
            norm.viewport.layers.mid.remove(e);

        this.instances.remove(norm);
    }

}