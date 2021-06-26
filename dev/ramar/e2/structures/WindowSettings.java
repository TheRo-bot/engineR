package dev.ramar.e2.structures;


public class WindowSettings
{
	private int viewportW, viewportH;
	private int windowW, windowH;

	private boolean fullscreen;
	private String title;

	public WindowSettings(int vpW, int vpH, int wW, int wH, boolean fullscreen, String title)
	{
		viewportW = vpW;
		viewportH = vpH;

		windowW = wW;
		windowH = wH;

		this.fullscreen = fullscreen;
		this.title = title;
	}

	public int getVPWidth()
	{   return viewportW;   }


	public int getVPHeight()
	{   return viewportH;   }


	public int getWindowWidth()
	{   return windowW;    }


	public int getWindowHeight()
	{   return windowH;   }


	public boolean fullscreen()
	{   return fullscreen;   }

	public String getTitle()
	{   return title;   }

}