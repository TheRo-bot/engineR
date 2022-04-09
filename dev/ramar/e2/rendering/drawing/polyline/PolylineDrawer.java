package dev.ramar.e2.rendering.drawing.polyline;


import dev.ramar.e2.structures.Vec2;


public abstract class PolylineDrawer
{


    public PolylineDrawer()
    {

    }

    private PolylineMods mod = null;
    public PolylineMods getMod()
    {   return this.mod;   }

    public PolylineMods withMod(PolylineMods pm)
    {
        this.mod = pm;
        return this.mod;
    }

    public PolylineMods withMod()
    {
        return this.withMod(new PolylineMods());
    }


    public void clearMod()
    {
        this.mod = null;
    }


    public abstract void points(double... points);
    public abstract void points(Vec2... points);

    public abstract void offsets(double... offsets);
    public abstract void offsets(Vec2... offsets);

}