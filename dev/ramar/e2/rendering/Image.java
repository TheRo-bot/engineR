package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.Drawable;
import java.awt.image.BufferedImage;


public interface Image
{
    public int getWidth();
    public int getHeight();

    public BufferedImage getBufferedImage();

    public void scale(double xAm, double yAm);
    public double getScaleX();
    public double getScaleY();

    public void rotate(double zAm);
    public double getRotZ();
}