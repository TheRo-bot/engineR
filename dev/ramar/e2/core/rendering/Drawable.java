package dev.ramar.e2.core.rendering;

import java.util.Comparator;

/*
Interface: Drawable
 - The interface a ViewPort uses to draw objects onto the screen
 - essentially, using <ViewPort instance>.draw.stateless.perm.add(<Drawable>)
   will add your implementation into the list of what to draw each frame
*/
public interface Drawable
{
    public static class DrawableComparator implements Comparator<Drawable>
    {
        public int compare(Drawable a, Drawable b)
        {  return a.getZIndex() - b.getZIndex();  }
    }


    public void drawAt(double xOff, double yOff, Viewport vp);


    public default int getZIndex()
    {  return 0;  }
}