package dev.ramar.e2.structures;


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

	public double getX()
	{   return x;   }

	public double getY()
	{   return y;   }


	public Vec2 set(Vec2 pos)
	{
		if( pos != null )
		{
			this.x = pos.x;
			this.y = pos.y;
		}

		return this;
	}


	public Vec2 set(double x, double y)
	{
		this.x = x;
		this.y = y;

		return this;
	}

	public Vec2 set(double c)
	{
		this.x = c;
		this.y = c;

		return this;
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
		x = 0.0;
		y = 0.0;

		return this;
	}


	public Vec2 add(Vec2 change)
	{
		if( change != null )
		{
			x += change.x;
			y += change.y;
		}

		return this;
	}

	public Vec2 add(double x, double y)
	{
		this.x += x;
		this.y += y;

		return this;
	}


	public Vec2 newAdd(double x, double y)
	{
		return new Vec2(this.x + x, this.y + y);
	}

	public Vec2 newAdd(Vec2 change)
	{
		if( change != null )
			return new Vec2(x + change.x, y + change.y);
		else
			return new Vec2(x, y);

	}


	public Vec2 take(Vec2 change)
	{
		if( change != null )
		{
			x -= change.x;
			y -= change.y;
		}

		return this;
	}

	public Vec2 newTake(Vec2 change)
	{
		if( change != null )
			return new Vec2(x - change.x, y - change.y);
		else
			return new Vec2(x, y);
	}


	public Vec2 take(double x, double y)
	{
		this.x -= x;
		this.y -= y;

		return this;
	}


	public Vec2 newTake(double x, double y)
	{
		return new Vec2(this.x - x, this.y - y);
	}

	public Vec2 multiply(Vec2 change)
	{
		if( change != null )
		{
			x *= change.x;
			y *= change.y;
		}

		return this;
	}


	public Vec2 newMultiply(Vec2 change)
	{
		if( change != null )
			return new Vec2(x * change.x, y * change.y);
		else
			return new Vec2(x, y);
	}

	public Vec2 multiply(double constant)
	{
		x *= constant;
		y *= constant;

		return this;
	}


	public Vec2 multiply(double x, double y)
	{
		this.x *= x;
		this.y *= y;

		return this;
	}
/*

	public Vec2 multiply(int x, int y)
	{
		this.x *= x;
		this.y *= y;

		return this;
	}*/



	public Vec2 newMultiply(double constant)
	{
		return new Vec2(x * constant, y * constant);
	}


	public Vec2 divide(Vec2 change)
	{
		if( change == null )
			return this;

		if( change.x == 0.000 || change.y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / (" + change.x + ", " + change.y + ")");

		x /= change.x;
		y /= change.y;

		return this;
	}



	public Vec2 newDivide(Vec2 change)
	{
		if( change == null )
			return new Vec2(x, y);

		if( change.x == 0.000 || change.y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / (" + change.x + ", " + change.y + ")");

		return new Vec2(x / change.x, y / change.y);
	}


	public Vec2 newDivide(double x, double y)
	{

		if( x == 0.000 || y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + this.x + ", " + this.y + ") / (" + x + ", " + y + ")");

		return new Vec2(this.x / x, this.y / y);
	}



	public Vec2 divide(double x, double y)
	{
		if( x == 0.000 || y == 0.000 )
			throw new ArithmeticException("Division by 0 (" + this.x + ", " + this.y + ") / (" + x + ", " + y + ")");

		this.x /= x;
		this.y /= y;

		return this;
	}

	public Vec2 divide(double constant)
	{
		if( constant == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / " + constant);

		x /= constant;
		y /= constant;

		return this;
	}


	public Vec2 newDivide(double constant)
	{
		if( constant == 0.000 )
			throw new ArithmeticException("Division by 0 (" + x + ", " + y + ") / " + constant);

		return new Vec2(x / constant, y / constant);
	}

}