package dev.ramar.e2.structures.renderables.hud.elements;

import dev.ramar.e2.structures.renderables.hud.HUDElement;
import dev.ramar.e2.structures.renderables.hud.OverlayerRendering;

import dev.ramar.e2.structures.Colour;


public class RectangleElement extends HUDElement
{
    private boolean filled = false,
                    border = false;


    private Colour borderColour = null;
    private int borderDepth = -1;


    public RectangleElement(double x, double y, double dimX, double dimY, boolean fill)
    {
        super(x, y, dimX, dimY);
        filled = fill;        
    }

    public RectangleElement(double x, double y, double dimX, double dimY, Colour c, boolean fill)
    {
        this(x, y, dimX, dimY, fill);
        thisC = c;
    }


    public RectangleElement hasFill()
    {
        filled = true;
        return this;
    }

    public RectangleElement noFill()
    {
        filled = false;
        return this;
    }

    public RectangleElement showBorder(Colour c, int depth)
    {
        borderColour = c;
        if( depth < 0 )
            throw new IllegalArgumentException("Depth must be > 0");
        borderDepth = depth;

        border = true;
        return this;
    }


    public RectangleElement showBorder()
    {
        if( borderColour == null || borderDepth == -1)
            throw new IllegalStateException("No border information set, use showBorder(Colour, int)");
        border = true;
        return this;
    }

    public RectangleElement noBorder()
    {
        border = false;
        return this;
    }


    @Override
    public void drawOffset(double xO, double yO, OverlayerRendering o)
    {
        double x = pos.getX() + xO,
               y = pos.getY() + yO;


        Colour c = o.getColour();


        x = convertWithHoriAlign(x);
        y = convertWithVertAlign(y);


        if( border )
        {
            o.setColour(borderColour);

            double hx = dimX/2,
                   hy = dimY/2,
                   hd = borderDepth/2;

            o.drawRect(x - hx - hd, y - hy - hd, (int)(dimX + borderDepth), (int)(dimY + borderDepth) );


        }

        o.setColour(thisC);

        if( filled )
            o.drawRect(x - dimX/2, y - dimY/2, (int)dimX, (int)dimY);
        else
            o.outlineRect(x - dimX/2, y - dimY/2, (int)dimX, (int)dimY);

        o.setColour(c);
    }



}