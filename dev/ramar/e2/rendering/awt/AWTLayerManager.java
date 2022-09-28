package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.LayerManager;
import dev.ramar.e2.rendering.LayerManager.Layer;

public class AWTLayerManager extends LayerManager<AWTLayerManager.AWTLayer>
{

    public AWTLayerManager.AWTLayer createLayer()
    {
        return new AWTLayerManager.AWTLayer();
    }



    public void drawAt(double x, double y, AWTViewport vp)
    {
        synchronized(this.layers)
        {
            for( AWTLayer l : this.layers.values() )
                l.drawAt(x, y, vp);
        }
    }


    public class AWTLayer extends Layer
    {
        private boolean ignoreOffset = false;
        public void ignoreOffset() {  this.ignoreOffset = true;  } 
        public void considerOffset() {  this.ignoreOffset = false;  } 

        public void drawAt(double x, double y, AWTViewport vp)
        {
            synchronized(this.drawables)
            {
                for( Drawable d : this.drawables )
                {
                    if( ignoreOffset )
                        d.drawAt(0, 0, vp);
                    else
                        d.drawAt(x, y, vp);
                }
            }
        }
    }
}