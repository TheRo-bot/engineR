package dev.ramar.e2.rendering.drawing.rect;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

public class Rect implements Drawable
{

    public Rect(double x, double y, double w, double h)
    {
        this(new RectMods());

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rect(RectMods rm)
    {
        this.mod = rm;
    }


    public double x = 0, 
                  y = 0, 
                  w = 0, 
                  h = 0;




    private RectMods mod = null;
    public RectMods getMod()
    {   return this.mod;   }

    public Rect withMod(RectMods rm)
    {
        this.mod = rm;
        return this;
    }

    public Rect withCopyMod(RectMods rm)
    {
        if( this.mod == null )
            this.mod = new RectMods();

        this.mod
            .offset.with(rm.offset.get())
            .colour.with(rm.colour.get())
        ;

        return this;
    }

    

    public void drawAt(double x, double y, ViewPort vp)
    {

    }
}
