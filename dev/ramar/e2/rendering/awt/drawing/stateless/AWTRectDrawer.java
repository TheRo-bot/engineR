package dev.ramar.e2.rendering.awt.drawing.stateless;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import dev.ramar.e2.rendering.drawing.stateless.RectDrawer;
import dev.ramar.e2.rendering.drawing.stateless.RectDrawer.RectMods;



public class AWTRectDrawer extends RectDrawer
{
    private AWTViewPort vp;


    public AWTRectDrawer()
    {
    }


    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
        }
    }



    @Override
    public void pospos(double x1, double y1, double x2, double y2)
    {
        RectMods rm = getMod();
        if( rm != null )
        {
            x1 = rm.modX(x1);
            y1 = rm.modY(y1);
            x2 = rm.modX(x2);
            y2 = rm.modY(y2);
        }

        System.out.println("pospos [" + x1 + ", " + y1 + "], [" + x2 + ", " + y2 + "]");
    }  


    public void poslen(double x, double y, double w, double h)
    {
        RectMods rm = getMod();
        if( rm != null )
        {
            x = rm.modX(x);
            y = rm.modY(y);
        }

        System.out.println("poslen [" + x + ", " + y + "], [" + (x + w) + ", " + (y + h) + "]");
    }


}