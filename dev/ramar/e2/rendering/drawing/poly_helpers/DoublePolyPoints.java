package dev.ramar.e2.rendering.drawing.poly_helpers;

import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.structures.Vec2;

import java.util.Arrays;

public class DoublePolyPoints extends PolyPoints
{

    public DoublePolyPoints() {}

    private double[] points = new double[0];

    //// list stuff

    public PolyPoints add(double x, double y)
    {
        this.points = Arrays.copyOf(this.points, this.points.length + 2);
        this.points[this.points.length - 2] = x;
        this.points[this.points.length - 1] = y;

        return this;
    }

    public PolyPoints add(Vec2 v)
    {   return this.add(v.getX(), v.getY());   }



    public void remove(int index)
    {
        if( index < 0 || index >= this.points.length / 2 )
            throw new IndexOutOfBoundsException(index + " (for 0 -> " + (this.points.length / 2 - 1) + ")");

        double[] newPoints = Arrays.copyOf(this.points, this.points.length - 2);

        for( int ii = index; ii < newPoints.length; ii++ )
            newPoints[ii] = this.points[ii + 1];

        this.points = newPoints;
    }


    public int size()
    {   return this.points.length / 2;   }


    //// X stuff

    public double getX(int index)
    {
        if( index < 0 || index > this.points.length / 2)
            throw new IndexOutOfBoundsException(index + " for range 0-" + this.points.length / 2);

        return this.points[index / 2];
    }


    public PolyPoints withX(int index, double mod)
    {
        this.points[index] = mod;
        return this;
    }



    //// Y stuff

    public double getY(int index)
    {
        return this.points[index / 2 + 1];
    }


    public PolyPoints withY(int index, double mod)
    {
        this.points[index / 2 + 1] = mod;
        return this;
    }



    public void drawClosed(double x, double y, ViewPort vp)
    {
        if( this.offsets )
            vp.draw.polygon.offsets(x, y, points);
        else
            vp.draw.polygon.points(x, y, points);
    }

    
    public void drawOpen(double x, double y, ViewPort vp)
    {
        if( this.offsets )
            vp.draw.polyline.offsets(x, y, points);
        else
            vp.draw.polyline.points(x, y, points);
    }

}