package dev.ramar.e2.core.objects;

import dev.ramar.e2.core.rendering.Drawable;
import dev.ramar.e2.core.rendering.Viewport;

import dev.ramar.e2.core.structures.Vec2;

import dev.ramar.utils.HiddenList;

import java.util.List;
import java.util.LinkedList;

public class Container extends RObject
{

	public Container()
	{
		this.onCreate();
	}

	public final Vec2 pos = new Vec2(0);

	public final LocalList<Drawable> children = new LocalList<>();

	protected void onCreate() {}

	public void setup(RObject parent)
	{
		super.setup(parent);
		synchronized(this.children)
		{
			for( Drawable draw : this.children.getList() )
				if( draw instanceof RObject )
					((RObject)draw).setup(this);
		}
	}

	public void shutdown()
	{
		synchronized(this.children)
		{
			for( Drawable draw : this.children.getList() )
				if( draw instanceof RObject )
					((RObject)draw).shutdown();
		}
	}


	public void drawAt(double x, double y, Viewport vp)
	{
		synchronized(this.children)
		{
			for( Drawable draw : this.children.getList() )
				draw.drawAt(x + pos.getX(), y + pos.getY(), vp);
		}
	}


	public class LocalList<E> extends HiddenList<E>
	{
		protected List<E> createList()
		{  return new LinkedList<E>();  }

		private List<E> getList()
		{  return this.list;  }
	}
}