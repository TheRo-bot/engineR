package dev.ramar.e2.demos.combat.player.guns.fullauto;

import dev.ramar.e2.demos.combat.player.guns.GunStats;


public class FullAutoStats extends GunStats
{
	public static class Defaults
	{
		public static final double FIRE_RATE = 3.0;
	}


	/*
	ClassField: fireRate
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

}