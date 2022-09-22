package dev.ramar.e2.rendering;


import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.DrawManager;
import dev.ramar.e2.rendering.LayerManager;


public class AWTViewport extends Viewport<AWTLayerManager, AWTDrawManager>
{

	public void drawTo(Graphics2D g2d)
	{
		if( g2d != null )
		{
            double x = this.getCenterX(),
                   y = this.getCenterY();

            this.draw.setGraphics(g2d);
            this.layers.drawAt(x, y, this);
            this.draw.setGraphics(null);
		}
	}


    /* Viewport Implementation
    --===------------------------
    */

    protected AWTLayerManager createLayerManager()
    {
        return new AWTLayerManager();
    }

    protected AWTDrawManager createDrawManager()
    {
        return new AWTDrawManager();
    }
}