package dev.ramar.e2.backend;

/*
Class: Moment
 - A simple structure to store the specific second and update
   that an Updatable is in
*/
public class Moment
{

	private int second;
	private int update;

	private double ups;

	public Moment(double ups)
	{
		second = update = 0;
		this.ups = ups;
	}

	public Moment(int s, int u, double ups)
	{
		second = s;
		update = u;
		this.ups = ups;
	}

	public String toString()
	{
		return second + "." + update + " (" + ups + ")";
	}

	public int getUpdate()
	{
		return update;
	}

	public int getSecond()
	{
		return second;
	}

	public double getUPS()
	{
		return ups;
	}


	public void setUPS(double ups)
	{
		this.ups = ups;
	}

	public void step()
	{
		update++;
		if( update > ups )
		{
			second++;
			update = 0;
		}

	}


}