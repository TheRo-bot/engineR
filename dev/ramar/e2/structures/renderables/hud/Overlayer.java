package dev.ramar.e2.structures.renderables.hud;

import dev.ramar.e2.backend.renderers.AWTRenderer;

import dev.ramar.e2.interfaces.rendering.Renderable;
import dev.ramar.e2.interfaces.rendering.ViewPort;
import dev.ramar.e2.interfaces.rendering.Sprite;

import dev.ramar.e2.structures.Vec2;
import dev.ramar.e2.structures.Colour;

import java.util.*;

import java.awt.Font;

import java.awt.geom.AffineTransform;

public class Overlayer implements Renderable, OverlayerRendering
{

    private List<HUDElement> elements = java.util.Collections.synchronizedList(new ArrayList<>());

    public Overlayer() 
    {
    }

    
    public void addElement(HUDElement e)
    {
        elements.add(e);
    }


    public void removeElement(HUDElement e)
    {
        elements.remove(e);
    }






    /* Renderable Implementation
    -------------------------------
    */

    private ViewPort vpThisDraw = null;

    public void render(ViewPort vp)
    {
        drawSelf(null, vp);
    }


    private AffineTransform trans = new AffineTransform();

    public void drawSelf(Vec2 pos, ViewPort vp)
    {
        vpwc = vp.getWorldCenter();

        double x = 0.0,
               y = 0.0;

        if( pos != null )
        {
            x += pos.getX();
            y += pos.getY();
        }

        synchronized(elements)
        {
            vpThisDraw = vp;
            Colour c = vpThisDraw.getColour();
            Font f = vpThisDraw.getFont();

            AffineTransform before = ((AWTRenderer)vp).getGraphics().getTransform();
            ((AWTRenderer)vp).getGraphics().setTransform(trans);

            for( HUDElement e : elements )
            {
                e.drawOffset(x, y, this);
            }

            ((AWTRenderer)vp).getGraphics().setTransform(before);
            vpThisDraw.setColour(c);
            vpThisDraw.setFont(f);
            vpThisDraw = null;
        }

    }


    /* OverlayerRendering Implementation
    ---------------------------------------
    */
    private Colour colourToUse = new Colour(255, 255, 255, 255);
    private Font fontToUse = null;
    private Vec2 vpwc = null;


    private void testViewPort()
    {
        if( vpThisDraw == null )
            throw new IllegalStateException("Can only call drawing methods when drawing!");
    }

    public OverlayerRendering setColour(Colour c)
    {
        colourToUse.set(c);
        return this;
    }

    public Colour getColour()
    {
        return colourToUse;
    }


    public void setFont(Font f)
    {
        fontToUse = f;
    }


    public Font getFont()
    {
        return fontToUse;
    }


    public OverlayerRendering drawRect(double x, double y, int width, int height)
    {
        testViewPort();
        vpThisDraw.setColour(colourToUse);
        vpThisDraw.overlayFilledRect(x, y, width, height);
        return this;
    }


    public OverlayerRendering drawRect(double x1, double y1, double x2, double y2)
    {
        testViewPort();
        vpThisDraw.setColour(colourToUse);
        vpThisDraw.overlayFilledRect(x1, y1, x2, y2);
        return this;
    }


    public OverlayerRendering outlineRect(double x, double y, int width, int height)
    {
        testViewPort();
        vpThisDraw.setColour(colourToUse);
        vpThisDraw.overlayRect(x, y, width, height);

        return this;
    }

    public OverlayerRendering drawSprite(double x, double y, Sprite s)
    {
        testViewPort();
        vpThisDraw.setColour(colourToUse);
        vpThisDraw.overlaySprite(s, x - vpwc.getX(), y - vpwc.getY());
        return this;
    }


    public OverlayerRendering drawText(double x, double y, String s)
    {
        testViewPort();
        vpThisDraw.setFont(fontToUse);
        vpThisDraw.setColour(colourToUse);
        vpThisDraw.overlayAbsText(s, x, y);

        return this;
    }




}