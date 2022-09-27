package dev.ramar.e2.control.awt;

import dev.ramar.e2.control.MouseManager;

import dev.ramar.e2.rendering.awt.AWTWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class AWTMouseManager extends MouseManager
{

	public AWTMouseManager(AWTWindow window)
	{
		this.adapter = this.createMouseAdapter();
		this.window = window;
	}

	private AWTWindow window = null;

	// the adapter of the canvas
    public final MouseAdapter adapter;

	public double toRawX(double x) { return x; }
	public double toRawY(double y) { return y; }

	protected MouseAdapter createMouseAdapter()
	{
		return new MouseAdapter()
	    {
	        // ex = eventX || MouseEvent.getX()
	        private double convertX(double ex)
	        {
				AWTWindow wdow = AWTMouseManager.this.window;
	        	if( wdow != null )
	        	{
	        		ex = (ex + 1) / wdow.canvas.getWidth() * wdow.getResolutionW();
	        		ex -= wdow.getResolutionW() * 0.5;
	        		ex -= wdow.viewport.getCenterX();
	        	}

	        	return ex;
	        }
	        // ey = eventY || MouseEvent.getY()
	        private double convertY(double ey)
	        {
				AWTWindow wdow = AWTMouseManager.this.window;
	        	if( wdow != null )
	        	{
	        		ey = (ey + 1) / wdow.canvas.getHeight() * wdow.getResolutionH();
	        		ey -= wdow.getResolutionH() * 0.5;
	        		ey -= wdow.viewport.getCenterY();
	        	}
	        	
	        	return ey;
	        }

	        @Override        
	        public void mousePressed(MouseEvent e)
	        {
	        	AWTMouseManager.this.onPress(e.getButton());
	        }


	        @Override
	        public void mouseDragged(MouseEvent e)
	        {
	        	double x = this.convertX(e.getX()),
	        		   y = this.convertY(e.getY());
	        	AWTMouseManager.this.onMove(x, y);
	        }

	        @Override
	        public void mouseMoved(MouseEvent e)
	        {
	        	double x = this.convertX(e.getX()),
	        		   y = this.convertY(e.getY());
	        	AWTMouseManager.this.onMove(x, y);
	        }

	        @Override        
	        public void mouseReleased(MouseEvent e)
	        {
	        	double x = this.convertX(e.getX()),
	        		   y = this.convertY(e.getY());
	        	AWTMouseManager.this.onRelease(e.getButton());
	        }

	        @Override        
	        public void mouseWheelMoved(MouseWheelEvent e)
	        {
	        	AWTMouseManager.this.onWheel(e.getPreciseWheelRotation());
	        }

	    };
	}
}