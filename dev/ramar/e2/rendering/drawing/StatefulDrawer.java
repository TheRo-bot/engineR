package dev.ramar.e2.rendering.drawing;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.stateful.*;

import dev.ramar.utils.HiddenList;

import java.util.List;

public abstract class StatefulDrawer implements Drawable
{
    public final ShapeList shapes = new ShapeList();


    private class ShapeList extends HiddenList<Shape>
    {
        private List<Shape> getList()
        {
            return list;
        }
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        synchronized(shapes)
        {
            for( Shape s : shapes.getList() )
                s.drawAt(x, y, vp);
        }
    }
}