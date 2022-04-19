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
        switch(id)
        {
            case Font.PLAIN:
                return FontStyle.Normal;

            case Font.BOLD:
                return FontStyle.Bold;

            case Font.ITALIC:
                return FontStyle.Italic;

            default:
                return null;
        }
    }
}