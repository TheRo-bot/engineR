package dev.ramar.e2.system.control;

import dev.ramar.e2.core.control.MouseManager;
import dev.ramar.e2.core.rendering.Window;

import dev.ramar.e2.core.structures.Vec2;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

import java.util.Map;
/*
MouseManager: SystemMouseManager
 - This uses system-hook from https://github.com/kristian/system-hook/
*/
public class SystemMouseManager extends MouseManager
{

	public SystemMouseManager()
	{
	}
	private Window window = null;

	public final Vec2 rawPosition = new Vec2();

	// remove the viewport's center positioning from the given x
    public double toRawX(double x)
    {
		Window wdow = this.window;
		if( wdow != null )
		{
			x += wdow.viewport.getCenterX();
			x += wdow.getResolutionW() * 0.5;
		}
		return x;
    }

	// remove the viewport's center positioning from the given y
    public double toRawY(double y)
    {
		Window wdow = this.window;
		if( wdow != null )
		{
			y += wdow.viewport.getCenterY();
			y += wdow.getResolutionH() * 0.5;
		}
		return y;
    }

    public void poll()
    {
    	double x = this.rawPosition.getX(),
    		   y = this.rawPosition.getY();

    	this.adapter.mouseMoved(x, y);
    }

    protected boolean isListening = false;
	public void listen()
	{
		this.isListening = true;
		this.mouseHook.addMouseListener(this.adapter);

	}
	public void ignore()
	{
		this.isListening = false;
		this.mouseHook.removeMouseListener(this.adapter);
	}


	public SystemMouseManager(Window window)
	{
		this.window = window;
	}

	protected GlobalMouseHook mouseHook = new GlobalMouseHook();

	public void shutdown()
	{
		System.out.println("mouse shutdown");
		this.mouseHook.shutdownHook();
	}

	protected LocalMouseAdapter adapter = new LocalMouseAdapter();

	private class LocalMouseAdapter extends GlobalMouseAdapter
	{
		private double convertX(double x)
		{
			Window wdow = SystemMouseManager.this.window;
			x -= wdow.getDeviceX();
        	if( wdow != null )
        	{
        		x = (x + 1) / wdow.getPixelWidth() * wdow.getResolutionW();
        		x -= wdow.getResolutionW() * 0.5;
        		x -= wdow.viewport.getCenterX();
        	}
			return x;
		}

        private double convertY(double y)
        {
			Window wdow = SystemMouseManager.this.window;
			y -= wdow.getDeviceY();

        	if( wdow != null )
        	{
        		y = (y + 1) / wdow.getPixelHeight() * wdow.getResolutionH();
        		y -= wdow.getResolutionH() * 0.5;
        		y -= wdow.viewport.getCenterY();
        	}
        	
        	return y;
        }

        // converts global button to EngineR button
        private int convertButton(int btn)
        {
        	switch(btn)
        	{
	        	case 1:
	        		return 1;
	        	case 2:
	        		return 3;
	        	case 16:
	        		return 2;
	        	case 32:
	        		return 4;
        		case 64:
	        		return 5;
        	}
        	return 0;
        }

        private boolean receivedDown = false;

        private boolean isValidMouseAction(double x, double y)
        {
        	Window wdow = SystemMouseManager.this.window;

        	return wdow != null
	        	&& wdow.isFocused()
	        	&& 0 <= x && x <= wdow.getPixelWidth()
	        	&& 0 <= y && y <= wdow.getPixelHeight()
        	;
        }

		@Override 
		public void mousePressed(GlobalMouseEvent event)
		{
			Window wdow = SystemMouseManager.this.window;
			if( wdow != null )
			{
				double x = event.getX() - wdow.getDeviceX();
				double y = event.getY() - wdow.getDeviceY();

				if( this.isValidMouseAction(x, y) )
				{
					synchronized(rawPosition)
					{
						rawPosition.set(event.getX(), event.getY());
					}
					this.receivedDown = true;
					SystemMouseManager.this.onPress(this.convertButton(event.getButton()));
				}
			}
		}
		
		@Override 
		public void mouseReleased(GlobalMouseEvent event)
		{

			if( this.receivedDown )
			{
				synchronized(rawPosition)
				{
					rawPosition.set(event.getX(), event.getY());
				}
				this.receivedDown = false;			
				SystemMouseManager.this.onRelease(this.convertButton(event.getButton()));
			}
		}
		

		// monitor-relative position
        @Override
        public void mouseMoved(GlobalMouseEvent event)
        {
			synchronized(rawPosition)
			{
				rawPosition.set(event.getX(), event.getY());
			}
			this.mouseMoved(event.getX(), event.getY());
        }

        public void mouseMoved(double x, double y)
        {
			SystemMouseManager.this.onMove(this.convertX(x), this.convertY(y));
        }

		@Override 
		public void mouseWheel(GlobalMouseEvent event) {
			Window wdow = SystemMouseManager.this.window;
			if( wdow != null )
			{
				double x = event.getX() - wdow.getDeviceX();
				double y = event.getY() - wdow.getDeviceY();

				if( this.isValidMouseAction(x, y) )
				{
					synchronized(rawPosition)
					{
						rawPosition.set(event.getX(), event.getY());
					}
					SystemMouseManager.this.onWheel(event.getDelta() / -120.0);
				}
			}
		}
	}




}