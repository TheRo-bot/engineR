package dev.ramar.e2.rendering.drawing.enums;

import java.awt.BasicStroke;

public enum CapStyle
{

    Butt(BasicStroke.CAP_BUTT), Round(BasicStroke.CAP_ROUND),
    Square(BasicStroke.CAP_SQUARE);

    private int id;
    CapStyle(int id)
    {
        this.id = id;
    }

    public int intify()
    {   return this.id;   }


    public static CapStyle fromInt(int id)
    {
        for( CapStyle cs : CapStyle.values() )
            if( cs.id == id )
                return cs;

        return null;
    }

}