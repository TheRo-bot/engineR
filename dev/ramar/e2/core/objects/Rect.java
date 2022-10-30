package dev.ramar.e2.core.objects;

import dev.ramar.e2.core.drawing.rect.RectMods;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;

import dev.ramar.e2.core.structures.Vec2;

public class Rect extends RObject
{

	public final Vec2 pos = new Vec2(),
				      len = new Vec2();

	public Rect()
	{

	}

	public Rect(double x, double y)
	{
		this.pos.set(x, y);
	}

	public Rect(double x, double y, double l, double h)
	{
		this(x, y);
		this.len.set(l, h);
	}


	public final RectMods mods = new RectMods();
	public RectMods getMod() {  return this.mods;  }


	public void drawAt(double ox, double oy, Viewport vp)
	{
		double x, y, w, h;

		synchronized(this)
		{
			vp.draw.rect.withMod(this.mods);

			x = pos.getX();
			y = pos.getY();
			w = len.getX();
			h = len.getY();
			
		}

		x -= w * 0.5;
		y -= h * 0.5;

		vp.draw.rect.poslen(ox + x, oy + y, w, h);

		vp.draw.rect.clearMod();
	}
}