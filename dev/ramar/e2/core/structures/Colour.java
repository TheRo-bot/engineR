package dev.ramar.e2.core.structures;


import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Colour
{

	private static Set<Colour> randomColours = new HashSet<>();


	private static int getNewVal(Set<Integer> ints, Random rd, int max)
	{
		int val = rd.nextInt(max);
		while( !ints.contains(val) )
		{
			val = rd.nextInt(max);
			System.out.print("val: " + val);
		}
		System.out.println("final val: " + val);
		ints.add(val);

		return val;
	}

	private int red, green, blue, alpha;

	public Colour(int r, int g, int b, int a)
	{
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	public Colour(Random rd)
	{

		do
		{
			this.red = rd.nextInt(255);
			this.green = rd.nextInt(255); rd.nextInt(255);
			this.blue = rd.nextInt(255);
			this.alpha = rd.nextInt(255);
		}
		while( randomColours.contains(this) );

		randomColours.add(this);
	}

	public Colour(Colour c)
	{
		this.red = c.red;
		this.green = c.green;
		this.blue = c.blue;
		this.alpha = c.alpha;
	}

	public Colour clone()
	{
		return new Colour(this);
	}

	public boolean equals(Object in)
	{
		boolean equal = false;
		Colour inC;

		if( in instanceof Colour )
		{
			inC = (Colour)in;
			if( this.red == inC.red )
				if( this.blue == inC.blue )
					if( this.green == inC.green )
						if( this.alpha == inC.alpha )
							equal = true;
		}

		return equal;

	}

	public String toString()
	{
		return "(R: " + red + ", G: " + green + ", B: " + blue + ", A: " + alpha + ")";
	}

	public int getRed()
	{
		return red;
	}


	public int getGreen()
	{
		return green;
	}

	public int getBlue()
	{
		return blue;
	}

	public int getAlpha()
	{
		return alpha;
	}


	public void set(int r, int g, int b, int a)
	{
		red = Math.max(0, Math.min(255, r));
		green = Math.max(0, Math.min(255, g));
		blue = Math.max(0, Math.min(255, b));
		alpha = Math.max(0, Math.min(255, a));
	}

	public void set(double r, double g, double b, double a)
	{
        red = (int)(Math.max(0, Math.min(1.0, r)) * 255.0);
        green = (int)(Math.max(0, Math.min(1.0, g)) * 255.0);
        blue = (int)(Math.max(0, Math.min(1.0, b)) * 255.0);
        alpha = (int)(Math.max(0, Math.min(1.0, a)) * 255.0);
	}

	public void set(Colour c)
	{
		set(c.red, c.green, c.blue, c.alpha);
	}

	public void setRed(int colour)
	{
		red = colour;
	}
	public Colour withRed(int r)
	{
		this.red = r;
		return this;
	}


	public void setGreen(int colour)
	{
		green = colour;
	}
	public Colour withGreen(int g)
	{
		this.green = g;
		return this;
	}

	public void setBlue(int colour)
	{
		blue = colour;
	}
	public Colour withBlue(int g)
	{
		this.green = g;
		return this;
	}

	public void setAlpha(int colour)
	{
		alpha = colour;
	}
	public Colour withAlpha(int a)
	{
		this.alpha = a;
		return this;
	}


	public java.awt.Color convertToColor()
	{
		return new java.awt.Color(red, green, blue, alpha);
	}


	public void fillG2D(java.awt.Graphics2D g2d)
	{
		g2d.setColor(this.convertToColor());
	}

}