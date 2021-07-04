package dev.ramar.e2.backend.renderers;


import dev.ramar.e2.interfaces.rendering.*;
import dev.ramar.e2.interfaces.events.listeners.*;
import dev.ramar.e2.interfaces.events.producers.*;


import dev.ramar.e2.structures.*;

import dev.ramar.e2.backend.KeyBinds;
import dev.ramar.e2.backend.renderers.awt.*;

import java.util.*;
import java.util.concurrent.*;

import java.io.File;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.FontMetrics;

import java.awt.image.BufferStrategy;
import java.awt.image.RenderedImage;

import java.awt.geom.AffineTransform;



/*
Class: AWTRenderer
 - A Renderer which functions through Java's awt package
 - the awt Canvas has a lot of functionality, so AWTRenderer takes up
   a lot of responsibilities, i kind of expect this to happen if different
   Renderers are created too, though
*/
public class AWTRenderer extends Canvas implements RenderManager, ViewPort, Controller, MouseController
{
	private static final String INNER_THREAD_NAME = "Render Thread";

	// The actual Window, this is an src.backend.renderers.awt.Screen
	// It's used for a lot of things, so it's not apart of one implementation
	private Screen screen;
	private double drawResW = 1920,
				   drawResH = 1080;
	private List<RenderManagerListener> rmListeners = new ArrayList<>();

	// This object really gets initialised at ViewPort.initialise()
	public AWTRenderer()
	{}


	public Screen getScreen()
	{
		return screen;
	}

	/* Controller Implementation
	-----------------------------------
	| Uses a private class ActionController to handle all actions
	*/

	private ActionController actionListener;


	public void initialise()
	{
		actionListener = new ActionController(screen);
	}


	public void addListener(ControlListener cl)
	{
		if(! (cl instanceof ActionListener) )
			throw new IllegalArgumentException("listener must be an ActionListener");
		actionListener.addListener((ActionListener)cl);
	}


	public void removeListener(ControlListener cl)
	{
		if(! (cl instanceof ActionListener) )
			throw new IllegalArgumentException("listener must be an ActionListener");
		actionListener.removeListener((ActionListener)cl);
	}


	public String classifyController()
	{
		return actionListener.classifyController();
	}


	// MouseController implementation
	public Vec2 getMousePos()
	{
		return actionListener.getMousePos().take(getOffsetX(), getOffsetY());
	}

	private Thread waitingThread = new Thread(() -> 
	{
		final List<Boolean> bools = new ArrayList<>(); 
		RenderManagerListener rml = () -> 
		{
			bools.add(true);
		};

		try
		{
			while(bools.isEmpty())
			{
				Thread.sleep(1000);
			}
		}
		catch(InterruptedException e) {}
	});

	public void waitForClose()
	{
		try
		{
			waitingThread.join();
		}
		catch(InterruptedException e) {}
	}

	/* RenderManager Implementation
	-----------------------------------
	| Essentially just passes renderables to the inner thread,
	| and communicates to that thread
	*/

	// a list of renderables that the sub-thread calls to render to the screen
	private List<Renderable> renderables = new ArrayList<>();
	// communication lines for adding and removal of renderables
	private ArrayBlockingQueue<Renderable> toAdd = new ArrayBlockingQueue<>(9999);
	private ArrayBlockingQueue<Renderable> toRemove = new ArrayBlockingQueue<>(9999);
	// an internal flag for efficiencies in checking the comm lines
	private boolean renderablesModifyRequest = false;


	// internal thread
	private Thread thread;
	// paused tells the thread to sleep if set
	private boolean paused = false;
	// fps, if set to a valid, positive number, will tell the internal thread
	// to have fps many frames every second 
	private double fps = -1.0;


	public synchronized void addRenderable(Renderable r)
	{
		toAdd.offer(r);
		renderablesModifyRequest = true;
	}


	public synchronized void removeRenderable(Renderable r)
	{
		toRemove.offer(r);
		renderablesModifyRequest = true;
	}


	public synchronized void start()
	{
		thread = new Thread(this::threadMain);
		thread.setName(INNER_THREAD_NAME);
		thread.start();
	}


	public synchronized void pause()
	{
		paused = true;
	}


	// sets how many frames happen per second
	public void setSpeed(double fps)
	{
		this.fps = fps;
	}


