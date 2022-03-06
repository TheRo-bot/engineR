package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

import dev.ramar.e2.demos.combat.Player;

import dev.ramar.e2.demos.combat.Player.Updatable;

import dev.ramar.e2.structures.Vec2;


/*
Action: DodgeAction
 - Makes the player dodge... holy shit that's crazy
*/
public class DodgeAction extends Action
{
    private Player player;

    // this is our movement vector that the player gives us
    private Vec2 vector;

    private MovementAction ma = null;

    public DodgeAction(Player p)
    {   
        this.player = p;
        this.ma = (MovementAction)this.player.actions.get("movement");


        // even though DODGE_POWER is used twice it doesn't feel the same
        // if you remove one of them / scale the other to be the same
        this.vector = this.player.vecs.create((double mod) ->
        {
            return mod * DODGE_POWER;
        });
    }

    public String getName()
    {   return "dodge";   }


    private static final double DODGE_POWER = 6;
    private static final double DODGE_POWER_2 = 6;
    private static final double DODGE_DURA = 0.25;


    public boolean act(ActionManager am, Object[] info)
    {
        double xVel = this.player.getXV(),
               yVel = this.player.getYV();

        if( Math.abs(xVel) > 0.1 || Math.abs(yVel) > 0.1 )
        {
            double abs = Math.sqrt(xVel * xVel + yVel * yVel);
            double xVelNorm = xVel / abs,
                   yVelNorm = yVel / abs;
            
            final double xDist = xVelNorm * DODGE_POWER_2,
                         yDist = yVelNorm * DODGE_POWER_2;  

            this.blockAll(am);
            Updatable updater = new Updatable()
            {
                private double delta = DODGE_DURA;

                public boolean update(double delta)
                {
                    boolean stop = false;
                    this.delta -= delta;

                    double power = (this.delta / DODGE_DURA);
                    if( this.delta < 0)
                        stop = true;
                    else
                        DodgeAction.this.vector.add(xDist * power, yDist * power);

                    // when the main dodge movement is meant to end, we want to end it with
                    // a smooth transition from dodge to anything else.'

                    // we also want to block this dodge from happening again, so in that time
                    // we re-block this action
                    if( stop )
                    {
                        // in the next update, add this updatable which lasts for 0.3 seconds,
                        // and slows down the vector
                        DodgeAction.this.player.toUpdate.queueAdd(new Updatable()
                        {
                            private double delta = 0.3;

                            public boolean update(double delta)
                            {
                                boolean _stop = false;
                                this.delta -= delta;
                                if( this.delta < 0 )
                                    _stop = true;
                                else
                                    DodgeAction.this.vector.multiply(0.98);

                                if( _stop )
                                {
                                    am.unblock(DodgeAction.this, DodgeAction.this);
                                    DodgeAction.this.vector.clear();
                                }

                                return _stop;
                            }
                        });
                        // clear everything we're meant to block
                        DodgeAction.this.unblockAll(am);
                        // and make sure we're blocked while the inner updatable finishes off
                        am.block(DodgeAction.this, DodgeAction.this);

                    }

                    return stop;
                }
            };

            this.player.toUpdate.add(updater);
        }

            
        return false;
    }
}