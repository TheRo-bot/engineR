package dev.ramar.e2.core.structures;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Vec2
{

	private static final DecimalFormat df = new DecimalFormat("0.0000");

	static
	{
		df.setRoundingMode(RoundingMode.HALF_UP);
	}

	private double x = 0.0;
	private double y = 0.0;

	public Vec2() {}

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
		this.x = c.x;
		this.y = c.y;
	}

	public synchronized int hashCode()
	{
		int hash = 7;

		hash = 31 * hash + (int)x;
		hash = 47 * hash + (int)y;

		return hash;
	}


	public synchronized boolean equals(Object in)
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

	public synchronized Vec2 clone()
	{
		return new Vec2(this);
	}


	public synchronized String toString()
	{
		return "(" + df.format(x) + ", " + df.format(y) + ")";
	}

	public synchronized double getX()
	{   return x;   }

	public synchronized double getY()
	{   return y;   }


	public synchronized Vec2 set(Vec2 pos)
	{
		if( pos != null )
		{
			this.setX(pos.x);
			this.setY(pos.y);
		}

		return this;
	}


	public synchronized Vec2 set(double x, double y)
	{
		this.setX(x);
		this.setY(y);

		return this;
	}

	public synchronized Vec2 set(double c)
	{
		this.setX(c);
		this.setY(c);

		return this;
	}
	

	public synchronized void setX(double x)
	{
		this.x = x;
	}

	public synchronized void setY(double y)
	{
		this.y = y;
	}

	public synchronized Vec2 clear()
	{
		this.setX(0);
		this.setY(0);

		return this;
	}


	public synchronized Vec2 add(Vec2 change)
	{
		if( change != null )
		{
			this.setX(this.x + change.x);
			this.setY(this.y + change.y);
		}

		return this;
	}

	public synchronized Vec2 add(double x, double y)
	{
		this.setX(this.x + x);
		this.setY(this.y + y);

		return this;
	}


	public synchronized Vec2 copy()
	{
		return new Vec2(this.x, this.y);
	}

	public synchronized Vec2 take(Vec2 change)
	{
		if( change != null )
		{
			this.setX(this.x - change.x);
			this.setY(this.y - change.y);
		}

		return this;
	}

	public synchronized Vec2 take(double x, double y)
	{
		this.setX(this.x - x);
		this.setY(this.y - y);

		return this;
	}


	public synchronized Vec2 multiply(Vec2 change)
	{
		if( change != null )
		{
			this.setX(this.x * change.x);
			this.setY(this.y * change.y);
		}

		return this;
	}


	public synchronized Vec2 multiply(double constant)
	{
		this.setX(this.x * constant);
		this.setY(this.y * constant);

		return this;
	}


	public synchronized Vec2 multiply(double x, double y)
	{
		this.setX(this.x * x);
		this.setY(this.y * y);

		return this;
	}

	public synchronized Vec2 divide(Vec2 change)
	{
		if( change == null )
			return this;

		if( change.x == 0.000 || change.y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / (" + change.x + ", " + change.y + ")");

		this.setX(x / change.x);
		this.setY(y / change.y);

		return this;
	}


	public synchronized Vec2 divide(double x, double y)
	{
		if( x == 0.000 || y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + this.x + ", " + this.y + ") / (" + x + ", " + y + ")");

		this.setX(this.x / x);
		this.setY(this.y / y);

		return this;
	}

	public synchronized Vec2 divide(double constant)
	{
		if( constant == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / " + constant);

		this.setX(this.x / constant);
		this.setY(this.y / constant);

		return this;
	}

}