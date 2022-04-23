package dev.ramar.e2.rendering.drawing.poly_helpers;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.rendering.ViewPort;

public abstract class PolyPoints 
{

    //// list stuff

    public abstract PolyPoints add(double x, double y);
    public abstract PolyPoints add(Vec2 v);
    public abstract void remove(int index);

    public abstract int size();

    //// X stuff

    public abstract double getX(int index);
    public abstract PolyPoints withX(int index, double mod);

    public PolyPoints addX(int index, double mod)
    {
        this.withX(index, this.getX(index) + mod);
        return this;
    }

    //// Y stuff

    public abstract double getY(int index);
    public abstract PolyPoints withY(int index, double mod);

    public PolyPoints addY(int index, double mod)
    {
        return this.withY(index, this.getY(index) + mod);
    }


    public PolyPoints add(int index, double x, double y)
    {
        this.addX(index, x);
        this.addY(index, y);
        return this;
    }

    public PolyPoints set(int index, double x, double y)
    {
        this.withX(index, x);
        this.withY(index, y);
        return this;
    }

    //// if it closed or not


    protected boolean offsets = false;
    public PolyPoints makeOffsets(boolean b)
    {
        this.offsets = b;
        return this;
    }


    public abstract void drawClosed(double x, double y, ViewPort vp);
    public abstract void drawOpen(double x, double y, ViewPort vp);
}