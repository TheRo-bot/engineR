package dev.ramar.e2.core.objects;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;

public class RObject implements Drawable
{

	protected RObject parent = null;

	// prepare everything for drawing
	public void setup(RObject parent)
	{
		this.parent = parent;
	}

	// undo all your preparation
	public void shutdown() 
	{  }


	// draw to the viewport
	public void drawAt(double x, double y, Viewport vp)
	{  }
}