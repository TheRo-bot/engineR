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
    // protected final List<Drawable> drawables = new ArrayList<>();

    protected StatelessDrawer(RectDrawer rd, ImageDrawer id, TextDrawer td)
    {
        rect = rd;
        image = id;
        text = td;
    }


    public final DrawableList     perm = new DrawableList();
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
        private List<Drawable> getList()
        {
            return list;
        } 
    }


    public class TempDrawableList extends HiddenList<TempDrawable>
    {
        private List<TempDrawable> getList()
        {
            return list;
        } 
    }




    public void drawAt(double x, double y, ViewPort vp)
    {
        synchronized(perm)
        {
            for( Drawable d : perm.getList() )
                d.drawAt(x, y, vp);
        }

        int beforeSize = temp.getList().size();
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

        if( beforeSize != temp.getList().size() )
            System.out.println(beforeSize + " -> " + temp.getList().size());
    }

}