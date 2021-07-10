package dev.ramar.e2.rendering;


/*
Interface: Drawable
 - The interface a ViewPort uses to draw objects onto the screen
 - essentially, using <ViewPort instance>.draw.stateless.perm.add(<Drawable>)
   will add your implementation into the list of what to draw each frame
*/
public interface Drawable
{
    public void drawAt(double xOff, double yOff, ViewPort vp);
}