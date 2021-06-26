package dev.ramar.e2.structures.renderables.entities;


import dev.ramar.e2.backend.world_components.*;

import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.interfaces.rendering.Layerable;
import dev.ramar.e2.interfaces.events.listeners.ActionListener;
import dev.ramar.e2.interfaces.events.listeners.EventListener;
import dev.ramar.e2.interfaces.events.producers.PositionMover;

import dev.ramar.e2.interfaces.events.producers.Controller;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.utils.Timer;

import java.util.*;


/*
Class: WorldEntity
 - Represents an Entity which can be placed in a World
 - This Entity can only exist in a spot, and has some
   capability to be moved, using <vel>
*/
public class WorldEntity implements Layerable, ActionListener, EventListener
{
	protected Vec2 pos;
	protected Vec2 vel;

	/*
	Method: initialise()
	 - An initialise method so anonymous classes have 
	   a method to override, BUT they MUST call the 
	   super initialise !!
	*/
	protected void initialise()
	{
	}

	protected WorldEntity(Vec2 p, Vec2 v)
	{
		uuid = StaticInfo.UUID.createNewUUID();
		this.pos = p;
		this.vel = v;

	}


	public WorldEntity(double x, double y, double xv, double yv)
	{
		uuid = StaticInfo.UUID.createNewUUID();
		this.pos = new Vec2(x, y);
		this.vel = new Vec2(xv, yv);

		initialise();
	}


	protected WorldEntity(WorldEntity we)
	{
		uuid = StaticInfo.UUID.createNewUUID();

		if( we.pos != null )
			this.pos = we.pos.clone();

		if( we.vel != null )
			this.vel = we.vel.clone();

		if( we.ownedChunk != null)
			this.ownedChunk = we.ownedChunk;

		if( we.ownedLayer != null)
			this.ownedLayer = we.ownedLayer;
	}

	public WorldEntity clone()
	{
		WorldEntity we = new WorldEntity(this);
		we.initialise();
		return we;
	}


	public String toString()
	{
		return "{" + StaticInfo.Functions.getClassName(this) + ": " + uuid + "}";
	}


	/* WorldEntity implementation
	---------------------------------
	*/
	protected void consoleOutput(String msg)
	{
		System.out.println("[" + uuid + "] " + msg);
	}


	protected void moveUP() 
	{
		// consoleOutput("move up!");
	}


	protected void moveDOWN()
	{
		// consoleOutput("move down!");
	}


	protected void moveRIGHT()
	{
		// consoleOutput("move right!");
	}


	protected void moveLEFT()
	{
		// consoleOutput("move left!");
	}


	protected void leftClick() 
	{
		// consoleOutput("left click!");
	}


	protected void rightClick() 
	{
		// consoleOutput("right click!");
	}


	public void doControl()
	{
		synchronized(actions)
		{
			for( String s : actions )
				onAction(s);
		}
	}


	protected void onAction(String action)
	{
		switch(action)
		{
			case StaticInfo.Control.Actions.MOVE_UP:
				moveUP();
				break;

			case StaticInfo.Control.Actions.MOVE_DOWN:
				moveDOWN();
				break;

			case StaticInfo.Control.Actions.MOVE_LEFT:
				moveLEFT();
				break;

			case StaticInfo.Control.Actions.MOVE_RIGHT:
				moveRIGHT();
				break;

			case StaticInfo.Control.Actions.LEFT_CLICK:
				leftClick();
				break;

			case StaticInfo.Control.Actions.RIGHT_CLICK:
				rightClick();
				break;
		}
	}


	/*
	Methods: WorldEntity Accessors
	*/

	public double getXPos()
	{
		return pos.getX();
	}


	public double getYPos()
	{
		return pos.getY();
	}

	// Layerable method
	public Vec2 getPos()
	{
		return pos;
	}


	public double getXVel()
	{
		return vel.getX();
	}


	public double getYVel()
	{
		return vel.getY();
	}


	public Vec2 getVel()
	{
		return vel;
	}


	/*
	Methods: WorldEntity Mutators
	*/

	private static final int ROUND_TO = 8;
	private static final double THRESHOLD = 0.0;
	private final Vec2 lastMove = new Vec2(-9000, -9000);


	private void testMoved()
	{
		// do x
		if( Math.abs(StaticInfo.Functions.roundDouble(pos.getX() - lastMove.getX(), ROUND_TO)) > THRESHOLD )
		{
			onMove();
			lastMove.set(pos);
		}	
		else if(  Math.abs(StaticInfo.Functions.roundDouble(pos.getY() - lastMove.getY(), ROUND_TO)) > THRESHOLD )
		{
			onMove();
			lastMove.set(pos);
		}

	}

