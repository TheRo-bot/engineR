package dev.ramar.e2.rendering.drawing.line;

import dev.ramar.e2.rendering.drawing.Drawer;



public abstract class LineDrawer extends Drawer<LineMods>
{
    public abstract void pospos(double x1, double y1, double x2, double y2);

    public abstract void poslen(double x, double y, double xd, double yd);
}