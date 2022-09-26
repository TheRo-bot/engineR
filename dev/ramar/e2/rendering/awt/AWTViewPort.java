package dev.ramar.e2.rendering.awt;


import dev.ramar.e2.rendering.Viewport;
import dev.ramar.e2.rendering.DrawManager;
import dev.ramar.e2.rendering.LayerManager;

import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class AWTViewport extends Viewport<AWTLayerManager, AWTDrawManager>
{

    public AWTViewport(AWTWindow window)
    {
        this.window = window;
    }

    public AWTWindow window;


	public void drawTo(Graphics2D g2d)
	{
		if( g2d != null )
		{
            this.draw.setGraphics(g2d);

            double x = 0,
                   y = 0;


            this.draw.setGraphics(g2d);

            this.draw.rect.withMod()
                .colour.with(0, 0, 0, 255)
                .fill.with()
            ;

            this.draw.rect.poslen(0, 0, 
                this.window.getResolutionW(),
                this.window.getResolutionH()
            );

            AffineTransform vpTransform = new AffineTransform();
            vpTransform.translate(this.window.getResolutionW() * 0.5, this.window.getResolutionH() * 0.5);
            
            vpTransform.translate(this.getCenterX(), this.getCenterY());

            AffineTransform vp = g2d.getTransform();
            vp.concatenate(vpTransform);
            g2d.setTransform(vp);

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