    public void addCloseListener(RenderManagerListener rml)
    {
    	rmListeners.add(rml);
    }


    public void removeCloseListener(RenderManagerListener rml)
    {
    	rmListeners.remove(rml);
    }


	public void shutdown()
	{
		thread.interrupt();
	}


	/* Inner Thread Methods
	------------------------------
	*/

	/*
	Method: threadMain
	 - The starting point for the thread
	 - Essentially loops until interrupted, envoking render()
	   as often as it can, optionally occasionally outputting 
	   the FPS to the screen
	*/
	private void threadMain()
	{
		int renderCount = 0;
		long second = System.currentTimeMillis();

		// temp as fuck - weird race condition or some shit 
		// that's going on
		try
		{
			Thread.sleep(30);
		}
		catch(InterruptedException e) {}

		while(! thread.isInterrupted() )
		{
			if( paused )
			{
				try
				{
					Thread.sleep(1);
				}
				catch(InterruptedException e) {}
			}
			else
			{
				render();
				renderCount++;

				if( System.currentTimeMillis() - second > 1000 )
				{
					System.out.println("FPS: " + renderCount);
					renderCount = 0;
					second = System.currentTimeMillis();
				}
			}
		}
	}


	private AffineTransform worldTransform = new AffineTransform();
	private double scaleXAm = 1.0,
				   scaleYAm = 1.0;

	public void scaleBy(double x, double y)
	{
		worldTransform.translate(screen.getWidth()/2,screen.getHeight()/2);
		worldTransform.scale(x, y);
		worldTransform.translate(-screen.getWidth()/2, -screen.getHeight()/2);

		scaleXAm += x;
		scaleYAm += y;
	}

	public void scaleTo(double xAm, double yAm)
	{
		scaleBy(xAm - scaleXAm, yAm - scaleYAm);
	}


	public Graphics2D getGraphics()
	{
		return graphics;
	}

	private boolean firstRender = true;

	/*
	Method: render
	 - Renders every Renderable in renderables to the screen
	 - There is no ordering, except for the order of adding to
	   the list, so backgrounds should be added first 
	*/
	private void render()
	{
		if( bufferStrategy == null )
      	{
			bufferStrategy = screen.getBufferStrategy();
			if( bufferStrategy == null )
			{
			    screen.createBufferStrategy(3);
			    bufferStrategy = screen.getBufferStrategy();
			}
      	}

      	graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		// ensure we're at the right scale

		if( firstRender )
		{
			firstRender = false;

			double scaleW = (double)screen.getWidth()  / drawResW,
				   scaleH = (double)screen.getHeight() / drawResH;
			// <TEMP>
	        // scaleBy(scaleW, scaleH);
		}

		graphics.setTransform(worldTransform);

		for( Renderable r : renderables )
			r.render(this);

		bufferStrategy.show();
		graphics.dispose();

		if( renderablesModifyRequest )
		{

			Renderable r = toAdd.poll();
			while(r != null )
			{
				renderables.add(r);
				r = toAdd.poll();
			}

			r = toRemove.poll();
			while(r != null )
			{
				renderables.remove(r);
				r = toRemove.poll();
			}


			renderablesModifyRequest = false;
		}




	}

	/* ViewPort Implementation
	-----------------------------
	*/

	private BufferStrategy bufferStrategy;
	private Graphics2D graphics;
	private Color bufferColour = new Color(0, 0, 0, 0);

	private Vec2 worldCenter = new Vec2(0, 0);

	private Vec2 offset = new Vec2(2, 2);
	// the scale of the world, influences how far something is actually
	// placed in the world
	private Vec2 scale = new Vec2(1, 1);

	// slineXs/Ys are used for memory efficiency, so the renderer
	// doesn't have to constantly create/delete memory
	private Object splineMutex = new Object();
	private int[] splineXs = new int[99999];
	private int[] splineYs = new int[99999];


	public void setWorldScale(double x, double y)
	{
		scale.set(x, y);
	}


	public Vec2 getWorldScale()
	{
		return scale;
	}


	public double getOffsetX()
	{
		return worldCenter.getX() * -1 + (screen.getWidth() / offset.getX()) * scale.getX();
	}

	public double getOffsetY()
	{
		return worldCenter.getY() * -1 + (screen.getHeight() / offset.getY()) * scale.getY();
	}


