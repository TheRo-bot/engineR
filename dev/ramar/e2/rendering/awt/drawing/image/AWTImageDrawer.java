package dev.ramar.e2.rendering.awt.drawing.image;

import dev.ramar.e2.rendering.drawing.image.ImageDrawer;

import dev.ramar.e2.rendering.Image;

import dev.ramar.e2.rendering.awt.AWTViewPort;

public class AWTImageDrawer extends ImageDrawer
{

    private AWTViewPort vp = null;

    public AWTImageDrawer withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        return this;
    }


    public void at(double x, double y, Image i)
    {

    }
}