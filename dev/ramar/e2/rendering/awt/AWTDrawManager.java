package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.e2.rendering.awt.drawing.rect.AWTRectDrawer;
import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygonDrawer;
import dev.ramar.e2.rendering.awt.drawing.line.AWTLineDrawer;


import java.awt.Graphics2D;

public class AWTDrawManager extends DrawManager<AWTRectDrawer, AWTPolygonDrawer, AWTLineDrawer>
{
    public AWTDrawManager()
    {}

    public void setGraphics(Graphics2D g2d)
    {
        this.rect.setGraphics(g2d);
        this.polygon.setGraphics(g2d);
        this.line.setGraphics(g2d);
    }

    public AWTRectDrawer createRectDrawer()
    {
        return new AWTRectDrawer();
    }

    public AWTPolygonDrawer createPolygonDrawer()
    {
        return new AWTPolygonDrawer();
    }

    public AWTLineDrawer createLineDrawer()
    {
        return new AWTLineDrawer();
    }


}