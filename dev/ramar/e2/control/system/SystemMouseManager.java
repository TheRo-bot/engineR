package dev.ramar.e2.control.system;

import dev.ramar.e2.control.MouseManager;

import dev.ramar.e2.rendering.Window;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;
/*
MouseManager: SystemMouseManager
 - This uses system-hook from https://github.com/kristian/system-hook/
*/
public class SystemMouseManager extends MouseManager
{

	private Window window = null;

	public SystemMouseManager(Window window)
	{
		this.window = window;
		this.mouseHook = new GlobalMouseHook();

		this.mouseHook.addMouseListener(new GlobalMouseAdapter()
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

			@Override 
			public void mousePressed(GlobalMouseEvent event)  {
				SystemMouseManager.this.onPress(this.convertButton(event.getButton()));
			}
			
			@Override 
			public void mouseReleased(GlobalMouseEvent event)  {
				SystemMouseManager.this.onRelease(this.convertButton(event.getButton()));
			}
			

	        @Override
	        public void mouseMoved(GlobalMouseEvent e)
	        {
	        	double x = this.convertX(e.getX()),
	        		   y = this.convertY(e.getY());
	        	SystemMouseManager.this.onMove(x, y);
	        }

			@Override 
			public void mouseWheel(GlobalMouseEvent event) {
				SystemMouseManager.this.onWheel(event.getDelta() / -120.0);
			}
		});
	}

	protected GlobalMouseHook mouseHook = null;



}