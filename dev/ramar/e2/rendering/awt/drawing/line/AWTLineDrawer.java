package dev.ramar.e2.rendering.awt.drawing.line;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.line.LineMods;
import dev.ramar.e2.rendering.drawing.line.LineDrawer;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;

public class AWTLineDrawer extends LineDrawer
{
    private Graphics2D graphics = null;
    private static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }

    public void pospos(double x1, double y1, double x2, double y2)
    {
    	if( this.graphics != null )
    	{
    		LineMods mods = this.getMod();
    		Colour colour = DEFAULT_COLOUR;
    		double width = 1.0;

    		if( mods != null )
    		{
	    		colour = mods.colour.get();
	    		width = mods.width.get();

	    		double offX = mods.offset.getX(), 
	    			   offY = mods.offset.getY();

	    		x1 += offX;
	    		x2 += offX;

	    		y1 += offY;
	    		y2 += offY;
    		}

    		java.awt.Color oldColor = graphics.getColor();
    		java.awt.Stroke oldStroke = graphics.getStroke();

    		graphics.setColor(colour.convertToColor());
    		graphics.setStroke(new BasicStroke((float)width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    		graphics.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

    		graphics.setColor(oldColor);
    		graphics.setStroke(oldStroke);
    	}
    }

}