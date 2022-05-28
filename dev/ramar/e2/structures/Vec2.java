package dev.ramar.e2.structures;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Vec2 implements Point
{

	private static DecimalFormat df = new DecimalFormat("0.00");

	static
	{
		df.setRoundingMode(RoundingMode.HALF_UP);
	}

	public double x = 0.0,
				  y = 0.0;

	public Vec2()  {}

	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vec2(double c)
	{
		this.x = c;
		this.y = c;
	}

	public Vec2(Vec2 c)
	{
		this.x = c.getX();
		this.y = c.getY();
	}



	/* Object Methods
	--====--------------
	*/



	public int hashCode()
	{
		int hash = 7;

		hash = 31 * hash + (int)x;
		hash = 47 * hash + (int)y;

		return hash;
	}


	public boolean equals(Object in)
	{
		boolean equal = false;
		Vec2 inV;
		if( in instanceof Vec2 )
		{
			inV = (Vec2)in;
			if( this.x - inV.x == 0.00)
				if( this.y - inV.y == 0.00 )
					equal = true;
		}

		return equal;
	}

	public Vec2 clone()
	{
		return new Vec2(this);
	}


	public String toString()
	{
		return "(" + df.format(x) + ", " + df.format(y) + ")";
	}


	/* Getters / Setters
	--====-----------------
	*/

	public Vec2 set(Vec2 pos)
	{
		if( pos != null )
			return this.set(pos.getX(), pos.getY());

		return this;
	}


	public Vec2 set(double x, double y)
	{
		this.setX(x);
		this.setY(y);

		return this;
	}

	public Vec2 set(double c)
	{
		return this.set(c, c);
	}
	

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public Vec2 clear()
	{
		this.setX(0.0);
		this.setY(0.0);

		return this;
	}


	public Vec2 add(Vec2 change)
	{
		if( change != null )
			return this.add(change.getX(), change.getY());

		return this;
	}

	public Vec2 add(double x, double y)
	{
		this.setX(this.x + x);
		this.setY(this.y + y);

		return this;
	}


	public Vec2 take(Vec2 change)
	{
		if( change != null )
			return this.take(change.getX(), change.getY());

		return this;
	}


	public Vec2 take(double x, double y)
	{
		this.setX(this.x - x);
		this.setY(this.y - y);

		return this;
	}


	public Vec2 multiply(Vec2 change)
	{
		if( change != null )
			return this.multiply(change.getX(), change.getY());

		return this;
	}


	public Vec2 multiply(double c)
	{
		return this.multiply(c, c);
	}


	public Vec2 multiply(double x, double y)
	{
		this.setX(this.x * x);
		this.setY(this.y * y);

		return this;
	}



	public Vec2 divide(Vec2 change)
	{
		if( change != null )
			return this.divide(change.getX(), change.getY());

		return this;
	}


	public Vec2 divide(double x, double y)
	{
		if( x == 0.000 || y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + this.x + ", " + this.y + ") / (" + x + ", " + y + ")");

		this.setX(this.x /= x);
		this.setY(this.y /= y);

		return this;
	}


	public Vec2 divide(double c)
	{
		return this.divide(c, c);
	}

    public double getX() {  return this.x;  }
    public double getY() {  return this.y;  }

    public double addX(double x)  {  this.x += x; return x;  }
    public double addY(double y)  {  this.y += y; return y;  }

    public double minX(double x)  {  this.x -= x; return x;  }
    public double minY(double y)  {  this.y -= y; return y;  }

    public double mulX(double x)  {  this.x *= x; return x;  }
    public double mulY(double y)  {  this.y *= y; return y;  }

    public double divX(double x)  {  this.x /= x; return x;  }
    public double divY(double y)  {  this.y /= y; return y;  }
}