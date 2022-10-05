package dev.ramar.e2.awt.drawing.polyshape;


import dev.ramar.e2.core.drawing.polyshape.*;
/* PolyshapeMods, PolyshapeDrawer */

import dev.ramar.e2.core.structures.Vec2;
import dev.ramar.e2.core.structures.Colour;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;

import java.util.Arrays;

public class AWTPolyshapeDrawer extends PolyshapeDrawer
{
	public static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);


	private Graphics2D graphics = null;

	public void setGraphics(Graphics2D g2d)
	{
		this.graphics = g2d;
	}

    public void positions(double... poses)
    {
    	PolyshapeMods mods = this.getMod();
    	int xs[] = new int[(int)(poses.length / 2)];
    	int ys[] = new int[(int)(poses.length / 2)];


    	boolean flip = true;
    	int dex = 0;
    	for( double d : poses )
    	{
    		if( flip )
    			xs[dex] = (int)Math.round(d);
    		else
    		{
    			ys[dex] = (int)Math.round(d);
    			dex++;
    		}
    		flip = !flip;
    	}

    	this.positions(xs, ys);
    }
    public void offsets(double... offsets)
    {
    	PolyshapeMods mods = this.getMod();
    	int xs[] = new int[(int)(offsets.length / 2)];
    	int ys[] = new int[(int)(offsets.length / 2)];


    	boolean flip = true;
    	int dex = 0;
    	double xbuf = 0.0,
    		   ybuf = 0.0;
    	for( double d : offsets )
    	{
    		if( flip )
    		{
    			xbuf += d;
    			xs[dex] = (int)Math.round(xbuf);
    		}
    		else
    		{
    			ybuf += d;
    			ys[dex] = (int)Math.round(ybuf);
    			dex++;
    		}
    		flip = !flip;
    	}

    	this.positions(xs, ys);
    }


    public void positions(int[] xs, int[] ys)
    {
        Graphics2D g2d = this.graphics;
        if( g2d == null || xs == null || ys == null )
            return;
		double ox = 0.0,
			   oy = 0.0;

    	Colour colour = DEFAULT_COLOUR;
    	double width = 1.0;
    	boolean loops = false,
    			fill = false;

    	PolyshapeMods mods = this.getMod();
		if( mods != null )
		{
			ox += mods.offset.getX();
			oy += mods.offset.getY();

			colour = mods.colour.get();
			width = mods.width.get();
			loops = mods.loops.get();
			fill = mods.fill.get();
		}


		AffineTransform at = this.graphics.getTransform();
		AffineTransform oldTransform = new AffineTransform(at);

		at.translate(ox, oy);

		Color oldColor = this.graphics.getColor();
		this.graphics.setColor(colour.convertToColor());

		Stroke oldStroke = this.graphics.getStroke();
		this.graphics.setStroke(new BasicStroke((float)width));

		this.graphics.setTransform(at);

		// this.graphics.drawPolygon(xs, ys, Math.min(xs.length, ys.length));


		if( loops )
		{
			if( fill )
				this.graphics.fillPolygon(xs, ys, Math.min(xs.length, ys.length));
			this.graphics.drawPolygon(xs, ys, Math.min(xs.length, ys.length));
		}
		else
			this.graphics.drawPolyline(xs, ys, Math.min(xs.length, ys.length));

		// if( fill )
		// 	this.graphics.fillPolygon(xs, ys, Math.min(xs.length, ys.length));


		this.graphics.setTransform(oldTransform);
		this.graphics.setColor(oldColor);
		this.graphics.setStroke(oldStroke);
    }
}
