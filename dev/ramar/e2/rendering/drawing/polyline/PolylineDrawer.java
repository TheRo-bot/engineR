package dev.ramar.e2.rendering.drawing.polyline;


public class PolylineDrawer
{


    public PolylineDrawer()
    {

    }

    private PolylineMods mods = null;
    public PolylineMods getMods()
    {   return this.mods;   }

    public PolylineMods withMod(PolylineMods pm)
    {
        this.currMod = pm;
        return this.currMod;
    }

    public PolylineMods withMod()
    {
        return this.withMod(new PolylineMods());
    }


    public void clearMod()
    {
        this.currMod = null;
    }

}