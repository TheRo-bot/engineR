package dev.ramar.e2.awt.drawing.image;

import dev.ramar.e2.core.structures.Colour;

import dev.ramar.e2.core.drawing.image.ImageMods;
import dev.ramar.e2.core.drawing.image.ImageDrawer;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Image;

import java.awt.geom.AffineTransform;

public class AWTImageDrawer extends ImageDrawer
{
    private Graphics2D graphics = null;
    private static final Colour DEFAULT_COLOUR = new Colour(255, 255, 255, 255);
    public void setGraphics(Graphics2D graphics)
    {
        this.graphics = graphics;
    }


    public void at(double x, double y, Image im)
    {
        Graphics2D g2d = this.graphics;
        if( g2d == null )
            return;

        double scale = 1.0;

        ImageMods mod = this.getMod();

        if( mod != null )
        {
        	scale = mod.scale.get();
        	x += mod.offset.getX();
        	y += mod.offset.getY();
        }

        AffineTransform origTrans = g2d.getTransform();
        AffineTransform at = new AffineTransform(origTrans);
        at.translate(x, y);
        at.scale(scale, scale);

        g2d.setTransform(at);

    	g2d.drawImage(im, null, null);

    	g2d.setTransform(origTrans);
    }

}