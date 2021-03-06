package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ViewPort;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;

import java.awt.image.AffineTransformOp;

import java.awt.geom.AffineTransform;

public class AWTImage implements Image
{
    private BufferedImage image;
    private AffineTransformOp op = null;

    private double rotationAm = 0.0,
                   xScaleAm = 1.0,
                   yScaleAm = 1.0;

    public AWTImage(File file)
    {
        try
        {
            image = ImageIO.read(file);
        }
        catch(IOException e)
        {}
    }   

    public AWTImage(BufferedImage bi)
    {
        image = bi;
    }

    public AWTImage(InputStream is) throws IOException
    {
        image = ImageIO.read(is);
    }


    /* Image Implementation
    --===---------------------
    */

    public int getWidth()
    {
        if( image == null )
            return 0;
        return (int)(image.getWidth() * xScaleAm);

    }
    
    public int getHeight()
    {
        if( image == null )
            return 0;
        return (int)(image.getHeight() * yScaleAm);
    }


    public BufferedImage getBufferedImage()
    {
        return image;
    }



    public void scale(double xAm, double yAm)
    {
        AffineTransform at = new AffineTransform();
        at.scale(xAm, yAm);

        xScaleAm += xAm;
        yScaleAm += yAm;

        /*if( op == null )
            op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        else
            op.getTransform().concatenate(at);
        */
    }


    public void rotate(double zAm)
    {
        AffineTransform at = new AffineTransform();
        at.rotate(zAm);
        rotationAm += zAm;

        /*if( op == null )
            op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        else
            op.getTransform().concatenate(at);
        */
    }

    public double getScaleX()
    {
        return xScaleAm;
    }


    public double getScaleY()
    {
        return yScaleAm;
    }


    public double getRotZ()
    {
        return rotationAm;
    }


}