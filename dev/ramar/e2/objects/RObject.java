package dev.ramar.e2.objects;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;

public class RObject implements Drawable
{

	protected RObject parent = null;

	public void setup(RObject parent)
	{
		this.parent = parent;
	}

	public void shutdown() 
	{  }


	public void drawAt(double x, double y, Viewport vp)
	{  }
}