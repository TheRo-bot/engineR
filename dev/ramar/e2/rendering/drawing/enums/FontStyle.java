package dev.ramar.e2.rendering.drawing.enums;

import java.awt.Font;

public enum FontStyle
{


    Normal(Font.PLAIN), Bold(Font.BOLD), Italic(Font.ITALIC)

    private int id;
    FontStyle(int id)
    {
        this.id = id;
    }

    public int intify()
    {   return this.id;   }

    public static FontStyle fromInt(int id)
    {
        for( FontStyle fs : FontStyle.values() )
            if( fs.id == id )
                return fs;

        return null;
    }

}