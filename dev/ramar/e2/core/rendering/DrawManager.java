package dev.ramar.e2.core.rendering;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/

import dev.ramar.e2.core.drawing.rect.RectDrawer;
import dev.ramar.e2.core.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.core.drawing.line.LineDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.rect)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager<E extends RectDrawer, V extends LineDrawer>
{
    protected DrawManager()
    {
        this.rect = this.createRectDrawer();
        this.line = this.createLineDrawer();
    }


    public final E rect;
    public abstract E createRectDrawer();

    public final V line;
    public abstract V createLineDrawer();


}
