package dev.ramar.e2.rendering.drawing.stateful;


import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;
/*
Shape: Rect
 - A Rectangular shape
   (A persistent rectangle)
*/
public class Rect extends Shape
{
    private Vec2 pos;
    private double width, height;

    private RectMods mod = new RectMods().withPermanence(true);    

    public Rect(double x, double y, double width, double height)
    {
        pos = new Vec2(x, y);
        this.width = width;
        this.height = height;
    }


    public Rect(double w, double h)
    {
        pos = new Vec2(0, 0);
        this.width = w;
        this.height = h;
    }


    public double getX()
    {
        return mod.modX(pos.getX());
    }


    public double getY()
    {
        return mod.modY(pos.getY());
    }

    public double getW()
    {
        return width;
    }

    public double getH()
    {
        return height;
    }


    public void setX(double x)
    {
        pos.setX(x);
    }

    public void setY(double y)
    {
        pos.setY(y);
    }

    public void setW(double w)
    {
        width = w;
    }

    public void setH(double h)
    {
        height = h;
    }

    public RectMods getMod()
    {
        return mod;
    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        boolean doOffset = mod.isOffsetAllowed();

        if( doOffset ) mod.withOffset(x, y);

        vp.draw.stateless.rect.useTempMod(mod);

        vp.draw.stateless.rect.poslen(pos.getX(), pos.getY(), width, height);

        vp.draw.stateless.rect.clearTempMod();

        if( doOffset ) mod.withOffset(-x, -y);
    }

}