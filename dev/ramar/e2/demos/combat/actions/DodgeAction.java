package dev.ramar.e2.demos.combat.actions;

import dev.ramar.e2.demos.combat.actions.ActionManager.Action;

public class DodgeAction extends Action
{
    private Player player;

    public DodgeAction(Player p)
    {   
        this.player = p;
    }

    public String getName()
    {
        return "dodge";
    }

    public boolean act(Object[] o)
    {
        boolean out = false;
        if( this.player != null ) 
        {
            double dodgeAngle = 0.0;

            // figure out what direction the player is moving

            // 
        }
        return out;
    }
}