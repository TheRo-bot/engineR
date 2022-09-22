package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.e2.rendering.awt.drawing.rect.AWTRectDrawer;
import dev.ramar.e2.rendering.awt.drawing.polygon.AWTPolygonDrawer;
import dev.ramar.e2.rendering.awt.drawing.polyline.AWTPolylineDrawer;


import java.awt.Graphics2D;

public class AWTDrawManager extends DrawManager<AWTRectDrawer, AWTPolygonDrawer, AWTPolylineDrawer>
{
    public AWTDrawManager()
    {}

    public void setGraphics(Graphics2D g2d)
    {
        this.rect.setGraphics(g2d);
        this.polygon.setGraphics(g2d);
        this.polyline.setGraphics(g2d);
    }

    public AWTRectDrawer createRectDrawer()
    {
        return new AWTRectDrawer();
    }

    public AWTPolygonDrawer createPolygonDrawer()
    {
        return new AWTPolygonDrawer();
    }

    public AWTPolylineDrawer createPolylineDrawer()
    {
        return new AWTPolylineDrawer();
    }


}