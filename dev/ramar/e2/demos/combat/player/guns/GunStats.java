package dev.ramar.e2.demos.combat.player.guns;

public class GunStats
{
	public static class Defaults
	{
		public static final double VELOCITY = 5000.0;
		public static final double TIME_TO_LIVE = 3.0;
		public static final int CLIP_SIZE = 30;

	}


	public GunStats()
	{

	}


	/* Class-Fields
	--===-------------
	*/

	/*
	Class-Field: clipSize
	 - How much gun can shoot
	*/
	private int clipSize = GunStats.Defaults.CLIP_SIZE;

	public int getClipSize()
	{   return this.clipSize;    }

	public void setClipSize(int cs)
	{   this.clipSize = cs;   }

	public GunStats withClipSize(int cs)
	{
		this.setVelocity(cs);
		return this;		
	}


	/*
	Class-Field: velocity
	 - Represents how fast a bullet fired will travel (in whocares/s)
	*/
	private double velocity = GunStats.Defaults.VELOCITY;
	public double getVelocity()
	{   return this.velocity;    }

	public void setVelocity(double velocity)
	{   this.velocity = velocity;   }

	public GunStats withVelocity(double velocity)
	{
		this.setVelocity(velocity);
		return this;		
	}


	/*
	Class-Field: timeToLive
	 - How long the bullet has until it dies (tragically)
	*/
	private double timeToLive = GunStats.Defaults.TIME_TO_LIVE;
	public double getTimeToLive()
	{   return this.timeToLive;   }

	public void setTimeToLive(double ttl)
	{   this.timeToLive = ttl;   }

	public GunStats withTimeToLive(double ttl)
	{
		this.setTimeToLive(ttl);
		return this;
	}

}