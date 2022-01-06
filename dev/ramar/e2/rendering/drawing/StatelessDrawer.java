package dev.ramar.e2.rendering.drawing;

import dev.ramar.e2.rendering.drawing.stateless.*;
import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.TempDrawable;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.utils.ValuePair;
import dev.ramar.utils.HiddenList;

import java.util.*;


/*
Abstract Class: StatelessDrawer
 - Owned by a DrawManager
 - The class which draws things to a screen on a frame-by-frame basis.
 - things drawn to the screen using this class have no persistence in
   the next frame.
*/
public abstract class StatelessDrawer
{
    public final RectDrawer rect;
    public final ImageDrawer image;
    public final TextDrawer text;
    public final LineDrawer line;

    // protected final List<Drawable> drawables = new ArrayList<>();

    protected StatelessDrawer(RectDrawer rd, ImageDrawer id, TextDrawer td, LineDrawer ld)
    {
        rect = rd;
        image = id;
        text = td;
        line = ld;
    }


    public final DrawableList     perm = new DrawableList();
    public final DrawableList     top = new DrawableList();
    public final TempDrawableList temp = new TempDrawableList();

    /* Defined HiddenLists
    -===---------------------
     These extendsions of HiddenList exposes the actual list to
     this file and this file only, which is useful since only this
     file should know about it, but we still want outside users
     to change the state of the class 
    */

    public class DrawableList extends HiddenList<Drawable>
    {
        private List<Drawable> toRemove = new ArrayList<>();

        public void queueRemove(Drawable d)
        {
            synchronized(toRemove)
            {
                toRemove.add(d);
            }
        }

        private List<Drawable> getList()
        {
            return list;
        } 

        private List<Drawable> getRemoveQueue()
        {
            return toRemove;
        } 
    }


    public class TempDrawableList extends HiddenList<TempDrawable>
    {
        private List<TempDrawable> getList()
        {
            return list;
        } 
    }


    protected void drawPerm(double x, double y, ViewPort vp)
    {
        synchronized(perm.getList())
        {
            for( int ii = 0; ii < perm.getList().size(); ii++ )
                perm.getList().get(ii).drawAt(x, y, vp);
        } 
        
        for( Drawable d : perm.getRemoveQueue() )
            perm.remove(d);

        perm.getRemoveQueue().clear();
    }

    protected void drawTemp(double x, double y, ViewPort vp)
    {
        synchronized(temp)
        {
            for( int ii = 0; ii < temp.getList().size(); ii++ )
            {
                TempDrawable td = temp.getList().get(ii);

                if( !td.continueDraw() )
                {
                    temp.remove(ii);
                    ii -= 1;
                    td.onDrawStop();
                }
                else
                    td.drawAt(x, y, vp);
            }
        }
    }


    protected void drawTop(double x, double y, ViewPort vp)
    {
        synchronized(top)
        {
            for( Drawable d : top.getList() )
                d.drawAt(0, 0, vp);
        }
    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        drawPerm(x, y, vp);
        drawTemp(x, y, vp);
        drawTop(x, y, vp);




    }

}