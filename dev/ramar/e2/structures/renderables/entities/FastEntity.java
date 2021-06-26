package dev.ramar.e2.structures.renderables.entities;


import dev.ramar.e2.interfaces.updating.FastUpdater;
import dev.ramar.e2.interfaces.updating.FUpdateManager;

import dev.ramar.e2.structures.StaticInfo;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.structures.Colour;
/*
Class: FastEntity
 - A WorldEntity which updates by delta time
*/
public class FastEntity extends WorldEntity implements FastUpdater
{
	protected Vec2 drag = new Vec2(1.0);
	protected Vec2 maxSpeed = new Vec2(1000);


	@Override
	protected void initialise()
	{
		super.initialise();
		// maxDrag = new Vec2(0.99);
		// drag = new Vec2(maxDrag);
		// maxSpeed = new Vec2(1000);
	}

	public FastEntity(double x, double y, double xv, double yv)
	{
		super(x, y, xv, yv);
		initialise();
	}


	public FastEntity(Vec2 pos, Vec2 vel)
	{
		super(pos, vel);
		initialise();
	}


	public FastEntity(FastEntity cl)
	{
		super(cl);
		drag.set(cl.drag);
	}


	public Vec2 getDrag()
	{
		return drag;
	}

	/* FastUpdater implementation
	--------------------------------
	*/

	protected boolean doKill = false;

	protected double deltaTest = 0.0;
	protected long startTime = 0;

	public void startUpdating()
	{
		StaticInfo.Objects.getFUpdateManager().addFUpdater(this);

	}

	private boolean firstUpdate = true;

	public void update(double delta)
	{
		if( firstUpdate )
		{
			firstUpdate = false;
			startTime = System.currentTimeMillis();
		}
		doControl(delta);

		doMovement(delta);
	}

	protected void doControl(double delta)
	{
		synchronized(actions)
		{
			for( String s : actions )
				onAction(s, delta);
		}
	}



	protected void onAction(String action, double delta)
	{
		switch(action)
		{
			case StaticInfo.Control.Actions.MOVE_UP:
				moveUP(delta);
				break;

			case StaticInfo.Control.Actions.MOVE_DOWN:
				moveDOWN(delta);
				break;

			case StaticInfo.Control.Actions.MOVE_LEFT:
				moveLEFT(delta);
				break;

			case StaticInfo.Control.Actions.MOVE_RIGHT:
				moveRIGHT(delta);
				break;

			case StaticInfo.Control.Actions.LEFT_CLICK:
				leftClick(delta);
				break;

			case StaticInfo.Control.Actions.RIGHT_CLICK:
				rightClick(delta);
				break;
		}
	}



	protected void moveUP(double delta)
	{

	}


	protected void moveDOWN(double delta)
	{
		
	}

	protected void moveLEFT(double delta)
	{
		
	}

	protected void moveRIGHT(double delta)
	{
		
	}


	protected void leftClick(double delta)
	{
		
	}

	protected void rightClick(double delta)
	{
		
	}


	private double xInput, yInput;


	/*
	Method: doMovement
	 - Calculates how much to move dependent on how much time (delta)
	   has passed
	 - delta is a double where 1.0 represents 1 second, vel, maxSpeed,
	   drag, etc. all define how much movement in one second. This means
	   that we can use delta like a percentage; doMovement(0.5) == half of
	   a second of movement, means 50% of all vel/maxSpeed/etc.
	*/
	protected void doMovement(double delta)
	{
		// ensure we're not going faster than maxSpeed

		double xMove = vel.getX() < 0 ? Math.max(vel.getX(), -maxSpeed.getX()) :
								 		Math.min(vel.getX(),  maxSpeed.getX());			   

		double yMove = vel.getY() < 0 ? Math.max(vel.getY(), -maxSpeed.getY()) :
								 		Math.min(vel.getY(),  maxSpeed.getY());


        // The distance the player is moving is defined by the hypotenuse of the
        // two lengths vel.x and vel.y. modify xMove and yMove

        double hyp = Math.sqrt(xMove * xMove + yMove * yMove);

        if( xMove != 0 )
        {
	        double xTheta = Math.cos(xMove / hyp);
        	double newCalc = Math.cos(xTheta) * xMove;

        	xInput = newCalc - xMove;

			xMove = newCalc;
        }
        if( yMove != 0 )
        {
    	    double yTheta = Math.cos(yMove / hyp);
        	double newCalc = Math.cos(yTheta) * yMove;

        	yInput = newCalc - yMove;

        	yMove = newCalc;
        }


		// modify xMove / yMove by how long it's been since the last update

		double deltaXMove = xMove * delta,
			   deltaYMove = yMove * delta;

		// calculate how drag will effect us

		deltaXMove *= drag.getX() / (1 + delta);
		deltaYMove *= drag.getY() / (1 + delta);

		// remove the distance we're travelling from vel and apply it to pos

		// consoleOutput("Moving (" + deltaXMove + ", " + deltaYMove + ") from " + vel);

		// lower our velocity by how much we're moving

		setXVel(getXVel() - deltaXMove);
		setYVel(getYVel() - deltaYMove);

		setYVel(getYVel() / (1 + delta));
		setXVel(getXVel() / (1 + delta));

		// increase our position by how much we're moving
		setXPos(getXPos() + deltaXMove);
		setYPos(getYPos() + deltaYMove);

	}


	public boolean kill()
	{
		return doKill;
	}


	public void onKill()
	{
		if( getChunk() != null )
			getChunk().removeLayerable(this);

		StaticInfo.Objects.getFUpdateManager().removeFUpdater(this);


	}		


	@Override
	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
	}


	@Override
	public void drawSelf(Vec2 v, ViewPort vp)
	{
		super.drawSelf(v, vp);
		double x = getXPos(),
			   y = getYPos();

		Colour c = vp.getColour();
		if( v != null )
		{
			x += v.getX();
			y += v.getY();
		}

		// vp.setColour(255, 255, 255, 255);
		// vp.drawText("inputs: " + StaticInfo.Functions.roundDouble(xInput, 2) + ", " + StaticInfo.Functions.roundDouble(yInput, 2), x, y);

		vp.setColour(c);
	}
}