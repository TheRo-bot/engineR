package dev.ramar.e2.demos.combat.hitboxes;

import dev.ramar.e2.rendering.Drawable;

public abstract class Hitbox implements Drawable
{
    public static class HitLayers
    {
        public static class Player
        {
            public static final int COLLIDE = 0;
            public static final int BULLETS = 1;
        }

        public static class Enemy
        {
            public static final int COLLIDE = 2;
            public static final int BULLETS = 3;
        }
    }

    public abstract boolean collidesWith(Rectbox rb);

}