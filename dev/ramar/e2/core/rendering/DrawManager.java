package dev.ramar.e2.core.rendering;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/

import dev.ramar.e2.core.drawing.rect.RectDrawer;
import dev.ramar.e2.core.drawing.polyshape.PolyshapeDrawer;
import dev.ramar.e2.core.drawing.line.LineDrawer;
import dev.ramar.e2.core.drawing.text.TextDrawer;
import dev.ramar.e2.core.drawing.image.ImageDrawer;
import dev.ramar.e2.core.drawing.oval.OvalDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.rect)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager<E extends RectDrawer, K extends LineDrawer, V extends PolyshapeDrawer, 
                                  L extends TextDrawer, B extends ImageDrawer, G extends OvalDrawer>
{
    protected DrawManager()
    {
        this.rect = this.createRectDrawer();
        this.line = this.createLineDrawer();
        this.polyshape = this.createPolyshapeDrawer();
        this.text = this.createTextDrawer();
        this.image = this.createImageDrawer();
        this.oval = this.createOvalDrawer();
    }


    public final E rect;
    public abstract E createRectDrawer();

    public final K line;
    public abstract K createLineDrawer();

    public final V polyshape;
    public abstract V createPolyshapeDrawer();


    public final L text;
    public abstract L createTextDrawer();


    public final B image;
    public abstract B createImageDrawer();


    public final G oval;
    public abstract G createOvalDrawer();
}
