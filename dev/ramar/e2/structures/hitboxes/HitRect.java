package dev.ramar.e2.structures.hitboxes;


import dev.ramar.e2.structures.*;
import dev.ramar.e2.interfaces.collision.Hittable;
import dev.ramar.e2.interfaces.collision.HitShape;

import dev.ramar.e2.interfaces.rendering.ViewPort;


import java.util.*;

public class HitRect implements HitShape
{

	// center position (if in a hitbox, it's an offset)
	private Vec2 pos = new Vec2(0, 0);
	// the positional offsets of pos with width and height
	private List<Vec2> offsets = new ArrayList<>();
	// distance from center to edge
	private double width, height;

	private Group g;

	private void initialiseOffsets()
	{
		for( int ii = 0; ii < 4; ii++ )
			offsets.add(new Vec2(0, 0));

		updateOffsets();
	}

	public void updateOffsets()
	{
		long startTime = System.nanoTime();
		offsets.get(0).clear().add( width/2,  height/2).add(pos);
		offsets.get(1).clear().add( width/2, -height/2).add(pos);
		offsets.get(2).clear().add(-width/2,  height/2).add(pos);
		offsets.get(3).clear().add(-width/2, -height/2).add(pos);

		// System.out.println("time to update: " + (System.nanoTime() - startTime) + "ns" );
	}

	public HitRect(double x, double y, double w, double h)
	{
		pos.set(x, y);
		width = w;
		height = h;
		initialiseOffsets();
	}

	public HitRect(Vec2 pos, double w, double h)
	{
		this.pos.set(pos);
		width = w;
		height = h;
		initialiseOffsets();
	}

	public HitRect(HitRect hc)
	{
		this.pos.set(hc.pos.getX(), hc.pos.getY());
		this.width = hc.width;
		this.height = hc.height;
		initialiseOffsets();
	}


	public HitShape clone()
	{
		return new HitRect(this);
	}	


