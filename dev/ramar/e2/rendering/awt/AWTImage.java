package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AWTImage extends Image
{
    private BufferedImage image;


    public AWTImage(File file)
    {
        try
        {
            image = ImageIO.read(file);
        }
        catch(IOException e)
        {}
    }   


    @Override
    public int getWidth()
    {
        if( image == null )
            return 0;
        return image.getWidth();

    }
    
    @Override
    public int getHeight()
    {
        if( image == null )
            return 0;
        return image.getHeight();
    }


    @Override
    public void drawAt(double xOff, double yOff, ViewPort vp)
    {
        if( vp instanceof AWTViewPort )
        {
            AWTStatelessDrawer sless = (AWTStatelessDrawer)vp.draw.stateless;
        }
    }


    @Override
    public BufferedImage getBufferedImage()
    {
        return image;
    }
}