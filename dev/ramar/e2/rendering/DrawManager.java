package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;
import dev.ramar.e2.rendering.drawing.StatefulDrawer;

import dev.ramar.e2.rendering.drawing.rect.RectDrawer;
import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.rendering.drawing.polyline.PolylineDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager
{

    public static DrawManagerBuilder init()
    {
        return new DrawManagerBuilder();
    }

    public final RectDrawer rect;
    public final PolygonDrawer polygon;
    public final PolylineDrawer polyline;

    protected DrawManager(DrawManagerBuilder dmb)
    {
        this.rect = dmb.rect;
        this.polygon = dmb.polygon;
        this.polyline = dmb.polyline;
    }



    protected static class DrawManagerBuilder
    {
        protected DrawManagerBuilder()
        {

        }

        public RectDrawer rect = null;
        public DrawManagerBuilder withRect(RectDrawer rd)
        {
            this.rect = rd;
            return this;
        }

        public PolygonDrawer polygon = null;
        public DrawManagerBuilder withPolygon(PolygonDrawer pgd)
        {
            this.polygon = pgd;
            return this;
        }        

        public PolylineDrawer polyline = null;
        public DrawManagerBuilder withPolyline(PolylineDrawer pld)
        {
            this.polyline = pld;
            return this;
        }        
    }

}