	public void setXPos(double x)
	{
		pos.setX(x);
		testMoved();
	}


	public void setYPos(double y)
	{
		pos.setY(y);
		testMoved();
	}


	public void setPos(double x, double y)
	{
		double ourRoundX = StaticInfo.Functions.roundDouble(this.pos.getX(), ROUND_TO),
		       ourRoundY = StaticInfo.Functions.roundDouble(this.pos.getY(), ROUND_TO);

		double theirRoundX = StaticInfo.Functions.roundDouble(x, ROUND_TO),
			   theirRoundY = StaticInfo.Functions.roundDouble(y, ROUND_TO);

		this.pos.set(x, y);
		testMoved();
	}


	public void setPos(Vec2 pos)
	{
		setPos(pos.getX(), pos.getY());
	}


	public void setXVel(double x)
	{
		vel.setX(x);
	}


	public void setYVel(double y)
	{
		vel.setY(y);
	}


	public void setVel(Vec2 vel)
	{
		this.vel.set(vel);
	}


	/* EventListener Implementation
	----------------------------------
	*/


    public void event(String eventName)
    {

    }


	public void timerComplete(String name)
	{
		event(name);
	}


	public void wait(long time, String name)
	{
		Timer.wait(time, name, this);
	}


	/* Layerable implementation
	------------------------------
	*/

	private String uuid;
	private Chunk ownedChunk;
	private ChunkLayer ownedLayer;

	public String getUUID()
	{
		return uuid;
	}


	public void setChunk(Chunk c)
	{
		ownedChunk = c;
	}


	public Chunk getChunk()
	{
		return ownedChunk;
	}

	
	public void clearChunk()
	{
		ownedChunk = null;
	}


	public void setLayer(ChunkLayer cl)
	{
		ownedLayer = cl;
	}


	public ChunkLayer getLayer()
	{
		return ownedLayer;
	}


	public void clearLayer()
	{
		ownedLayer = null;
	}



	/* PositionMover implementation 
	----------------------------------
	   (SUB Layerable)
	*/

	public void onMove()
	{
		if( ownedChunk != null )
			ownedChunk.moved(this, pos);
	}



	/* Renderable Implementation
	-------------------------------
	   (SUB Layerable)
	*/

	protected Colour mainColour = new Colour(255, 0, 0, 255);
	protected Colour secondaryColour = new Colour(0, 255, 255, 255);

	protected int size = 10;


	public void setMainColour(int r, int g, int b, int a)
	{
		mainColour.set(r, g, b, a);
	}


	public void setSecondaryColour(int r, int g, int b, int a)
	{
		secondaryColour.set(r, g, b, a);
	}


	public Colour getMainColour()
	{
		return mainColour;
	}


	public Colour getSecondaryColour()
	{
		return secondaryColour;
	}


	public void render(ViewPort vp)
	{
		Colour originalColour = vp.getColour();

		vp.setColour(mainColour);
		vp.drawRect(pos.getX() - size/2, pos.getY() - size/2, size, size);

		if( ownedChunk != null )
		{
			vp.setColour(secondaryColour);
			// vp.drawLine (pos.getX(), pos.getY(), ownedChunk.getWorldX(), ownedChunk.getWorldY());
		}

		vp.setColour(originalColour);
	}

	public void drawSelf(Vec2 p, ViewPort vp)
	{
		double x = getXPos(),
			   y = getYPos();

		if( p != null )
		{
			x += p.getX();
			y += p.getY();
		}

		Colour originalColour = vp.getColour();

		vp.setColour(mainColour);
		vp.drawRect(x - size/2, y - size/2, size, size);

		if( ownedChunk != null )
		{
			vp.setColour(secondaryColour);
			// vp.drawLine (pos.getX(), pos.getY(), ownedChunk.getWorldX(), ownedChunk.getWorldY());
		}

		vp.setColour(originalColour);
	}


	/* ActionListener implementation
	----------------------------------
	*/
	protected Set<String> actions = java.util.Collections.synchronizedSet(new HashSet<String>());

	public void doKeyListening()
	{
		StaticInfo.Objects.getMainController().addListener(this);
	}

	public void stopKeyListening()
	{
		StaticInfo.Objects.getMainController().removeListener(this);
	}


	public void doKeyListening(Controller c)
	{
		c.addListener(this);
	}

	public void stopKeyListening(Controller c)
	{
		c.removeListener(this);
	}




	public void actionStart(Controller c, String action)
	{
		actions.add(action);
	}


	public void actionEnd(Controller c, String action)
	{
		actions.remove(action);
	}


	public void controlCallback(Controller c, String action)
	{

	}

}