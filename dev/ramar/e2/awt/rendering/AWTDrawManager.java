package dev.ramar.e2.awt.rendering;

import dev.ramar.e2.core.rendering.DrawManager;

import dev.ramar.e2.awt.drawing.rect.AWTRectDrawer;
import dev.ramar.e2.awt.drawing.line.AWTLineDrawer;


import java.awt.Graphics2D;

public class AWTDrawManager extends DrawManager<AWTRectDrawer, AWTLineDrawer>
{
    public AWTDrawManager()
    {}

    public void setGraphics(Graphics2D g2d)
    {
        this.rect.setGraphics(g2d);
        // this.polygon.setGraphics(g2d);
        this.line.setGraphics(g2d);
    }

    public AWTRectDrawer createRectDrawer()
    {
        return new AWTRectDrawer();
    }

    // public AWTPolygonDrawer createPolygonDrawer()
    // {
    //     return new AWTPolygonDrawer();
    // }

    public AWTLineDrawer createLineDrawer()
    {
        return new AWTLineDrawer();
    }


}