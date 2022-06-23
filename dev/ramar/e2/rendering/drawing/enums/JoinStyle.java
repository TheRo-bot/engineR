package dev.ramar.e2.rendering.drawing.enums;

import java.awt.BasicStroke;

public enum JoinStyle
{

    Bevel(BasicStroke.JOIN_BEVEL), Miter(BasicStroke.JOIN_MITER),
    Round(BasicStroke.JOIN_ROUND);

    private int id;
    JoinStyle(int id)
    {
        this.id = id;
    }

    public int intify()
    {   return this.id;   }


    public static JoinStyle fromInt(int id)
    {
        for( JoinStyle js : JoinStyle.values() )
            if( js.id == id )
                return js;

        return null;
    }

}
