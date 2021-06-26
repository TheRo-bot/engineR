package dev.ramar.e2.interfaces.collision;


import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.structures.Vec2;

import java.util.List;

/*
Interface: HitShape
 - Interface for a Collidable shape, describes what can be used
   for collision testing
*/
public interface HitShape
{
	public double getWidth();

	public double getHeight();

	public Vec2 getDimensions();

	public boolean pointInside(Vec2 p);

	public boolean pointInsideOff(Vec2 off, Vec2 p);

	public boolean collidesAt(Vec2 p, Hittable h);

	public List<Vec2> calculateDefiningPoints(double fidelityP);

	public void drawSelf(ViewPort vp);

	public void drawSelfOff(Vec2 p, ViewPort vp);

	public void drawSelfOff(double x, double y, ViewPort vp);

	public HitShape clone();

}