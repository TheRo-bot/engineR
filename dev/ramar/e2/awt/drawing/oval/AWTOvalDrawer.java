package dev.ramar.e2.awt.drawing.oval;


import dev.ramar.e2.core.drawing.oval.OvalMods;
import dev.ramar.e2.core.drawing.oval.OvalDrawer;

import dev.ramar.e2.core.structures.Colour;

import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.geom.AffineTransform;

public class AWTOvalDrawer extends OvalDrawer
{
	public class Defaults
	{
		public static final Colour COLOUR = new Colour(255, 255, 255, 255); 
	}


	private Graphics2D graphics = null;
	public void setGraphics(Graphics2D g2d)
	{
		this.graphics = g2d;
	}


	public void poslen(double x, double y, double w, double h)
	{
		Graphics2D g2d = this.graphics;
        if( g2d == null )
            return;

        boolean fill = false;
        Colour colour = Defaults.COLOUR;
        double rotation = 0.0;

        OvalMods mods = this.getMod();
        if( mods != null )
        {
        	x += mods.offset.getX();
        	y += mods.offset.getY();

        	fill = mods.fill.get();
        	colour = mods.colour.get();
        	rotation = mods.rotation.get();
        }

        AffineTransform origTrans = g2d.getTransform();
        AffineTransform at = new AffineTransform(origTrans);
        at.translate(x, y);
        at.translate(w * 0.5, h * 0.5);
        at.rotate(Math.toRadians(rotation));
        at.translate(w * -0.5, h * -0.5);

        Color origColor = g2d.getColor();

        g2d.setColor(colour.convertToColor());
        g2d.setTransform(at);

        if( fill )
        	g2d.fillOval(0, 0, (int)w, (int)h);
        else
        	g2d.drawOval(0, 0, (int)w, (int)h);

        g2d.setColor(origColor);
        g2d.setTransform(origTrans);
	}


	public void posradii(double x, double y, double a, double b)
	{
        this.poslen(x - a * 0.5, y - b * 0.5, a, b);
	}
}