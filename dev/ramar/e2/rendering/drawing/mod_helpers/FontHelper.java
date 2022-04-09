package dev.ramar.e2.rendering.drawing.mod_helpers;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.drawing.FontStyle;

import java.awt.Font;

public class FontHelper<E extends ModHelperOwner>
{

    private E owner;

    public FontHelper(E owner) 
    {
        this.owner = owner;
    }

    private Font font = new Font("Arial Rounded MT Bold", Font.PLAIN, 12);
    public Font get()
    {   return this.font;   }




    public String getName()
    {    return this.font.getName();    }

    public E withName(String name)
    {
        this.font = new Font(name, this.font.getStyle(), this.font.getSize());
        return this.owner;
    }



    public FontStyle getStyle()
    {   return FontStyle.fromInt(this.font.getStyle());   }

    public E withStyle(FontStyle style)
    {
        this.font = new Font(this.font.getName(), style.intify(), this.font.getSize());
        return this.owner;
    }



    public int getSize()
    {   return this.font.getSize();   }

    public E withSize(int size)
    {
        this.font = new Font(this.font.getName(), this.font.getStyle(), size);
        return this.owner;
    }



}