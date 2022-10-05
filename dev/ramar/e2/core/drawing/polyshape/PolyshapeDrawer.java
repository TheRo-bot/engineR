package dev.ramar.e2.core.drawing.polyshape;

import dev.ramar.e2.core.drawing.Drawer;


public abstract class PolyshapeDrawer extends Drawer<PolyshapeMods>
{

    public PolyshapeMods withMod()
    {  return this.withMod(new PolyshapeMods()); }

    // 1D array representing [x1, y1, x2, y2, ...., x(n), y(n)]
    public abstract void positions(double... poses);
    public abstract void offsets(double... offsets);
}