	public void initialise(WindowSettings ws)
	{
		screen = new Screen(ws, this)
		{
			@Override
			public void onClose()
			{
				super.onClose();
				for( RenderManagerListener rml : rmListeners )
				{
					rml.onClose();
				}
			}
		};


		// scaleBy((double)screen.getWidth() / (double)drawResW, (double)screen.getHeight() / (double)drawResH);
	}


	public Sprite createSprite(String fileName)
	{
		AWTSprite sprite = new AWTSprite(fileName);
		return sprite;
	}


	public Sprite createSprite(File file)
	{
		AWTSprite sprite = new AWTSprite(file);
		return sprite;
	}


	public AnimatedSprite createAnimatedSprite(String fileName, double fps)
	{
		AWTAnimSprite animSpr = new AWTAnimSprite(fileName, fps);
		return animSpr;
	}

	public AnimatedSprite createAnimatedSprite(File file, double fps)
	{
		AWTAnimSprite animSpr = new AWTAnimSprite(file, fps);
		return animSpr;
	}


	public int getScreenWidth()
	{
		return screen.getWidth();
	}

	public int getScreenHeight()
	{
		return screen.getHeight();
	}


	public Colour getColour()
	{
		Color c = graphics.getColor();
		return new Colour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}


	public void setColour(Colour c)
	{
		setColour(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}


	public void setColour(int r, int g, int b, int a)
	{
		Color color = new Color(r, g, b, a);
		if( graphics != null )
			graphics.setColor(color);
		else
			throw new IllegalStateException("setColour can only be called during rendering!");
	}


	public void setFont(String s)
	{
		graphics.setFont(new Font(s, Font.PLAIN, graphics.getFont().getSize()) );
	}


	public void setFont(Font f)
	{
		if( f != null )
			graphics.setFont(f);
	}


	public Font getFont()
	{
		return graphics.getFont();
	}



	public void setWorldCenter(Vec2 v)
	{
		worldCenter.set(v);
	}

	public void setWorldCenter(double x, double y)
	{
		worldCenter.set(x, y);
	}



	public Vec2 getWorldCenter()
	{
		return new Vec2(getOffsetX(), getOffsetY());
	}


	/* CONVENTIONS:
	 * 	- draw means fill the entire object with colour
	 *  - outline is the border of that object
	 *  - anything that uses a Vec2 uses it's double component,
	 *    so theoretically it's more efficient to use doubles if
	 *    you can, but it's not the end of the world
	 */



	public void setPixel(int x, int y, int r, int g, int b, int a)
	{
		Colour c = getColour();
		setColour(r, g, b, a);
		graphics.drawRect(x, y, 1, 1);
		setColour(c);
	}


	public void drawRect(double px, double py, int w, int h)
	{
		graphics.fillRect((int)(px + getOffsetX()), (int)(py + getOffsetY()), w, h);
	}


	public void drawRect(double px1, double py1, double px2, double py2)
	{
		double smallx, bigx, smally, bigy;

		smallx = px1 < px2 ? px1 : px2;
		if( px1 < px2 )
		{   smallx = px1; bigx = px2;   }
		else
		{   smallx = px2; bigx = px1;   }

		if( py1 < py2 )
		{   smally = py1; bigy = py2;   }
		else
		{   smally = py2; bigy = py1;   }


		graphics.fillRect((int)(smallx + getOffsetX()), (int)(smally + getOffsetY()), (int)(bigx + getOffsetX()), (int)(bigy + getOffsetY()));
	}


	public void drawRect(Vec2 p1, int w, int h)
	{
		drawRect(p1.getX(), p1.getY(), w, h);
	}


	public void drawRect(Vec2 p1, Vec2 p2)
	{
		drawRect(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}


	public void outlineRect(double px, double py, int w, int h)
	{
		graphics.drawRect((int)(px + getOffsetX()), (int)(py + getOffsetY()), w, h);
	}


	public void outlineRect(double px1, double py1, double px2, double py2)
	{
		double smallx, bigx, smally, bigy;

		smallx = px1 < px2 ? px1 : px2;
		if( px1 < px2 )
		{   smallx = px1; bigx = px2;   }
		else
		{   smallx = px2; bigx = px1;   }

		if( py1 < py2 )
		{   smally = py1; bigy = py2;   }
		else
		{   smally = py2; bigy = py1;   }

		graphics.drawRect((int)(smallx + getOffsetX()), (int)(smally + getOffsetY()), (int)(bigx + getOffsetX()), (int)(bigy + getOffsetY()));
	}


	public void outlineRect(Vec2 p1, int w, int h)
	{
		outlineRect(p1.getX(), p1.getY(), w, h);
	}


	public void outlineRect(Vec2 p1, Vec2 p2)
	{
		outlineRect(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}


	public void drawSpline(List<Vec2> positions)
	{
		drawSpline(positions, 0, 0);
	}


	public void drawSpline(List<Vec2> positions, Vec2 thisOff)
	{
		drawSpline(positions, thisOff.getX(), thisOff.getY());
	}


	public void drawSpline(List<Vec2> positions, double offX, double offY)
	{
		int index = 0;
		synchronized(splineMutex)
		{
			for( Vec2 v : positions )
			{
				splineXs[index] = (int)(v.getX() + getOffsetX() + offX);
				splineYs[index] = (int)(v.getY() + getOffsetY() + offY);
				index++;
			}

			graphics.drawPolyline(splineXs, splineYs, index);
		}
	}


	public void drawSpline(int[] xs, int[] ys, int count)
	{
		double _offX = getOffsetX();
		double _offY = getOffsetY();
		for( int ii = 0; ii < count; ii++ )
		{
			xs[ii] += (int)_offX;
			ys[ii] += (int)_offY;
		}


		graphics.drawPolyline(xs, ys, count);

		for( int ii = 0; ii < count; ii++ )
		{
			xs[ii] -= (int)_offX;
			ys[ii] -= (int)_offY;
		}
	}



	public void drawColouredSpline(Map<Integer, Colour> colours, List<Vec2> positions)
	{
		Iterator<Vec2> iter = positions.iterator();
		Colour origC = getColour();


		int count = 0, index = 0;
		for( index = 0; index < positions.size(); index++ )
		{
			Vec2 thisVec = positions.get(index);

			Colour newColour = colours.get(index);

			if( newColour != null )
			{
				setColour(newColour);

				if( count > 0 )
				{
					graphics.drawPolyline(splineXs, splineYs, count);
					count = 0;
				}
			}

			splineXs[count] = (int)(thisVec.getX() + getOffsetX());
			splineYs[count] = (int)(thisVec.getY() + getOffsetY());
			count++;
		}

		graphics.drawPolyline(splineXs, splineYs, count);

		setColour(origC);
	}



	public void drawPoly(List<Vec2> positions)
	{
		int index = 0;
		synchronized(splineMutex)
		{
			for( Vec2 v : positions )
			{
				splineXs[index] = (int)(v.getX() + getOffsetX());
				splineYs[index] = (int)(v.getY() + getOffsetY());
				index++;
			}

			graphics.drawPolygon(splineXs, splineYs, index);
		}
	}


	public void drawPoly(int[] xs, int[] ys, int count)
	{
		for( int ii = 0; ii < count; ii++ )
		{
			xs[ii] += getOffsetX();
			ys[ii] += getOffsetY();
		}

		graphics.drawPolygon(xs, ys, count);
	}


	public void drawCircle(double x, double y, double r)
	{
		drawOval(x, y, r * 2, r * 2);
	}


	public void drawCircle(Vec2 pos, double r)
	{
		drawCircle(pos.getX(), pos.getY(), r);
	}


	public void outlineCircle(double x, double y, double r)
	{	
		outlineOval(x - r / 2, y - r / 2, r * 2, r * 2);
	}


	public void outlineCircle(Vec2 pos, double r)
	{
		outlineCircle(pos.getX(), pos.getY(), r);
	}


	public void drawOval(double x, double y, double r1, double r2)
	{
		graphics.fillOval((int)(x + getOffsetX()), (int)(y + getOffsetY()), (int)r1, (int)r2);
	}


	public void drawOval(Vec2 pos, double r1, double r2)
	{
		drawOval(pos.getX(), pos.getY(), r1, r2);
	}


	public void outlineOval(double x, double y, double r1, double r2)
	{
		graphics.drawOval((int)(x + getOffsetX()), (int)(y + getOffsetY()), (int)r1, (int)r2);

	}


	public void outlineOval(Vec2 pos, double r1, double r2)
	{
		outlineOval(pos.getX(), pos.getY(), r1, r2);
	}


	public void drawLine(Vec2 start, Vec2 end)
	{
		drawLine(start.getX(), start.getY(), end.getX(), end.getY());
	}


	public void drawLine(double startX, double startY, double endX, double endY)
	{
		graphics.drawLine((int)(startX + getOffsetX()), (int)(startY + getOffsetY()), (int)(endX + getOffsetX()), (int)(endY + getOffsetY()));
	}


	public void drawLine(Vec2 start, double endX, double endY)
	{
		drawLine(start.getX(), start.getY(), endX, endY);
	}

	public void drawLine(double startX, double startY, Vec2 end)
	{
		drawLine(startX, startY, end.getX(), end.getY());
	}


	public void drawAbsText(String text, double x, double y)
	{
		graphics.drawString(text, (int)(x + getOffsetX()), (int)(y + getOffsetY()) );
	}


	public void drawText(String text, double cX, double cY)
	{
	    // Get the FontMetrics
	    FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
	    // Determine the X coordinate for the text

	    int xPos = (int)(cX - metrics.stringWidth(text) / 2 + getOffsetX());
	    int yPos = (int)(cY - metrics.getHeight() / 2 + getOffsetY());


	    graphics.drawString(text, xPos, yPos);
	}


	public void drawText(String text, Vec2 cPos)
	{
		drawText(text, cPos.getX(), cPos.getY());
	}

	private AWTSprite bufferSprite = new AWTSprite();

	public void drawSprite(Sprite s, double cX, double cY)
	{
		int xPos = (int)(cX -  s.getWidth() / 2),
			yPos = (int)(cY - s.getHeight() / 2);

		synchronized(bufferSprite)
		{
			bufferSprite.parseFromSprite(s);
			graphics.drawImage(bufferSprite.getImage(), (int)(xPos + getOffsetX()), (int)(yPos + getOffsetY()), null);
		}
	}

	public void drawSprite(Sprite s, Vec2 cPos)
	{
		drawSprite(s, cPos.getX(), cPos.getY());
	}


	public void drawImage(RenderedImage ir, AffineTransform at)
	{
		drawImage(ir, at, 0, 0);
	}


	public void drawImage(RenderedImage ir, AffineTransform at, double x, double y)
	{
		if( graphics instanceof Graphics2D )
		{
			AffineTransform newT = new AffineTransform();
			newT.translate(getOffsetX() + (int)x, getOffsetY() + (int)y);

			newT.concatenate(at);

			Graphics2D g2d = (Graphics2D)graphics;
			g2d.drawRenderedImage(ir, newT);
		}	
	}






	/* Overlayer Specific Methods
	--------------------------------
	*/

	public void overlayRect(double x, double y, int w, int h)
	{
		// System.out.println("overlay rect " + x + ", " + y + ", " + w + ", " + h);

		graphics.drawRect((int)x, (int)y, w, h);
	}

	public void overlayRect(double px1, double py1, double px2, double py2)
	{
		double smallx, bigx, smally, bigy;

		if( px1 < px2 )
		{   smallx = px1; bigx = px2;   }
		else
		{   smallx = px2; bigx = px1;   }

		if( py1 < py2 )
		{   smally = py1; bigy = py2;   }
		else
		{   smally = py2; bigy = py1;   }

		overlayRect(smallx, smally, (int)bigx, (int)bigy);
	}

	public void overlayFilledRect(double x, double y, int w, int h)
	{
		graphics.fillRect((int)x, (int)y, w, h);
	}

	public void overlayFilledRect(double px1, double py1, double px2, double py2)
	{
		double smallx, bigx, smally, bigy;

		if( px1 < px2 )
		{   smallx = px1; bigx = px2;   }
		else
		{   smallx = px2; bigx = px1;   }

		if( py1 < py2 )
		{   smally = py1; bigy = py2;   }
		else
		{   smally = py2; bigy = py1;   }


		overlayFilledRect(smallx, smally, (int)bigx, (int)bigy);
	}


	public void overlaySpline(List<Vec2> positions)
	{
		overlaySpline(positions, null);
	}

	public void overlaySpline(List<Vec2> positions, Vec2 off)
	{
		synchronized(splineMutex)
		{
			int ii = 0;
			for( Vec2 v : positions)
			{
				splineXs[ii] = (int)(v.getX() + (off != null ? off.getX() : 0.0)); 
				splineYs[ii] = (int)(v.getY() + (off != null ? off.getY() : 0.0)); 

				ii++;
			}

			overlaySpline(splineXs, splineYs, positions.size());
		}
	}


	public void overlaySpline(int[] xs, int[] ys, int count)
	{
		graphics.drawPolyline(xs, ys, count);
	}


	public void overlayPoly(List<Vec2> positions)
	{
		synchronized(splineMutex)
		{
			int ii = 0;
			for( Vec2 v : positions)
			{
				splineXs[ii] = (int)v.getX();
				splineYs[ii] = (int)v.getY();

				ii++;
			}

			overlayPoly(splineYs, splineYs, positions.size());
		}
	}

	public void overlayPoly(int[] xs, int[] ys, int count)
	{
		graphics.drawPolygon(xs, ys, count);
	}


	public void overlayCircle(double x, double y, double r)
	{
		graphics.drawOval((int)x, (int)y, (int)(r * 2), (int)(r * 2));
	}

	public void overlayCircle(Vec2 v, double r)
	{
		overlayCircle(v.getX(), v.getY(), r);
	}


	public void overlayOval(double x, double y, double r1, double r2)
	{
		graphics.drawOval((int)x, (int)y,(int)(r1 * 2),(int)(r2 * 2));
	}

	public void overlayOval(Vec2 v, double r1, double r2)
	{
		overlayOval(v.getX(), v.getY(), r1, r2);
	}


	public void overlayLine(double x1, double y1, double x2, double y2)
	{
		graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}

	public void overlayLine(Vec2 s, double ex, double ey)
	{
		overlayLine(s.getX(), s.getY(), ex, ey);
	}

	public void overlayLine(Vec2 s, Vec2 e)
	{
		overlayLine(s.getX(), s.getY(), e.getX(), e.getY());
	}


	// public void drawText(String text, double cX, double cY)
	// {
	//     // Get the FontMetrics
	//     FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
	//     // Determine the X coordinate for the text

	//     int xPos = (int)(cX - metrics.stringWidth(text) / 2 + getOffsetX());
	//     int yPos = (int)(cY - metrics.getHeight() / 2 + getOffsetY());

	//     // System.out.println(graphics.getFont());

	//     graphics.drawString(text, xPos, yPos);
	// }


	// public void drawText(String text, Vec2 cPos)
	// {
	// 	drawText(text, cPos.getX(), cPos.getY());
	// }


	public void overlayText(String text, double cX, double cY)
	{
	    // Get the FontMetrics
	    FontMetrics metrics = graphics.getFontMetrics(graphics.getFont());
	    // Determine the X coordinate for the text

	    int xPos = (int)(cX - metrics.stringWidth(text) / 2);
	    int yPos = (int)(cY - metrics.getHeight() / 2);

	    graphics.drawString(text, xPos, yPos);
	}

	public void overlayText(String text, Vec2 v)
	{
		overlayText(text, v.getX(), v.getY());
	}


	public void overlayAbsText(String text, double x, double y )
	{
		graphics.drawString(text, (int)x, (int)y);
	}




	public void overlaySprite(Sprite s, double x, double y)
	{
		int xPos = (int)(x -  s.getWidth() / 2),
			yPos = (int)(y - s.getHeight() / 2);

		synchronized(bufferSprite)
		{
			bufferSprite.parseFromSprite(s);
			graphics.drawImage(bufferSprite.getImage(), (int)(xPos), (int)(yPos), null);
		}
	}

	public void overlaySprite(Sprite s, Vec2 v)
	{

	}











	/* Private Classes
	-----------------------------
	*/


	/*
	Class: ActionController
	 - The AWTListener essentially, converts awt actions
	   into game actions through the KeyBinds class,
	   and sends it to the listeners
	*/
	private class ActionController implements MouseController
	{
		private List<ActionListener> listeners = new ArrayList<>();

		private KeyboardListener kl;
		private MouseListener ml;


		public ActionController(Canvas cv)
		{
			kl = new KeyboardListener(this);
			ml = new MouseListener(this);

			cv.addKeyListener(kl);
			cv.addMouseListener(ml);
		}


		public void actionHappened(String action)
		{
			// System.out.println("Action " + action + " happened! sending to " + listeners.size() + " listeners");
			for( ActionListener al : listeners )
			{
				al.actionStart(AWTRenderer.this, action);
			}
		}

		public void actionStopped(String action)
		{
			// System.out.println("Action " + action + " finished! sending to " + listeners.size() + " listeners");
			for( ActionListener al : listeners )
				al.actionEnd(AWTRenderer.this, action);
		}


		public void addListener(ActionListener al)
		{
			System.out.println("adding listener " + al);
			listeners.add(al);
		}


		public void removeListener(ActionListener al)
		{
			listeners.remove(al);
		}


		public String classifyController()
		{
			return kl.classifyController() + ml.classifyController();
		}

		// MouseController method!
		public Vec2 getMousePos()
		{
			return ml.getMousePos();
		}

	}


	/*
	Private Class: KeyboardListener
	 - The AWT KeyAdapter, sends Actions (or keystrokes if no action) to the ActionController
	*/
	private class KeyboardListener extends KeyAdapter implements Controller
	{
		private ActionController al;

		private Set<String> keysHeld = new HashSet<>();

		public KeyboardListener(ActionController al)
		{
			this.al = al;
		}

		public void initialise()
		{
		}


		public void keyPressed(KeyEvent e)
		{

			int key = e.getKeyCode();

			String action = KeyBinds.getAction(key);
			if( action != null )
			{
				if(! keysHeld.contains(action) )
				{
					al.actionHappened(action);
					keysHeld.add(action);
				}
			}


		}


		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();

			String action = KeyBinds.getAction(key);
			if( action != null )
			{
				al.actionStopped(action);
				keysHeld.remove(action);
			}
		}


		public String classifyController()
		{
			return StaticInfo.Control.ControllerTypes.KEYBOARD;
		}


		public void addListener(ControlListener cl)
		{
			throw new IllegalStateException("Not valid, KeyboardListener is not a stand-alone");
		}


		public void removeListener(ControlListener cl)
		{
			throw new IllegalStateException("Not valid, KeyboardListener is not a stand-alone");
		}
	}


	/* 
	Private Class: MouseListener
	 - Listens for mouse buttons and is able to get the mouse position when asked
	*/
	private class MouseListener extends MouseAdapter implements Controller, MouseController
	{
		private ActionController al;

		public MouseListener(ActionController al)
		{
			this.al = al;
		}

		public void initialise()
		{
		}

		public void mousePressed(MouseEvent e)
		{
			int button = e.getButton();
			String action = KeyBinds.getMouseAction(button);

			if( action != null)
		    	al.actionHappened(action);
		}


		public void mouseReleased(MouseEvent e)
		{
			int button = e.getButton();
			String action = KeyBinds.getMouseAction(button);

			if( action != null)
				al.actionStopped(action);
		}


		public Vec2 getMousePos()
		{
			Point rawPoint = MouseInfo.getPointerInfo().getLocation();

			// convert rawPoint to gamePoint
			double gameX = rawPoint.getX(),
				   gameY = rawPoint.getY();

			// rawPoint is the offset from the top left corner of the window to
			// where the mouse is
			// if the game is scaled by (x, y), then the game coordinate of the mouse
			// would be rawPoint *  
			Vec2 offset = screen.getMonitorPosition();


			gameX -= offset.getX() + screen.getMouseOffset().getX();
			gameY -= offset.getY() + screen.getMouseOffset().getY();


			gameX -= screen.getWidth() / 2;
			gameY -= screen.getHeight() / 2;
			gameX *= 1 / worldTransform.getScaleX();
			gameY *= 1 / worldTransform.getScaleY();
			gameX += screen.getWidth() / 2;
			gameY += screen.getHeight() / 2;
			


			return new Vec2(gameX, gameY);
		}


		public String classifyController()
		{
			return StaticInfo.Control.ControllerTypes.KEYBOARD;
		}


		public void addListener(ControlListener cl)
		{
			throw new IllegalStateException("Not valid, MouseListener is not a stand-alone");
		}


		public void removeListener(ControlListener cl)
		{
			throw new IllegalStateException("Not valid, MouseListener is not a stand-alone");
		}
	}
}