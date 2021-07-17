package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.Drawable;
import java.awt.image.BufferedImage;


public abstract class Image implements Drawable
{
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract void drawAt(double xOff, double yOff, ViewPort vp);

    public abstract BufferedImage getBufferedImage();
}