package dev.ramar.e2.demos.bufferedimage;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.ImageMods;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public abstract class BufferDrawable extends Image implements Drawable
{   
    BufferedImage bi;
    ImageMods im = new ImageMods();

    private double x = 0, 
                   y = 0;

    public BufferDrawable(double x, double y, double w, double h)
    {
        bi = new BufferedImage((int)w, (int)h, BufferedImage.TYPE_INT_ARGB);
        setX(x);
        setY(y);
    }

    public double getX()
    {   return this.x;    }

    private void onChange_X()
    {
        updatePos();
    }

    public void setX(double x)
    {
        this.x = x;
        onChange_X();
    }

    public BufferDrawable withX(double x)
    {
        setX(x);
        return this;
    }

    public double getY()
    {   return this.y;   }

    private void onChange_Y()
    {
        updatePos();
    }

    public void setY(double y)
    {
        this.y = y;
        onChange_Y();
    }

    public BufferDrawable withY(double y)
    {
        setY(y);
        return this;
    }

    public ImageMods getMod()
    {   return this.im;   }


    public Graphics2D getGraphics()
    {
        return bi.createGraphics();
    }

    public void clear()
    {
        for( int y = 0; y < bi.getHeight(); y++ )
            for( int x = 0; x < bi.getWidth(); x++ )
                bi.setRGB(x, y, 0);

    }

    public BufferedImage getImage()
    {
        return bi;
    }


    public abstract void draw();

    protected Object drawLock = new Object();

    public void drawAt(double x, double y, ViewPort vp)
    {
        synchronized(drawLock)
        {
            vp.draw.stateless.image.withTempMod(this.im);
            vp.draw.stateless.image.pos_c(x, y, this);
            vp.draw.stateless.image.clearTempMod();
        }
    }

    /* Event Methods
    -===---------------
    */

    public void redraw()
    {
        synchronized(drawLock)
        {
            this.clear();
            this.draw();
        }
    }

    private void updatePos()
    {
        this.im.withOffsetOnly(this.x, this.y);
        redraw();
    }


    /* Image Implementation
    --===---------------------
    */

    public int getWidth()
    {   return bi.getWidth();  }

    public int getHeight()
    {   return bi.getHeight();  }

    public BufferedImage getBufferedImage()
    {
        return bi;
    }

    public void scale(double xAm, double yAm)
    {
        Graphics2D g2d = getGraphics();
        g2d.scale(xAm, yAm);
    }


    private double thetaCache = 0.0;
    public void rotate(double zAm)
    {
        thetaCache += zAm;
        Graphics2D g2d = getGraphics();
        g2d.rotate(zAm);
    }

    public double getScaleX()
    {
        AffineTransform at = getGraphics().getTransform();
        return at.getScaleX();
    }

    public double getScaleY()
    {
        AffineTransform at = getGraphics().getTransform();
        return at.getScaleY();
    }

    public double getRotZ()
    {
        return thetaCache;
    }
}