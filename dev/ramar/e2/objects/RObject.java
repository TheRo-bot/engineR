package dev.ramr.e2.objects;

import dev.ramar.e2.core.rendering.Drawable;

public class RObject implements Drawable
{

	protected RObject parent = null;

	public void setup(RObject parent)
	{
		this.parent = parent;
	}

	public void shutdown() 
	{  }
}