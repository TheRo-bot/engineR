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

    public Rect(double x, double y, double width, double height)
    {
        pos = new Vec2(x, y);
        this.width = width;
        this.height = height;
    }


    public double getX()
    {
        return pos.getX();
    }


    public double getY()
    {
        return pos.getY();
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


    /* Drawing Modifications
    -===-----------------------
    */

    private boolean fill = false;

    public Rect withFill()
    {
        fill = true;
        return this;
    }

    public Rect withoutFill()
    {
        fill = false;
        return this;
    }

    public Rect setFill(boolean doFill)
    {
        fill = doFill;
        return this;
    }


    private Colour colour = new Colour(255, 255, 255, 255);

    public Rect withColour(int r, int g, int b, int a)
    {
        colour.set(r, g, b, a);

        return this;
    }

    public Rect withSecondPos(double x, double y)
    {
        width = pos.getX() - x;
        height = pos.getY() - y;
        return this;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {

        RectMods rm = vp.draw.stateless.rect.withMod().
                               withOffset(x, y).
                               withColour(colour);

        vp.draw.stateless.rect.poslen(pos.getX(), pos.getY(), width, height);
    }

}