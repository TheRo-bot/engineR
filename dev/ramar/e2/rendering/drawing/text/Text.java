package dev.ramar.e2.rendering.drawing.text;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

public class Text implements Drawable
{

    public Text(String text)
    {
        this.withText(text);
    }


    private String text = null;

    public String getText()
    {   return this.text;   }

    public Text withText(String text)
    {
        this.text = text;
        return this;
    }

    public void drawAt(double x, double y, ViewPort vp)
    {

    }
}
