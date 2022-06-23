package dev.ramar.e2.demos.combat.guns.semiauto;


import dev.ramar.e2.demos.combat.guns.GunStats;

public class SemiAutoStats extends GunStats
{

    public SemiAutoStats()
    {

    }


    public static class Defaults
    {
        public static final double SHOOT_DELAY = 0.1; 
        public static final int CHAIN_SHOT_AMOUNT = 20;
        public static final double TIME_TO_LIVE = 1.0;
        public static final double CHAIN_SHOT_END_LAG = 1.0;
    }

    public double shootDelay = Defaults.SHOOT_DELAY;
    public int chainShotAmount = Defaults.CHAIN_SHOT_AMOUNT;
    public double chainShotEndLag = Defaults.CHAIN_SHOT_END_LAG;
    public double timeToLive = Defaults.TIME_TO_LIVE;

}