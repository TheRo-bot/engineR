package dev.ramar.e2.demos.combat.enemies;

import dev.ramar.e2.demos.combat.Timer;

import dev.ramar.e2.demos.combat.actions.Action;
import dev.ramar.e2.demos.combat.actions.ActionManager;
import dev.ramar.e2.demos.combat.actions.shooting.ShootingAction;

import dev.ramar.e2.demos.combat.Player;

import java.util.Random;

public class EnemyBrainAction extends Action
{
    public static final String NAME = "enemy:brain";
    private Enemy enemy;

    public EnemyBrainAction(Enemy enemy)
    {
        super(EnemyBrainAction.NAME);
        this.enemy = enemy;
    }

    private boolean isThinking = false;

    private Random rd = new Random();

    public void think(ActionManager... ams)
    {
        this.enemy.withTarget(null);
        for( Player p : this.enemy.demo.players )
        {
            if( p.health >= 0 )
            {
                this.enemy.withTarget(p);
                break;
            }
        }

        if( this.enemy.gun.clip == 0 )
        {
            this.enemy.getAction_reload().blockedReload(ams);
        }
        else
        {
            ShootingAction sa = this.enemy.getAction_shooting();
            sa.blockedStartShooting(ams);
            Timer.getInstance().after(rd.nextDouble() * 2, () ->
            {
                sa.blockedStopShooting(ams);
            });
        }
    }

    public void blockedThink(ActionManager... ams)
    {
        if( !this.isBlocked(ams) )
        {
            this.toBlock.block(ams);
            this.think(ams);
        }
    }
}