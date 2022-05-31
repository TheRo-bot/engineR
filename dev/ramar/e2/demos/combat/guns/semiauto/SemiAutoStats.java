package dev.ramar.e2.demos.combat.guns.semiauto;


import dev.ramar.e2.demos.combat.guns.GunStats;

public class SemiAutoStats extends GunStats
{

    public SemiAutoStats()
    {

    }


    public static class Defaults
    {
        public static final double SHOOT_DELAY = 0.125; 
        public static final int CHAIN_SHOT_AMOUNT = 5;
    }

    public double shootDelay = Defaults.SHOOT_DELAY;

    public int chainShotAmount = Defaults.CHAIN_SHOT_AMOUNT;
}