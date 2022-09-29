package dev.ramar.e2.core.drawing.polygon;

import dev.ramar.e2.core.drawing.Drawer;


public abstract class PolygonDrawer extends Drawer<PolygonMods>
{

    public PolygonMods withMod()
    {  return this.withMod(new PolygonMods()); }

    // 1D array representing [x1, y1, x2, y2, ...., x(n), y(n)]
    public abstract void positions(double... poses);
    public abstract void offsets(double... offsets);
}
