package dev.ramar.e2.rendering.drawing.stateful;

import dev.ramar.e2.rendering.drawing.stateless.TextDrawer;
import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.TextMods;

import dev.ramar.e2.rendering.ViewPort;

public class TextShape extends Shape
{
    private String text = "";
    private TextMods mod = new TextMods().withPermanence(true);

    public TextShape()
    {

    }

    public TextShape(String s)
    {
        withText(s);
    }


    public void mimic(TextShape ts)
    {
        this.text = ts.text;
        this.mod = ts.mod.clone();
    }

    public TextShape withText(String s)
    {
        text = s;
        return this;
    }

    public double getXAtChar(int index)
    {
        double exp = 0;
        if( text != null && mod != null )
        {
            if( index >= 0 )
            {
                exp += mod.getWidthOfText(text.charAt(index)) / 2;
                if( index > 0 )
                    exp += mod.getWidthOfText(text.substring(0, Math.max(0, index - 1)));

            }
        }

        return exp;
    }

    public double getWidth()
    {
        return mod != null && text != null ? 
                   mod.getWidthOfText(text) :
                   0.0;
    }

    public double getHeight()
    {

        return mod != null ?
                   mod.getStringHeight() :
                   0.0;
    }


    public TextMods getMod()
    {
        return mod;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String s)
    {
        text = s;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.stateless.text.withTempMod(mod);
        if( mod.isOffsetAllowed() )
            vp.draw.stateless.text.pos_c(x, y, text);
        else
            vp.draw.stateless.text.pos_c(0, 0, text);
        vp.draw.stateless.text.clearTempMod();
    }   
}