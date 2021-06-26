package dev.ramar.e2.structures.hitboxes;


import dev.ramar.e2.structures.*;
import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.ViewPort;


import java.util.*;


public class HitCircle implements HitShape
{
	private Vec2 pos;
	private double radius;

	public HitCircle(Vec2 pos, double radius)
	{
		this.pos = pos;
		this.radius = radius;
	}

	public HitCircle(double x, double y, double radius)
	{
		this.pos = new Vec2(x, y);
		this.radius = radius;
	}


	public HitCircle(HitCircle hc)
	{
		this.pos = hc.pos.clone();
		this.radius = hc.radius;
	}

	public HitShape clone()
	{
		return new HitCircle(this);
	}	


	public double getRadius()
	{
		return radius;
	}


	public double getWidth()
	{
		return radius;
	}

	public double getHeight()
	{
		return radius;
	}


	public Vec2 getDimensions()
	{
		return new Vec2(radius, radius);
	}

	public boolean pointInsideOff(Vec2 ourP, Vec2 testP)
	{
		double testX = testP.getX();
		double testY = testP.getY();

		double ourX = pos.getX();
		double ourY = pos.getY();

		if( ourP != null )
		{
			ourX += ourP.getX();
			ourY += ourP.getY();
		}

		double part1 = testX - ourX;
		double part2 = testY - ourY;
		double dSquared = part1 * part1 + part2 * part2;

		return dSquared <= radius * radius;


	}

	public boolean pointInside(Vec2 p)
	{

		return pointInsideOff(null, p);
	}


	private Vec2 memVec = new Vec2(0, 0);

	public boolean collidesAt(Vec2 p, Hittable h)
	{
		boolean colliding = false;

		if( definingPoints == null )
			calculateDefiningPoints(1.0);

		for( Vec2 point : definingPoints )
		{
			memVec.set(point.getX() + p.getX(), point.getY() + p.getY());
			colliding = h.pointWithin(memVec);
			if( colliding )
				break;
		}

		return colliding;
	}


	private List<Vec2> definingPoints;


	// how specific should the calculation be (as a percentage)
	// calculates points that are centered at 0, 0
	// intended to be used so they are calculated very infrequently
	// (maybe even once), so that these points can be offset and tested
	// without having to constantly be re-created
	public List<Vec2> calculateDefiningPoints(double fidelityP)
	{

		List<Vec2> points = new ArrayList<>();

		if( fidelityP < 0.01)
			fidelityP = 0.01;
		else if( fidelityP > 1.0 )
			fidelityP = 1.0;

		int maxPoints = 360;
		maxPoints = (int)(maxPoints * fidelityP);

		double angle = maxPoints / Math.PI;

		for( int ii = 0; ii < maxPoints; ii++ )
		{
			double xp = 0 + radius * Math.cos(ii * angle);
			double yp = 0 + radius * Math.sin(ii * angle);

			points.add(new Vec2(xp, yp));
		}	

		definingPoints = points;
		return points;
	}

	public void drawSelf(ViewPort vp)
	{
		drawSelfOff(null, vp);
	}
	
	public void drawSelfOff(Vec2 p, ViewPort vp)
	{
		if( p != null )
			drawSelfOff(p.getX(), p.getY(), vp);
		else
			drawSelfOff(0, 0, vp);
	}

	public void drawSelfOff(double x, double y, ViewPort vp)
	{
		double ourCX = pos.getX() - radius/2 + x;
		double ourCY = pos.getY() - radius/2 + y;

		vp.outlineCircle(ourCX, ourCY, radius);
	}

}