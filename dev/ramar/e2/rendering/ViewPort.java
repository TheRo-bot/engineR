package dev.ramar.e2.rendering;


import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;


public abstract class Viewport<K extends LayerManager, V extends DrawManager>
{
    public final Colour background = new Colour(0, 0, 0, 255);

    protected Viewport()
    {
        this.layers = this.createLayerManager();
        this.draw = this.createDrawManager();
    }


    protected Vec2 centerPoint = new Vec2(0);

    public double getCenterX()
    {
        if( this.centerPoint != null )
            return this.centerPoint.getX();

        return 0;
    }

    public double getCenterY()
    {
        if( this.centerPoint != null )
            return this.centerPoint.getY();

        return 0;
    }

    public void setCenter(double x, double y)
    {
        this.centerPoint.set(x, y);
    }

    public void followPoint(Vec2 v)
    {
        this.centerPoint = v;
    }



    protected K layers = null;
    protected abstract K createLayerManager();

    protected V draw = null;
    protected abstract V createDrawManager();

}