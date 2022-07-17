package dev.ramar.e2.structures;

import dev.ramar.e2.rendering.TypedThing;

import java.awt.Toolkit;
import dev.ramar.e2.rendering.Window_NEW;
import dev.ramar.e2.rendering.awt.AWTWindow_NEW;

import java.lang.Class;
import java.lang.InstantiationException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class WindowBuilder<E extends Window_NEW> extends TypedThing
{

	public final Vec2 size = new Vec2(1920, 1080),
				       res = new Vec2(1920, 1080);


	private Class<E> type = null;
	public WindowBuilder(Class<E> type)
	{
		this.type = type;
	}


	// my display is 96 dpi, 25.4mm in an inch
	private double ppmm = 96 * 25.4;
	public double getPPMM()
	{  return this.ppmm;  }

	public void setPPMM(double ppmm)
	{  this.ppmm = ppmm;  }

	public WindowBuilder withPPMM(double ppmm)
	{
		this.setPPMM(ppmm);
		return this;
	}


	private E create()
	{
		try
		{
			if( this.type != null )
				return this.type.newInstance();
		}
		catch(InstantiationException | IllegalAccessException e) {}

		return null;
	}


	public E build()
	{
		E out = this.create();
		out.withScreenSize(this.size.getX(), this.size.getY());
		out.withScreenRes(this.res.getX(), this.res.getY());
		out.withPPMM(this.ppmm);

		return out;
	}


}