package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.*;
import dev.ramar.e2.demos.combat.actions.*;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.PairedValues;

import java.util.Set;
import java.util.HashSet;


/*
Action: MovementAction
 - Makes a player move 
*/
public class MovementAction extends Action
{
    public static final String NAME = "ability:player:movement";

    Player player = null;
    private ActionManager am = null;

    private Vec2 vec = null;
    private boolean[] directions = new boolean[4];

    private static final double SPEED = 2;
    private static final double ACCEL = 10;

    public MovementAction(ActionManager am, Player p)
    {
        super(MovementAction.NAME);

        this.am = am;
        this.player = p;
        this.vec = this.player.vecs.create((double val) ->
        {
            return val * SPEED * ACCEL;
        });

        DeltaUpdater.getInstance().toUpdate.add((double delta) -> 
        {
            boolean stop = false;
            double x = 0.0,
                   y = 0.0;
            if( directions[0] )
                y -= 1.0;
            if( directions[1] )
                y += 1.0;
            if( directions[2] )
                x -= 1.0;
            if( directions[3] )
                x += 1.0;

            if( !this.am.isBlocked(MovementAction.this) )
            {
                double abs = Math.sqrt(x * x + y * y);
                if( abs > 0 )
                    this.vec.add(x / abs, y / abs);

                // TODO: limit speed?
                // not needed, we have good movement code B)
            }

            return stop;
        }); 
    }

    public void act(ActionManager am, Object... o)
    {
        if( o.length > 1 )
        {
            String name = (String)o[0];
            boolean pressed = (boolean)o[1];

            this.toBlock.block(am);

            this.process((String)o[0], (boolean)o[1]);

            this.toBlock.unblock(am);
        }
    }

    private Set<PairedValues<String, Boolean>> toParse = new HashSet<>();

    public void blockedAct(ActionManager am, Object... o)
    {
        String name = (String)o[0];
        boolean process = (boolean)o[1];

        boolean didOverride = false;

        for( PairedValues<String, Boolean> sb : toParse )
            if( sb.getK().equals(name) )
            {
                sb.setV(process);
                didOverride = true;
                break;
            }

        if( !didOverride )
            toParse.add(new PairedValues<String, Boolean>(name, process));
    }

    public void onUnblock()
    {
        for( PairedValues<String, Boolean> vals : toParse )
        {
            String name = vals.getK();
            boolean type = vals.getV();
            process(name, type);
        }
        toParse.clear();
    }   



    private boolean process(String name, boolean pressed)
    {
        boolean acted = false;

        switch(name)
        {
            case "up":
                this.directions[0] = pressed;
                acted = true;
                break;

            case "down":
                this.directions[1] = pressed;
                acted = true;
                break;

            case "left":
                this.directions[2] = pressed;
                acted = true;
                break;

            case "right":
                this.directions[3] = pressed;
                acted = true;
                break;
        }

        return acted;
    }

}