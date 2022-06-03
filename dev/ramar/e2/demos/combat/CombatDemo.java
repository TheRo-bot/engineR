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


    List<Player> players = new ArrayList<>();
    List<Enemy>  enemies = new ArrayList<>();
    List<Rectbox> hitboxes = new ArrayList<>();

    Thread testThread;

    @Override   
    public void initialise()
    {
        super.initialise();   
        this.init_player();

        java.util.Random rd = new java.util.Random();

        int dist = 500;

        for( int ii = 0; ii < 10; ii++ )
        {
            Enemy enemy = new Enemy(rd.nextInt(dist) - dist * 0.5, rd.nextInt(dist) - dist * -0.5)
                .withDemo(this)
            ;
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

    private void connect_player_mainstance(EngineR2 er)
    {
        // establish keybinds
        for( Player p : this.players )
        {
            p.bindControl(er);
            er.viewport.layers.mid.add(p);
        }

        er.viewport.window.onClose.add(() -> { if( testThread != null ) testThread.interrupt(); } );
    }

    private void disconenct_player_mainstance(EngineR2 er)
    {
        for( Player p : this.players )
            // revoke keybinds
            p.unbindControl(er);
    }


    private void connect_player(EngineR2 er, boolean mainstance)
    {
        // viewing
        for( Rectbox rb : this.hitboxes )
            er.viewport.layers.mid.add(rb);
    }


    private void disconnect_player(EngineR2 er, boolean mainstance)
    {
        // viewing
        for( Player p : this.players )
            er.viewport.layers.mid.remove(p);
    }


    protected void connectMainstance(EngineR2 ms)
    {
        if( this.players.size() > 0 )
            this.connect_player_mainstance(ms);
        this.connectNormstance(ms);

        ms.viewport.window.onClose.add(() ->
        {
            DeltaUpdater.getInstance().interrupt();
        });
    }


    protected void disconnectMainstance(EngineR2 ms)
    {
        if( this.players.size() > 0 )
            this.disconenct_player_mainstance(ms);
        this.disconnectNormstance(ms);
    }



    protected void connectNormstance(EngineR2 ns)
    {
        if( this.players.size() > 0 )
            this.connect_player(ns, false);
    }


    protected void disconnectNormstance(EngineR2 ns)
    {
        if( this.players.size() > 0 )
            this.disconnect_player(ns, false);
    }


}