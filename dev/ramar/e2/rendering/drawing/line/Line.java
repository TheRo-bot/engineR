package dev.ramar.e2.rendering.drawing.line;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.Viewport;

import dev.ramar.e2.structures.Vec2;

public class Line implements Drawable
{
	public Line()
	{

	}

	public Line(double fx, double fy, double tx, double ty)
	{
		this.from.set(fx, fy);
		this.to.set(tx, ty);
	}

	public Line(Vec2 from, Vec2 to)
	{
		this.from = from;
		this.to = to;
	}

	public Vec2 from = new Vec2();
	public Vec2 to = new Vec2();


	protected LineMods mod = new LineMods();
	public LineMods getMod()
	{  return this.mod;  }

	public void drawAt(double x, double y, Viewport vp)
	{
		if( this.from != null && this.to != null )
		{
			vp.draw.line.withMod(this.mod);

			vp.draw.line.pospos(
				from.getX() + x,
				from.getY() + y,
				to.getX() + x,
				to.getY() + y
			);

			vp.draw.line.clearMod();
		}
	}
}