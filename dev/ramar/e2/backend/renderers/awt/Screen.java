package dev.ramar.e2.backend.renderers.awt;

import java.awt.*;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.StaticInfo;
import dev.ramar.e2.structures.WindowSettings;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
/*
Class: Screen
 - The Actual Window that AWT creates for Java
*/
public class Screen extends Canvas
{
	private JFrame frame;
	private WindowSettings ws;

	public Screen(WindowSettings ws, Canvas cv)
	{
		this.ws = ws;
		frame = new JFrame(ws.getTitle());

		frame.addComponentListener(new ComponentListener()
		{
			public void componentHidden(ComponentEvent e) {}

			public void componentMoved(ComponentEvent e) {}


			public void componentResized(ComponentEvent e)
			{
				onResize(frame.getWidth(), frame.getHeight());

			}

			public void componentShown(ComponentEvent e) {}
		});


		Dimension d = new Dimension(ws.screenW, ws.screenH);

		if( !ws.fullscreen() )
		{
			frame.setPreferredSize( d );
			frame.setMaximumSize( d );
			frame.setMinimumSize( d );
			// frame.setResizable(false);	
			frame.setLocationRelativeTo(null);			
			// frame.setSize(1000, 1000);

		}
		else
		{
			frame.setSize(1920, 1080);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setUndecorated(true);
		}

		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				onClose();
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.add(this);

		createBufferStrategy(3);

	}


	/*
	Method: getWidth
	 - Gets the width of the window in pixels
	*/
	public int getWidth()
	{
		return (int)getSize().getWidth();
	}


	/*
	Method: getHeight
	 - Gets the height of the window in pixels
	*/
	public int getHeight()
	{
		return (int)getSize().getHeight();
	}


	/*
	Method: isFocused
	 - If the window is actively being used 
	*/
	public boolean isFocused()
	{
		return frame.isFocused();
	}


	/*
	Method: getMonitorPosition
	 - Returns the position on the screen-space of where the
	   window is
	*/
	public Vec2 getMonitorPosition()
	{
		Point p = frame.getLocation();
		return new Vec2(p.getX(), p.getY());
	}  


	public Vec2 getMouseOffset()
	{
		if( ws.fullscreen() )
			return StaticInfo.Mouse.fullscreenOffset;
		else
			return StaticInfo.Mouse.windowedOffset;
	}


	public void onClose()
	{
		System.out.println("close!");
	}


	private void onResize(int newWidth, int newHeight)
	{
		// System.out.println("resized! " + newWidth + ", " + newHeight);
	}


}