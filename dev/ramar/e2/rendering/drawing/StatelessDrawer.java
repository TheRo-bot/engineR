package dev.ramar.e2.rendering.drawing;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer;
import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.TempDrawable;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.utils.ValuePair;

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

    // protected final List<Drawable> drawables = new ArrayList<>();

    protected final List<TempDrawable> temps = new ArrayList();


    protected StatelessDrawer(RectDrawer rd)
    {
        rect = rd;
    }


    public final HiddenList<Drawable>     perm = new HiddenList<>();
    public final HiddenList<TempDrawable> temp = new HiddenList<>();

    public class HiddenList<E>
    {
        private final List<E> list = new ArrayList<>();

        public void add(E e)
        {
            synchronized(this)
            {
                list.add(e);
            }
        }

        public void remove(E e)
        {
            synchronized(this)
            {
                list.remove(e);
            }
        }

        public void remove(int i)
        {
            synchronized(this)
            {
                list.remove(i);
            }
        }

    }


    public class Temps
    {

    }



    public void drawAt(double x, double y, ViewPort vp)
    {
        synchronized(perm)
        {
            for( Drawable d : perm.list )
                d.drawAt(x, y, vp);
        }

        int beforeSize = temp.list.size();
        synchronized(temp)
        {
            for( int ii = 0; ii < temp.list.size(); ii++ )
            {
                TempDrawable td = temp.list.get(ii);

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

        if( beforeSize != temp.list.size() )
            System.out.println(beforeSize + " -> " + temp.list.size());
    }

}