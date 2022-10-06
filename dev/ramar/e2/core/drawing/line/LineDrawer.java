package dev.ramar.e2.core.drawing.line;

import dev.ramar.e2.core.drawing.Drawer;

import dev.ramar.e2.core.structures.Vec2;

public abstract class LineDrawer extends Drawer<LineMods>
{
    public abstract void pospos(double x1, double y1, double x2, double y2);

    public void poslen(double x, double y, double xd, double yd)
    {
        this.pospos(x, y, x + xd, y + yd);
    }

    public void pospos(Vec2 from, Vec2 to)
    {
        if( from != null && to != null )
        {
            this.pospos(
                from.getX(),
                from.getY(),
                to.getX(),
                to.getY()
            );
        }
    }

    public void poslen(Vec2 from, Vec2 to)
    {
        if( from != null && to != null )
        {
            this.pospos(
                from.getX(),
                from.getY(),
                from.getX() + to.getX(),
                from.getY() + to.getY()
            );
        }
    }
}