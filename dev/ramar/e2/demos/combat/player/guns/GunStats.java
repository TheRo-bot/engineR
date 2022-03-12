package dev.ramar.e2.demos.combat.player.guns;

public class GunStats
{
	public static class Defaults
	{
		public static final double VELOCITY = 5000.0;
	}


	public GunStats()
	{

	}


	/* Class-Fields
	--===-------------
	*/


	/*
	Class-Field: velocity
	 - Represents how fast a bullet fired will travel (in whocares/s)
	*/
	private double velocity = GunStats.Defaults.VELOCITY;
	public double getVelocity()
	{   return this.velocity;    }

	public void setVelocity(double velocity)
	{   this.velocity = velocity;   }

	public GunStats witVelocity(double velocity)
	{
		this.setVelocity(velocity);
		return this;		
	}

}