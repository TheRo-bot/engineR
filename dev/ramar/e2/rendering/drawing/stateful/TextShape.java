package dev.ramar.e2.rendering.drawing.stateful;

import dev.ramar.e2.rendering.drawing.stateless.TextDrawer;
import dev.ramar.e2.rendering.drawing.stateless.TextDrawer.TextMods;

import dev.ramar.e2.rendering.ViewPort;

public class TextShape extends Shape
{
    private String text;
    private TextMods mod = new TextMods().withPermanence(true);

    public TextShape()
    {

    }

    public TextShape(String s)
    {
        withText(s);
    }

    public TextShape withText(String s)
    {
        text = s;
        return this;
    }


    public TextMods getMod()
    {
        return mod;
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