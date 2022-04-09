package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.structures.Vec2;

public class PolylineMods
{

    public PolylineMods()
    {

    }


    private Vec2 offset = new Vec2();

    public Vec2 getOffset()
    {   return this.offset;   }

    public double getOffX()
    {   return this.offset.getX();   }

    public double getOffY()
    {   return this.offset.getY();   }


    private Colour colour = new Colour();

    public Colour getColour()
    {   return this.colour;   }

    public PolylineMods withColour(int r, int g, int b, int a)
    {
        this.colour.set(r, g, b, a);
        return this;
    }


    private double thickness = 1.0;
    public double getThickness()
    {   return this.thickness;   }


    public PolygonMods withThickness(double thickness)
    {
        this.thickness = thickness;
        return this;
    }

}