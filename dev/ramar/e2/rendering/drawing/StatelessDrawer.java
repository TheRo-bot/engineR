package dev.ramar.e2.rendering.drawing;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer;
import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.DrawManager;
import java.util.*;

public abstract class StatelessDrawer
{
    public final RectDrawer rect;

    protected final List<Drawable> drawables = new ArrayList<>();

    protected StatelessDrawer(RectDrawer rd)
    {
        rect = rd;
    }


    public void addDrawable(Drawable d)
    {
        drawables.add(d);
    }

    public void removeDrawable(Drawable d)
    {
        drawables.remove(d);
    }


    public void addTempDrawable(Drawable d)
    {
        
    }


    public void drawAt(double x, double y, DrawManager dm)
    {
        for( Drawable d : drawables )
            d.drawAt(x, y, dm);
    }

}