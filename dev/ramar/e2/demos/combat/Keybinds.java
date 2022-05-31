package dev.ramar.e2.demos.combat;

import dev.ramar.e2.rendering.control.KeyCombo;
import dev.ramar.e2.rendering.control.KeyCombo.Directionality;

public class Keybinds
{

    public static class Player
    {
        public static final String UP    = "up";
        public static final String DOWN  = "down";
        public static final String LEFT  = "left";
        public static final String RIGHT = "right";
        public static final String DODGE = "dodge";

        public static final String RELOAD = "reload";

        public static final KeyCombo    up = new KeyCombo(Player.UP).withChar('w');
        public static final KeyCombo  down = new KeyCombo(Player.DOWN).withChar('s');
        public static final KeyCombo  left = new KeyCombo(Player.LEFT).withChar('a');
        public static final KeyCombo right = new KeyCombo(Player.RIGHT).withChar('d');
        public static final KeyCombo dodge = new KeyCombo(Player.DODGE).withTShift(Directionality.LEFT);

        public static final KeyCombo reload = new KeyCombo(Player.RELOAD).withChar('r');
    }
}