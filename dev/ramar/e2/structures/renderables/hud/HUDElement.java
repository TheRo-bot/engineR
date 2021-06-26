package dev.ramar.e2.structures.renderables.hud;

import dev.ramar.e2.structures.Colour;
import dev.ramar.e2.structures.Vec2;


public abstract class HUDElement
{

    /*
    ENUMS: HorizontalAlign, VerticalAlign
    - Alignment enums to tell the element what it's x, y coordinates
      mean in relation to rendering.
    */

    /*
    x should be considered (LEFT, CENTER, RIGHT) point of drawing
    */

    public static enum HorizontalAlign
    {
        LEFT, CENTER, RIGHT
    }


    /*
    y should be considered (TOP, CENTER, BOTTOM) point of drawing
    */
    
    public static enum VerticalAlign
    {
        TOP, CENTER, BOTTOM
    }

    public static enum Visibility
    {
        SHOWN, HIDDEN, GONE
    }


    protected Vec2 pos = new Vec2(0);

    protected double dimX = 0,
                     dimY = 0;

    protected HorizontalAlign horizontalCentering;
    protected VerticalAlign   verticalCentering;

    protected Visibility visibility;

    protected Colour thisC;

    public HUDElement(double x, double y, double dimX, double dimY)
    {
        pos.set(x, y);
        this.dimX = dimX;
        this.dimY = dimY;
        horizontalCentering = HorizontalAlign.CENTER;
        verticalCentering = VerticalAlign.CENTER;
        visibility = Visibility.SHOWN;
    }


    public HUDElement(double x, double y, double dimX, double dimY, Colour c)
    {
        this(x, y, dimX, dimY);
        thisC = c;
    }


    public Vec2 getPos()
    {
        return pos;
    }

    public double getDimX()
    {
        return dimX;
    }


    public double getDIMY()
    {
        return dimY;
    }




    public void setAlignment(HorizontalAlign ha, VerticalAlign va)
    {
        if( ha != null )
            horizontalCentering = ha;

        if( va != null )
            verticalCentering = va;
    }


    public HorizontalAlign getHorizontalAlignment()
    {
        return horizontalCentering;
    }


    public VerticalAlign getVerticalAlignment()
    {
        return verticalCentering;
    }



    public double convertWithHoriAlign(double x)
    {
        switch(horizontalCentering)
        {
            case LEFT:
                return x - dimX / 2;

            case RIGHT:
                return x += dimX / 2;
        }

        return x;
    }


    public double convertWithVertAlign(double y)
    {
        switch(verticalCentering)
        {
            case TOP:
                return y - dimY / 2;

            case BOTTOM:
                return y + dimY / 2;
        }     
        
        return y;   
    }


    public void draw(OverlayerRendering o)
    {
        drawOffset(0, 0, o);
    }



    public abstract void drawOffset(double xO, double yO, OverlayerRendering o);

}