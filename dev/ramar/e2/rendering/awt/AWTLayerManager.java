package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.LayerManager;
import dev.ramar.e2.rendering.LayerManager.Layer;

public class AWTLayerManager extends LayerManager<AWTLayer>
{





    public void drawAt(double x, double y, AWTViewport vp)
    {
        synchronized(this.layers)
        {
            for( AWTLayer l : this.layers )
                l.drawAt(x, y, vp);
        }
    }


    public class AWTLayer extends Layer
    {
        public void drawAt(double x, double y, AWTViewport vp)
        {
            synchronized(this.drawables)
            {
                for( Drawable d : this.drawables )
                    d.drawAt(x, y, vp);
            }
        }
    }
}