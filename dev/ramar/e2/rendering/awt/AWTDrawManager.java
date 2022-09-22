package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.e2.rendering.awt.rect.AWTRectDrawer;
import dev.ramar.e2.rendering.awt.polygon.AWTPolygonDrawer;
import dev.ramar.e2.rendering.awt.polyline.AWTPolylineDrawer;

public class AWTDrawManager extends DrawManager<AWTRectDrawer, AWTPolygonDrawer, AWTPolylineDrawer>
{
    public AWTDrawManager()
    {}

    private Graphics2D graphics = null;

    public void setGraphics(Graphics2D g2d)
    {
        this.graphics = g2d;
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