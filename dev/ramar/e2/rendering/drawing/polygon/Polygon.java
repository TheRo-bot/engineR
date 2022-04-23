package dev.ramar.e2.rendering.drawing.polygon;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.drawing.poly_helpers.*;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.utils.HiddenList;


import java.util.List;
import java.util.ArrayList;
/*
Drawable: Polygon
 - Represents a gosh darn Polygon!
*/
public class Polygon implements Drawable
{

    public Polygon()  
    {
        this.points = new DoublePolyPoints();
    }

    protected Polygon(PolyPoints override)
    {
        if( override == null )
            this.points = new DoublePolyPoints();
        else
            this.points = override;
    }

    protected PolygonMods mod = new PolygonMods();
    public PolygonMods getMod()
    {   return this.mod;   }



    public final PolyPoints points;

    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.polygon.withMod(this.mod);

        this.points.drawClosed(x, y, vp);

        vp.draw.polygon.clearMod();
    }


}   