package dev.ramar.e2.rendering.drawing;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.TempDrawable;



import dev.ramar.e2.rendering.drawing.rect.RectDrawer;
import dev.ramar.e2.rendering.drawing.image.ImageDrawer;
import dev.ramar.e2.rendering.drawing.text.TextDrawer;
import dev.ramar.e2.rendering.drawing.line.LineDrawer;

import dev.ramar.e2.rendering.drawing.polygon.PolygonDrawer;
import dev.ramar.e2.rendering.drawing.polyline.PolylineDrawer;

import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.DrawManager;

import dev.ramar.utils.ValuePair;
import dev.ramar.utils.HiddenList;

import java.util.*;


/*
Abstract Class: LayeredDrawer
 - Owned by a DrawManager
 - The class which draws things to a screen on a frame-by-frame basis.
 - things drawn to the screen using this class have no persistence in
   the next frame.
*/
public abstract class LayeredDrawer
{
    public final RectDrawer rect;
    public final ImageDrawer image;
    public final TextDrawer text;
    public final LineDrawer line;
    public final PolygonDrawer polygon;
    public final PolylineDrawer polyline;


    public static class LayeredBuilder
    {
        public LayeredBuilder() {}

        RectDrawer r; 
        public LayeredBuilder withRect(RectDrawer r)
        {
            this.r = r;
            return this;
        }

        ImageDrawer i; 
        public LayeredBuilder withImage(ImageDrawer i)
        {
            this.i = i;
            return this;
        }

        TextDrawer t;
        public LayeredBuilder withText(TextDrawer t)
        {
            this.t = t;
            return this;
        }


        LineDrawer l; 
        public LayeredBuilder withLine(LineDrawer l)
        {
            this.l = l;
            return this;
        }


        PolygonDrawer pg; 
        public LayeredBuilder withPolygon(PolygonDrawer pg)
        {
            this.pg = pg;
            return this;
        }


        PolylineDrawer pl;
        public LayeredBuilder withPolyline(PolylineDrawer pl)
        {
            this.pl = pl;
            return this;
        }
    }


    protected LayeredDrawer(LayeredBuilder sb)
    {
        rect = sb.r;
        image = sb.i;
        text = sb.t;
        line = sb.l;
        polygon = sb.pg;
        polyline = sb.pl;
    }


    public final Layers layers = new Layers();

    public static class Layers
    {
        public final LocalList<Drawable> back = new LocalList<>();
        public final LocalList<Drawable> mid  = new LocalList<>();
        public final LocalList<Drawable> top  = new LocalList<>();
        public final LocalList<TempDrawable> temp = new LocalList<>();

        public class LocalList<E> extends HiddenList<E>
        {
            private List<E> toRemove = new ArrayList<>();
            private List<E> toAdd = new ArrayList<>();

            public void queueRemove(E d)
            {
                synchronized(this)
                {
                    toRemove.add(d);
                }
            }

            public void queueAdd(E d)
            {
                synchronized(this)
                {
                    toAdd.add(d);
                }
            }

            private List<E> getList()
            {
                return list;
            } 

            private List<E> getRemoveQueue()
            {
                return toRemove;
            } 

            private List<E> getAddQueue()
            {
                return toAdd;
            }
        }
    } 

    /* Defined HiddenLists
    -===---------------------
     These extendsions of HiddenList exposes the actual list to
     this file and this file only, which is useful since only this
     file should know about it, but we still want outside users
     to change the state of the class 
    */

    public static class DrawableList extends HiddenList<Drawable>
    {

    }


    public class TempDrawableList extends HiddenList<TempDrawable>
    {
        private List<TempDrawable> getList()
        {
            return list;
        } 
    }


    protected void drawMid(double x, double y, ViewPort vp)
    {
        synchronized(layers.mid)
        {
            for( Drawable d : layers.mid.getAddQueue())
                layers.mid.add(d);

            layers.mid.getAddQueue().clear();
        }

        synchronized(layers.mid)
        {
            for( int ii = 0; ii < layers.mid.getList().size(); ii++ )
                layers.mid.getList().get(ii).drawAt(x, y, vp);
        } 
        
        synchronized(layers.mid)
        {
            for( Drawable d : layers.mid.getRemoveQueue() )
                layers.mid.remove(d);

            layers.mid.getRemoveQueue().clear();
        }
    }

    protected void drawTemp(double x, double y, ViewPort vp)
    {
        synchronized(layers.temp)
        {
            for( int ii = 0; ii < layers.temp.getList().size(); ii++ )
            {
                TempDrawable td = layers.temp.getList().get(ii);

                if( !td.continueDraw() )
                {
                    layers.temp.remove(ii);
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
        synchronized(layers.top)
        {
            for( Drawable d : layers.top.getList() )
                d.drawAt(0, 0, vp);
        }
    }

    public void drawAt(double x, double y, ViewPort vp)
    {
        // drawBack(x, y, vp);
        drawMid(x, y, vp);
        drawTemp(x, y, vp);
        drawTop(x, y, vp);



    }

}