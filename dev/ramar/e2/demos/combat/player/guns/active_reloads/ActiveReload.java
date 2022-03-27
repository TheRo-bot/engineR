package dev.ramar.e2.demos.combat.player.guns.active_reloads;


public interface ActiveReload
{


    /*
    Method: initiaite
     - Tells the reload lifecycle to continue where it left off
       (or start if it needs to)
    */
    public abstract void initiate();

    /*
    Method: interrupt
     - Tells the reload lifecycle to save the last action for later
       (or reset if it's finished)
    */
    public abstract void interrupt();
}