	/* HitShape implementation
	------------------------------
	*/


	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}


	public Vec2 getDimensions()
	{
		return new Vec2(width, height);
	}

	public boolean pointInsideOff(Vec2 off, Vec2 p)
	{
		double ourCX, ourCY;
		ourCX = pos.getX();
		ourCY = pos.getY();

		if( off != null )
		{
			ourCX += off.getX();
			ourCY += off.getY();
		}

		double tl_X = ourCX - width/2;
		double tl_Y = ourCY - height/2;

		double br_X = ourCX + width/2;
		double br_Y = ourCY + height/2;

		boolean inside = false;

		if( tl_X <= p.getX() && br_X >= p.getX() )
			if( tl_Y <= p.getY() && br_Y >= p.getY() )
				inside = true;

		// inside = ( p.getX() >= tl_X && p.getX() <= tl_X ) &&
		// 	( p.getY() >= tl_Y && p.getY() <= tl_Y );

		// System.out.println("point " + p + " is " + (inside ? "within" : "not within") + " (" + new Vec2(tl_X, tl_Y) + ") and (" + new Vec2(br_X, br_Y) + ")");

		return inside;
	}

	// an empty vec2 to be used by shapeInsideOff
	private Vec2 vecMem = new Vec2(0, 0);

	private boolean setAndTest(HitShape hs, Vec2 off, double x, double y)
	{
		vecMem.set(x, y);
		return hs.pointInsideOff(off, vecMem);
	}

	public boolean shapeInsideOff(Vec2 ourOff, HitShape hs, Vec2 theirOff)
	{
		long timeTestStart = System.nanoTime();
		double ourCX = pos.getX();
		double ourCY = pos.getY();

		if( ourOff != null )
		{
			ourCX += ourOff.getX();
			ourCY += ourOff.getY();
		}

		boolean inside = false;
		double halfWidth = width/2;
		double halfHeight = height/2;

		int testCount = 0;
		// center
		if(! setAndTest(hs, theirOff, ourCX, ourCY) )
		{
			testCount++;
			// top left
			if(! setAndTest(hs, theirOff, ourCX - halfWidth, ourCY - halfHeight) )
			{
				testCount++;
				// center top
				if(! setAndTest(hs, theirOff, ourCX, ourCY - halfHeight ) )
				{
					testCount++;
					// right top
					if(! setAndTest(hs, theirOff, ourCX + halfWidth, ourCY - halfHeight))
					{
						testCount++;
						// left center
						if(! setAndTest(hs, theirOff, ourCX - halfWidth, ourCY) )
						{
							testCount++;
							// right center
							if(! setAndTest(hs, theirOff, ourCX + halfWidth, ourCY) )
							{
								testCount++;
								// left bot
								if(! setAndTest(hs, theirOff, ourCX - halfWidth, ourCY + halfHeight) )
								{
									testCount++;
									// center bot
									if(! setAndTest(hs, theirOff, ourCX, ourCY + halfHeight) )
									{
										testCount++;
										// right bot 
										if(! setAndTest(hs, theirOff, ourCX + halfWidth, ourCY + halfHeight) )
										{
											// System.out.println("[shapeInsideOff] failed in " + (System.nanoTime() - timeTestStart) + "ns");
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// System.out.println("[shapeInsideOff] succeeded in " + (System.nanoTime() - timeTestStart) + "ns (" + testCount + " tests)");

		return true;
	}


	public boolean pointInside(Vec2 p)
	{
		return pointInsideOff(null, p);
	}


	public boolean collidesAt(Vec2 p, Hittable h)
	{
		boolean collides = false;

		Vec2 hOffset = h.getPos();
		for( HitShape shape : h.getShapes() )
		{
			collides = shapeInsideOff(p, shape, hOffset);
			if( collides )
				break;
		}

		return collides;
	}

	public List<Vec2> calculateDefiningPoints(double fidelityP)
	{
		List<Vec2> points = new ArrayList<>();
		System.out.println("[hitrect] calculate defining points w fidelity = " + fidelityP);
		double topWall = -height/2;
		double botWall =  height/2;
		double leftWall = - width/2;
		double rightWall =   width/2;


		// minimum fidelity is 1%, as it's only then the corners themselves 
		if( fidelityP < 0.01 )
			fidelityP = 0.01;
		else if( fidelityP > 1.0 )
			fidelityP = 1.0;


		// distance to place the next point (equal to width / fidelity as a number)
		double wDistToPoint = width * (1 / (fidelityP * 100));
		double hDistToPoint = height * (1 / (fidelityP * 100));

		// do top and bottom sides
		for( double ii = leftWall; ii <= rightWall; ii += wDistToPoint)
		{
			// top wall point
 			points.add(new Vec2(ii, topWall));
 			// bottom wall point
 			points.add(new Vec2(ii, botWall));
		}

		int index = 0;
		// do left and right sides
		// don't do the first and last points as they've already been calculated!
		for( double ii = topWall + hDistToPoint; ii <= botWall - hDistToPoint; ii += hDistToPoint)
		{
			// left wall point
			points.add(new Vec2(leftWall, ii));
			// right wall point
			points.add(new Vec2(rightWall, ii));

			index++;
		}


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
		double ourX = pos.getX() + x;
		double ourY = pos.getY() + y;

		vp.outlineRect((int)(ourX - width/2), (int)(ourY - height/2), (int)width, (int)height);		

	}


	/* END
	------------
	*/


	public void setGroup(Group g)
	{
		this.g = g;
	}

	// doesn't updateOffsets() before getting the corner
	public Vec2 forceGetCorner(boolean top, boolean left)
	{
		int index = 0;
		if( left )
			index += 2;
		if( top )
			index += 1;

		return offsets.get(index);
	}

	public Vec2 getCorner(boolean top, boolean left)
	{
		updateOffsets();

		int index = 0;
		if( left )
			index += 2;
		if( top )
			index += 1; 

		return offsets.get(index);
	}

	/*
	Public Method: pointWithin
	 - for the Hittable contract, tests if p is within this HitRect
	*/
	public boolean pointWithin(Vec2 p)
	{
		updateOffsets();
		return pointWithin(forceGetCorner(true, true), p, forceGetCorner(false, false));
	}


	private boolean pointWithin(Vec2 leftTopMost, Vec2 middle, Vec2 botRightMost)
	{
		boolean within = false;
		if( middle.getX() >= leftTopMost.getX() && middle.getX() <= botRightMost.getX() )
		{
			if( middle.getY() >= leftTopMost.getY() && middle.getY() <= botRightMost.getY())
			{
				within = true;
			}
		}
		System.out.println(middle + " " + (within ? "is" : "isn't") + " within " + leftTopMost + " and " + botRightMost);
		return within;
	}




	public void onCollision()
	{

	}

	public void onNoCollision()
	{

	}

}