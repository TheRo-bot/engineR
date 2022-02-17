package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.Drawable;
import java.awt.image.BufferedImage;


public abstract class Image
{
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract BufferedImage getBufferedImage();

    public abstract void scale(double xAm, double yAm);

    public abstract void rotate(double zAm);

    public abstract double getScaleX();

    public abstract double getScaleY();

    public abstract double getRotZ();

}