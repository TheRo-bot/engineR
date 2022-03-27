package dev.ramar.e2.demos.combat.player.guns.fullauto;

import dev.ramar.e2.demos.combat.player.guns.GunStats;


public class FullAutoStats extends GunStats
{
	public static class Defaults
	{
		public static final double FIRE_RATE = 3.0;
		public static final double DECEL_MULTIPLIER = 1.0;
		public static final double SPREAD = 5.0;
		public static final double SPREAD_MODIFIER = 3;
		public static final double SPREAD_REDUCTION = 3;
	}


	/*
	Class-Field: fireRate
	 - Represetns how many times the gun can shoot per second
	*/
	private double fireRate = FullAutoStats.Defaults.FIRE_RATE; 

	public double getFireRate()
	{   return this.fireRate;   }


	public void setFireRate(double fireRate)
	{   this.fireRate = fireRate;   }


	public FullAutoStats withFireRate(double fireRate)
	{
		this.setFireRate(fireRate);
		return this;
	}


    public FullAutoStats withVelocity(double velocity)
    {
        this.setVelocity(velocity);
        return this;
    }


    /*
	Class-Field: decelerationMultiplier
	 - how fast the bullet will slow down (how much drag?? in arbitrariums)
    */
    private double decelerationMultiplier = FullAutoStats.Defaults.DECEL_MULTIPLIER;

    public double getDeceleration()
    {   return this.decelerationMultiplier;   }

    public void setDeceleration(double decel)
    {   this.decelerationMultiplier = decel;   }

    public FullAutoStats withDeceleration(double decel)
    {
    	this.setDeceleration(decel);
    	return this;
    }


    /*
	Class-field: spread
	 - the radius of a 'circle' 
    */
    private double spread = FullAutoStats.Defaults.SPREAD;

    public double getSpread()
    {   return this.spread;   }

    public void setSpread(double spread)
    {   this.spread = spread;   }

    public FullAutoStats withSpread(double spread)
    {
    	this.setSpread(spread);
    	return this;
    }


    /*
    Class-Field: spreadModifier
     - Over time (delta), spread is increased by this value
    */
    private double spreadModifier = FullAutoStats.Defaults.SPREAD_MODIFIER;

    public double getSpreadModifier()
    {   return this.spreadModifier;   }

    public void setSpreadModifier(double sm)
    {   this.spreadModifier = sm;   }

    public FullAutoStats withSpreadModifier(double sm)
    {
    	this.setSpreadModifier(sm);
    	return this;
    }


    /*
	Class-Field: spreadReduction
	 - modifies how fast spread is reduced over time (delta)
    */
    private double spreadReduction = FullAutoStats.Defaults.SPREAD_REDUCTION;

    public double getSpreadReduction()
    {   return this.spreadReduction;   }

    public void setSpreadReduction(double sr)
    {   this.spreadReduction = sr;   }

    public FullAutoStats withSpreadReduction(double sr)
    {
    	this.setSpreadReduction(sr);
    	return this;
    }

}