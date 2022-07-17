package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Window_NEW;
import dev.ramar.e2.structures.Colour;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.geom.AffineTransform;

import javax.swing.JFrame;

public class AWTWindow_NEW extends Window_NEW
{
	private Colour BACKGROUND = new Colour(0, 0, 0, 255);
	public class Device
	{
		public static double ppmm = java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 25.4;

		public static double convertToMM(double pixels)
		{
			return pixels / ppmm;
		}


		public static double getDisplayWidth()
		{  return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();  }

		public static double getDisplayHeight()
		{  return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();  }
	}


	public AWTWindow_NEW()
	{
		this.frame.add(this.canvas);

        this.frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                AWTWindow_NEW.this.frame.dispose();
            }
        });

		// this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // this.canvas.createBufferStrategy(3);
		// this.graphics = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();
		// System.out.println("transform: " + this.graphics.getTransform());
	}

    private JFrame frame = new JFrame();
    private Canvas canvas = new Canvas();
    private Graphics2D graphics = null;

    /* Window_NEW Implementation
	--===----------------------
    */


    /* ScreenSize Property
	--====-------------------
    */


    public double screenSizeX = 1.0,
    			  screenSizeY = 1.0;

    public double getScreenSize_x()
    {  return this.screenSizeX;  }

    public double getScreenSize_y()
    {  return this.screenSizeY;  }

    public double getScreenSize_w()
    {  return this.screenSizeX * Device.getDisplayWidth();  }

    public double getScreenSize_h()
    {  return this.screenSizeY * Device.getDisplayHeight();  }



    public Window_NEW withScreenSize(double x, double y)
    {
    	this.screenSizeX = Math.max(0.0, Math.min(1.0, x));
    	this.screenSizeY = Math.max(0.0, Math.min(1.0, y));

    	this.onUpdateScreenSize();
    	return this;
    }


    private void onUpdateScreenSize()
    {
    	this.frame.setSize((int)this.getScreenSize_w(), (int)this.getScreenSize_h());
    }


    /* ScreenRes Property
	--====------------------
    */

    public double screenRes_w = 1920,
    			  screenRes_h = 1080;

   	public double getScreenRes_w()
   	{  return this.screenRes_w;  }

   	public double getScreenRes_h()
   	{  return this.screenRes_h;  }


   	public Window_NEW withScreenRes(double w, double h)
   	{
   		this.screenRes_w = w;
   		this.screenRes_h = h;

   		this.onUpdateScreenRes();
   		return this;
   	}

   	private void onUpdateScreenRes()
   	{
   		double xs = this.getScreenSize_w() / this.getScreenRes_w(),
   			   ys = this.getScreenSize_h() / this.getScreenRes_h();
   		this.graphicTransform.setToScale(xs, ys);
   	}

    /* Title Property
	--====--------------
    */

    public String getTitle()
    {
    	return this.frame.getTitle();
    }

    public Window_NEW withTitle(String title)
    {
    	this.frame.setTitle(title);
    	return this;
    }


    private AffineTransform graphicTransform = new AffineTransform();

    public Graphics2D setupGraphics()
    {
        Graphics2D g2d = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();    	

        g2d.setTransform(graphicTransform);
        g2d.setColor(this.BACKGROUND.convertToColor());
        g2d.fillRect(0, 0, (int)this.getScreenRes_w(), (int)this.getScreenRes_h());

        return g2d;
    }

    public BufferStrategy getBuffer()
    {
    	return this.canvas.getBufferStrategy();
    }

    public void show()
    {
    	this.canvas.getBufferStrategy().show();
    }

	public void createScreen()
	{

		double posX = Device.getDisplayWidth() / 2 - this.getScreenSize_w() / 2,
			   posY = Device.getDisplayHeight() / 2 - this.getScreenSize_h() / 2;

        this.frame.setDefaultLookAndFeelDecorated(false);
    	this.frame.setLocation((int)posX, (int)posY);
    	this.onUpdateScreenSize();
    	this.onUpdateScreenRes();
        this.frame.setVisible(true);
        canvas.createBufferStrategy(3);


        new Thread(() ->
        {
        	try
        	{
	        	while(true)
	        	{
	        		Graphics2D g2d = this.setupGraphics();

	        		double w = this.getScreenRes_w(),
	        			   h = this.getScreenRes_h();

	        		g2d.setColor(new Colour(255, 255, 255, 255).convertToColor());
	        		for( double ii = 0.0; ii <= 1.0; ii += 0.1 )
	        		{
	        			for( double jj = 0.0; jj <= 1.0; jj += 0.1 )
	        			{
	        				double x = w * (ii);
	        				double y = h * (jj);

	        				g2d.drawRect(0, 0, (int)x, (int)y);
	        				g2d.drawRect(0, 0, (int)x, (int)y);
	        			}
	        		}

	        		this.getBuffer().show();
	        		g2d.dispose();
	        		Thread.sleep(5);
	        	}
        	}
        	catch(InterruptedException e) {}
        }).start();

	}
}