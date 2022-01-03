package dev.ramar.e2.rendering;


/*
Interface: TempDrawable
 - A Drawable Sub-type which allows you to
   tell the renderer when you want to stop rendering,
   with a callback for when you actually do stop rendering
*/
public interface TempDrawable extends Drawable
{
    public boolean continueDraw();

    public void onDrawStop();
}