package dev.ramar.e2.rendering;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Map;
import java.util.TreeMap;

public abstract class LayerManager<E extends LayerManager.Layer>
{

    protected Map<Integer, E> layers = new TreeMap<>();


    public E get(int zIndex)
    {
        E l = this.layers.get(zIndex);
        if( l == null )
        {
            l = this.createLayer();

            // if layer is ever null, then who the fuck implemented createLayer to be null
            if( l == null )
                throw new NullPointerException("who the fuck implemented createLayer to be null??? excuse me??");

            this.layers.put(zIndex, l);
        }

        return l;
    }

    public void add(Drawable d)
    {
        this.addTo(0, d);
    }


    public void addTo(int zIndex, Drawable d)
    {
        E layer = this.get(zIndex);

        if( layer != null )
            layer.add(d);
    }


    public boolean remove(Drawable d)
    {
        for( E layer : this.layers.values() )
            if( layer.remove(d) )
                return true;

        return false;
    }

    public void removeAll(Drawable d)
    {
        for( E layer : this.layers.values() )
            layer.remove(d);
    }

    public void clear()
    {
        for( E layer : this.layers.values() )
        {
            layer.clear();
        }
        this.layers.clear();
    }

    public void clearLayer(int zIndex)
    {
        E layer = this.layers.get(zIndex);
        if( layer != null )
        {
            layer.clear();
        }
    }

    protected abstract E createLayer();


    public static class Layer
    {
        protected List<Drawable> drawables = new LinkedList<>();

        public void add(Drawable draw)
        {
            if( draw != null )
            {
                int zIndex = draw.getZIndex();
                synchronized(this.drawables)
                {
                    int index = 0;
                    for( Drawable d : this.drawables )
                    {
                        if( zIndex > d.getZIndex() )
                            break;
                        index++;
                    }

                    this.drawables.add(index, draw);
                }
            }
        }

        public boolean has(Drawable draw)
        {
            synchronized(this.drawables)
            {
                return this.drawables.contains(draw);
            }
        }

        public boolean remove(Drawable draw)
        {
            synchronized(this.drawables)
            {
                return this.drawables.remove(draw);
            }
        }

        public void clear()
        {
            synchronized(this.drawables)
            {
                this.drawables.clear();
            }
        }
    }
}