package dev.ramar.e2.backend.renderers.awt;

import dev.ramar.e2.interfaces.rendering.AnimatedSprite;
import dev.ramar.e2.interfaces.rendering.Sprite;

import dev.ramar.e2.backend.Moment;

import java.util.*;
import java.io.*;


import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.stream.*;
import javax.imageio.ImageReadParam;

import java.awt.image.BufferedImage;
import java.awt.Dimension;




public class AWTAnimSprite implements AnimatedSprite
{

	private List<Sprite> sprites = new ArrayList<>();

	private double fps;


	public AWTAnimSprite()
	{
		fps = 0.0;
	}

	public AWTAnimSprite(double fps)
	{
		this.fps = fps;
	}


	public AWTAnimSprite(List<Sprite> sprites, double fps)
	{
		this.sprites.addAll(sprites);
		this.fps = fps;
	}




	public AWTAnimSprite(String fileName, double fps)
	{
		loadFromFile(fileName);
		this.fps = fps;

		// System.out.println("!!! AWTAnimSprite: " + sprites);
	}


	public AWTAnimSprite(File file, double fps)
	{
		loadFromFile(file);
		this.fps = fps;

	}




	public void loadSpriteSheet(Sprite s, int w, int h)
	{
		sprites.clear();

		if( s.getWidth() % w != 0 )
			throw new IllegalArgumentException("Sprite width (" + s.getWidth() + ") is not divisible by " + w);

		if( s.getHeight() % w != 0 )
			throw new IllegalArgumentException("Sprite height (" + s.getHeight() + ") is not divisible by " + h);

		System.out.println("width: " + w + " height: " + h + " " + s.getWidth());
		for( int ii = 0; ii < s.getWidth(); ii += w)
		{
			// sprites.add(new Sprite(s.getImage().getSubimage(ii, 0, w, h)));
			sprites.add(s.copyArea(ii, 0, w, h));
		}

		System.out.println("!! loaded " + sprites.size() + " sprites");



	}





	public void loadFromFile(String fileName)
	{
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		Iterator<ImageReader> readerIter = ImageIO.getImageReadersByFormatName(extension);


		while(readerIter.hasNext())
		{
			ImageReader thisReader = readerIter.next();
			if( thisReader != null )
			{
				try
				{
					// System.out.println(Arrays.toString(thisReader.getOriginatingProvider().getInputTypes()));

					System.out.println("path: " + new File(fileName));
					ImageInputStream iis = ImageIO.createImageInputStream(new File(fileName));
					System.out.println("iis: " + iis);
					thisReader.setInput(iis);
					int index = 0;
					while(true)
					{
						BufferedImage bi = thisReader.read(index);

						if( bi != null )
						{
							AWTSprite aSprite = new AWTSprite(bi);
							sprites.add(aSprite);
						}
						index++;
					}
				}
				catch(IOException e) 
				{
					System.out.println("AWTAnimSprite.loadFromFile: IOException! " + e.getMessage());

				}

				catch(IndexOutOfBoundsException e) 
				{
					System.out.println("AWTAnimSprite.loadFromFile: IndexOutOfBoundsException! " + e.getMessage());
				}

				catch(IllegalArgumentException e) 
				{
					System.out.println("AWTAnimSprite.loadFromFile: IllegalArgumentException! " + e.getMessage());
				}
			}
		}
	}


	public void loadFromFile(File file)
	{
		loadFromFile(file.toString());
	}




 
	/* AnimatedSprite implementation
	------------------------------------
	*/

	public int getFrameCount()
	{
		return sprites.size();
	}


	public Sprite getNFrame(int n)
	{
		return sprites.get(n);
	}


	public double getFPS()
	{
		return fps;
	}


	public Sprite getFrame(long startTime, long passedTime)
	{
		System.out.println(passedTime / fps % 1000/fps < 0.1 );

		return sprites.get(0);
	}

	public Sprite getFrame(int startSec, int startUP, Moment time)
	{
		return sprites.get(0);
	}

}	