package dev.ramar.e2.core.drawing.text;

import dev.ramar.e2.core.drawing.Drawer;


public abstract class TextDrawer extends Drawer<TextMods>
{

    public TextMods withMod()
    {
        return super.withMod(new TextMods());
    }

	
	public abstract void at(double x, double y, String text);
}