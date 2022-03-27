package dev.ramar.e2.demos.combat.player.guns.active_reloads;

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Drwable;

public class FullAutoReload implements ActiveReload, Drawable
{
    private ReloadState currState = ReloadState.OLD_IN;

    public enum ReloadState
    {
        OLD_IN, OLD_OUT, NEW_IN, CHAMBER_BACK, DONE;

        public ReloadState()
        {

        }

        public void update(double delta)
        {
            System.out.println(this.toString());
        }
    }

    private Gun gun;

    public FullAutoReload(Gun g)
    {  
        this.gun = g;
    }



    /* Drawable Implementation
    --===------------------------
    */

    private Drawable drawer = new Drawable()
    {
        public void drawAt(double x, double y, ViewPort vp)
        {
            switch(currState)
            {
                case OLD_IN:
                    vp.draw.stateless.text.withMod()
                        .withColour(255, 255, 255, 255)
                        .withOffset(gun.anchor.getX(), gun.anchor.getY())
                    ;
                    vp.draw.stateless.text.pos_c(0, 40, "OLD_IN");
                    break;

                case OLD_OUT:
                    vp.draw.stateless.text.withMod()
                        .withColour(255, 255, 255, 255)
                        .withOffset(gun.anchor.getX(), gun.anchor.getY())
                    ;
                    vp.draw.stateless.text.pos_c(0, 40, "OLD_OUT");
                    break;

                case NEW_IN:
                    vp.draw.stateless.text.withMod()
                        .withColour(255, 255, 0, 255)
                        .withOffset(gun.anchor.getX(), gun.anchor.getY())
                    ;
                    vp.draw.stateless.text.pos_c(0, 40, "NEW_IN");
                    break;

                case CHAMBER_BACK:
                    vp.draw.stateless.text.withMod()
                        .withColour(255, 255, 0, 255)
                        .withOffset(gun.anchor.getX(), gun.anchor.getY())
                    ;
                    vp.draw.stateless.text.pos_c(0, 40, "CHAMBER_BACK");
                    break;

                case DONE:
                    vp.draw.stateless.text.withMod()
                        .withColour(255, 0, 0, 255)
                        .withOffset(gun.anchor.getX(), gun.anchor.getY())
                    ;
                    vp.draw.stateless.text.pos_c(0, 40, "DONE");
                    break;
            }
        }
    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        this.drawer.drawAt(x, y, vp);
    }

    /* ActiveReload Implementation
    --===----------------------------
    */



    public void initiate()
    {
        DeltaUpdater.getInstance().toUpdate.queueAdd()
    }

    public void interrupt()
    {

    }
}