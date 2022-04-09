package dev.ramar.e2.rendering.awt.drawing.text;


import dev.ramar.e2.rendering.drawing.text.TextDrawer;

import dev.ramar.e2.rendering.awt.AWTViewPort;

public class AWTTextDrawer extends TextDrawer
{

    private AWTViewPort vp = null;

    public AWTTextDrawer withViewPort(AWTViewPort vp)
    {
        this.vp = vp;
        return this;
    }

    public void at(double x, double y, String s)
    {

    }
}