package dev.ramar.e2.rendering.drawing.polygon;


import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.structures.Colour;

public class PolygonMods
{

    public PolygonMods()
    {

    }



    private Vec2 offset = new Vec2(0);
    public Vec2 getOffset()
    {   return this.offset;   }
    public double getOffX()
    {   return this.offset.getX();   }
    public double getOffY()
    {   return this.offset.getY();   }

    public PolygonMods withOffset(double x, double y)
    {
        this.offset.add(x, y);
        return this;
    } 

    public PolygonMods withOffset(double x, double y, boolean forced)
    {
        if( forced )
            this.offset.set(x, y);
        else
            this.offset.add(x, y);

        return this;
    }

    public void setOffset(double x, double y)
    {   this.withOffset(x, y, true);   }




    private Colour colour = new Colour(0, 0, 0, 0);
    public Colour getColour()
    {   return this.colour;   }


    public PolygonMods withColour(int r, int g, int b, int a)
    {
        this.colour.set(r, g, b, a);
        return this;
    }


    private boolean fill = false;
    public boolean getFill()
    {   return this.fill;   }


    public PolygonMods withFill()
    {   return this.withFill(true);   }

    public PolygonMods withoutFill()
    {   return this.withFill(false);   }

    public PolygonMods withFill(boolean b)
    {
        this.fill = b;
        return this;
    }




}

