package dev.ramar.e2.rendering;

import dev.ramar.e2.structures.Vec2;

import java.lang.reflect.TypeVariable;

public abstract class Window_NEW
{
    public enum FullscreenState
    {
        WINDOWED,
        FULLSCREEN,
        WINDOWED_BORDERLESS
    }


    /* Properties
	--===-----------
    */

    /* ScreenSize Property
	--====-------------------
    */

	// public abstract Window_NEW withScreenSize(double w, double h);

 //    public Vec2 getScreenSize()
 //    {  return new Vec2(this.getScreenSize_width(), getScreenSize_height());  }


 //    public abstract double getScreenSize_width();
 //    public abstract double getScreenSize_height();


 //    /* ScreenRes Property
	// --====------------------
 //    */


	// public abstract Window_NEW withScreenRes(double w, double h);

 //    public Vec2 getScreenRes()
 //    {  return new Vec2(this.getScreenRes_width(), getScreenRes_height());  }


 //    public abstract double getScreenRes_width();
 //    public abstract double getScreenRes_height();


 //    /* PPMM Property
	// --====-------------
 //    */

 //    public abstract double getPPMM();
	// public abstract Window_NEW withPPMM(double ppmm);



    /* Title Property
	--====--------------
    */

    public abstract String getTitle();
    public abstract Window_NEW withTitle(String title);

	public abstract void createScreen();
	
}