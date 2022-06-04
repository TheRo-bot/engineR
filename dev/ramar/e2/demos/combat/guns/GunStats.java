package dev.ramar.e2.demos.combat.guns;


public class GunStats
{

    public static class Defaults
    {
        public static final double VELOCITY = 30.0;
        public static final int CLIP_SIZE = 10;

        public static final double RELOAD_TIME = 0.5;
    }


    public double velocity = Defaults.VELOCITY;
    public int clipSize = Defaults.CLIP_SIZE;
    public double reloadTime = Defaults.RELOAD_TIME;
}