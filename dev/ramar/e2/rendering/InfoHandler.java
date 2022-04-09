package dev.ramar.e2.rendering;

public abstract class InfoHandler
{

	public final ViewPort vp;

	public InfoHandler(InfoBuilder ib)
	{
		this.vp = ib.vp;
	}

	public class InfoBuilder
	{
		public ViewPort vp = null;
		public InfoBuilder withViewPort(ViewPort vp)
		{   
			this.vp = vp;
			return this;
		}

	}

	public abstract ViewPort getViewPort();
}