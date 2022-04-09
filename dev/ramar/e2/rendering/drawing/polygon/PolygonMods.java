package dev.ramar.e2.rendering.drawing.polygon;


import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.JoinStyle;
import dev.ramar.e2.rendering.drawing.CapStyle;


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


    public PolygonMods withOnlyOffset(double x, double y)
    {
        return this.withOffset(x, y, true);
    }



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


    private float thickness = 1.0f;
    public float getThickness()
    {   return this.thickness;   }


    public PolygonMods withThickness(double thickness)
    {
        this.thickness = (float)thickness;
        return this;
    }



    private CapStyle capStyle = CapStyle.Round;
    public CapStyle getCapStyle()
    {   return this.capStyle;   }

    public PolygonMods withCapStyle(CapStyle cs)
    {
        this.capStyle = cs;
        return this;
    }



    private JoinStyle joinStyle = JoinStyle.Bevel;
    public JoinStyle getJoinStyle()
    {   return this.joinStyle;   }

    public PolygonMods withJoinStyle(JoinStyle js)
    {
        this.joinStyle = js;
        return this;
    }



    private float miter = 10.0f;
    public float getMiter()
    {   return this.miter;   }

    public PolygonMods withMiter(double m)
    {
        this.miter = (float)m;
        return this;
    }

}

