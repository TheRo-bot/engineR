package dev.ramar.e2.awt.rendering;

import dev.ramar.e2.core.rendering.DrawManager;

import dev.ramar.e2.awt.drawing.rect.AWTRectDrawer;
import dev.ramar.e2.awt.drawing.line.AWTLineDrawer;
import dev.ramar.e2.awt.drawing.polyshape.AWTPolyshapeDrawer;
import dev.ramar.e2.awt.drawing.text.AWTTextDrawer;
import dev.ramar.e2.awt.drawing.image.AWTImageDrawer;

import java.awt.Graphics2D;

public class AWTDrawManager extends DrawManager<AWTRectDrawer, AWTLineDrawer, AWTPolyshapeDrawer, 
                                                AWTTextDrawer, AWTImageDrawer>
{
    public AWTDrawManager()
    {}

    public void setGraphics(Graphics2D g2d)
    {
        this.rect.setGraphics(g2d);
        this.line.setGraphics(g2d);
        this.polyshape.setGraphics(g2d);
        this.text.setGraphics(g2d);
        this.image.setGraphics(g2d);
    }

    public AWTRectDrawer createRectDrawer()
    {
        return new AWTRectDrawer();
    }

    public AWTLineDrawer createLineDrawer()
    {
        return new AWTLineDrawer();
    }

    public AWTPolyshapeDrawer createPolyshapeDrawer()
    {
        return new AWTPolyshapeDrawer();
    }

    public AWTTextDrawer createTextDrawer()
    {  return new AWTTextDrawer();  }

    public AWTImageDrawer createImageDrawer()
    {  return new AWTImageDrawer();  }

}