package dev.ramar.e2.core.objects;

import dev.ramar.e2.core.drawing.rect.RectMods;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;

import dev.ramar.e2.core.structures.Vec2;

public class Rect extends RObject
{

	public final Vec2 pos = new Vec2(),
				      len = new Vec2();


	public final RectMods mods = new RectMods();
	public RectMods getMod() {  return this.mods;  }


	public void drawAt(double ox, double oy, Viewport vp)
	{
		vp.draw.rect.withMod(this.mods);


		double x = pos.getX(),
			   y = pos.getY(),
			   w = len.getX(),
			   h = len.getY()
		;

		x -= w * 0.5;
		y -= h * 0.5;

		vp.draw.rect.poslen(ox + x, oy + y, w, h);

		vp.draw.rect.clearMod();
	}
}