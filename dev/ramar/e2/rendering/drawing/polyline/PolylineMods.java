package dev.ramar.e2.rendering.drawing.polyline;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.rendering.drawing.JoinStyle;
import dev.ramar.e2.rendering.drawing.CapStyle;

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


    public PolylineMods withOffset(double x, double y)
    {
        return this.withOffset(x, y, false);
    }

    public PolylineMods withOffset(double x, double y, boolean force)
    {
        if( force )
            this.offset.set(x, y);
        else
            this.offset.add(x, y);

        return this;
    }

    public void setOffset(double x, double y)
    {
        this.withOffset(x, y, true);
    }

    public PolylineMods withOnlyOffset(double x, double y)
    {
        return this.withOffset(x, y, true);
    }



    private Colour colour = new Colour(255, 255, 255, 255);

    public Colour getColour()
    {   return this.colour;   }

    public PolylineMods withColour(int r, int g, int b, int a)
    {
        this.colour.set(r, g, b, a);
        return this;
    }


    private float thickness = 1.0f;
    public float getThickness()
    {   return this.thickness;   }


    public PolylineMods withThickness(double thickness)
    {
        this.thickness = (float)thickness;
        return this;
    }



    private CapStyle capStyle = CapStyle.Round;
    public CapStyle getCapStyle()
    {   return this.capStyle;   }

    public PolylineMods withCapStyle(CapStyle cs)
    {
        this.capStyle = cs;
        return this;
    }



    private JoinStyle joinStyle = JoinStyle.Bevel;
    public JoinStyle getJoinStyle()
    {   return this.joinStyle;   }

    public PolylineMods withJoinStyle(JoinStyle js)
    {
        this.joinStyle = js;
        return this;
    }



    private float miter = 10.0f;
    public float getMiter()
    {   return this.miter;   }

    public PolylineMods withMiter(double m)
    {
        this.miter = (float)m;
        return this;
    }

}