package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.structures.Vec2;

public abstract class PolygonDrawer
{
    public PolygonDrawer() 
    {

    }

    protected PolygonMods currMod = null;

    public PolygonMods getMod()
    {
        return this.currMod;
    }

    public PolygonMods withMod(PolygonMods pm)
    {
        this.currMod = pm;
        return this.currMod;
    }

    public PolygonMods withMod()
    {
        return this.withMod(new PolygonMods());
    }


    public void clearMod()
    {
        this.currMod = null;
    }


    public abstract void drawOffsets(Vec2... offsets);

    public abstract void drawOffsets(double... offsets);


    public abstract void drawPoints(double... points);
    public abstract void drawPoints(Vec2... points);
}