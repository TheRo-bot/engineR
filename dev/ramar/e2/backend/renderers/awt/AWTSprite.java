package dev.ramar.e2.backend.renderers.awt;

import dev.ramar.e2.backend.renderers.AWTRenderer;

import dev.ramar.e2.interfaces.rendering.Sprite;
import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Matrix;
import dev.ramar.e2.structures.StaticInfo;


import java.util.Random;

import java.io.*;

import java.awt.*;
import java.awt.event.*;


import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.ImageObserver;
import java.awt.image.AffineTransformOp;

import java.awt.geom.AffineTransform;

public class AWTSprite implements Sprite, ImageObserver, Renderable
{
	private static StaticInfo.Functions FUNCTIONS = StaticInfo.Functions.STATIC_REF;

	private BufferedImage im;

	public AWTSprite() {}

	public AWTSprite(BufferedImage thisI)
	{
		im = thisI;

	}



	public AWTSprite(String fileName)
	{
		load(fileName);
	}


	public AWTSprite(File file)
	{
		load(file);
	}


	public String toString()
	{
		return "Image " + getWidth() + " by " + getHeight() + ": " + im;
	}


	public void load(String fileName)
	{
		load(new File(fileName));
	}


	public void load(File f)
	{
		try
		{
			im = ImageIO.read(f);
		}
		catch( IOException e )
		{
			System.out.println("Couldn't load image! '" + f.getPath() + "' " + e.getMessage());
		}
	}


	public boolean imageUpdate(Image im, int infoflags, int x, int y, int width, int height)
	{
		this.im = (BufferedImage)im;

		return false;
	}


	public void parseFromSprite(Sprite sp)
	{
		im = (BufferedImage)(((AWTSprite)sp).getImage());
	}


	public int getWidth()
	{
		return im.getWidth();
	}


	public int getHeight()
	{
		return im.getHeight();
	}


	public Image getImage()
	{
		return im;
	}



	public void scaleToSize(int w, int h)
	{
		// Image returnedIm = im.getScaledInstance(w, h, Image.SCALE_DEFAULT);

		// im = (BufferedImage)returnedIm;
		// this.w = im.getWidth();
		// this.h = im.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int x, y;
		int ww = im.getWidth();
		int hh = im.getHeight();

		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
		  		int col = im.getRGB(x * ww / w, y * hh / h);
		  		img.setRGB(x, y, col);
			}
		}

	  	im = img;

	}


	/* Affine Transformations
	......................
	*/

	private AffineTransform at = new AffineTransform();
	private double rotatedAng = 0.0;


	public AffineTransform getTransformations()
	{
		return at;
	}


	public void setTransformation(AffineTransform at)
	{
		this.at = at;
	}


	public void rotate(double deg)
	{
		at.translate(im.getWidth()/2, im.getHeight()/2);
		at.rotate(Math.toRadians(deg));
		at.translate(-im.getWidth()/2, -im.getHeight()/2);
		rotatedAng += deg;

	}


	public void translate(double x, double y)
	{
		at.translate((int)FUNCTIONS.roundDouble(x, 0), (int)FUNCTIONS.roundDouble(y, 0));
	}


	private double scaleXAm = 0, scaleYAm = 0;

	public void scale(double xFactor, double yFactor)
	{
		translate(im.getWidth() / 2, im.getHeight() / 2);
		at.scale(xFactor, yFactor);
		scaleXAm += xFactor;
		scaleYAm += yFactor;
		translate(-im.getWidth() / 2, -im.getHeight() / 2);
	}


	public Vec2 getScaleAm()
	{
		return new Vec2(scaleXAm, scaleYAm);
	}



    public Sprite copyArea(int x, int y, int w, int h)
    {
    	return new AWTSprite(im.getSubimage(x, y, w, h));
	}


	/* Renderable Implementation
	-------------------------------
	*/


	public void rotateTo(double deg)
	{
		double toRotate = deg - rotatedAng;
		rotate(toRotate);
	}

	public void render(ViewPort vp)
	{
		drawSelf(null, vp);
	}


	public void drawSelf(Vec2 v, ViewPort vp)
	{
		int x = 0,
			y = 0;

		if( v != null )
		{
			x += (int)v.getX();
			y += (int)v.getY();
		}

		int moveX = x - im.getWidth() / 2,
			moveY = y - im.getHeight() / 2;


		if( vp instanceof AWTRenderer)
		{
			AWTRenderer ar = (AWTRenderer)vp;
			ar.drawImage(im, at, moveX, moveY);
		}

	}

}