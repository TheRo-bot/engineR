package dev.ramar.e2.rendering;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/

import dev.ramar.e2.rendering.drawing.rect.RectDrawer;
import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.rendering.drawing.polyline.PolylineDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.rect)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager<E extends RectDrawer, K extends PolygonDrawer, V extends PolylineDrawer>
{
    protected DrawManager()
    {
        this.rect = this.createRectDrawer();
        this.polygon = this.createPolygonDrawer();
        this.polyline = this.createPolylineDrawer();
    }


    public final E rect;
    public abstract E createRectDrawer();

    public final K polygon;
    public abstract K createPolygonDrawer();

    public final V polyline;
    public abstract V createPolylineDrawer();


}
