package dev.ramar.e2.structures;


public class WindowSettings
{

	public int screenW, screenH;

	private boolean fullscreen;
	
	public String title;

	public WindowSettings(int sW, int sH, boolean fullscreen, String title)
	{
		screenW = sW;
		screenH = sH;
		this.fullscreen = fullscreen;
		this.title = title;
	}

	public boolean fullscreen()
	{   return fullscreen;   }

	public String getTitle()
	{   return title;   }

}