package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.awt.*;
import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;
import dev.ramar.e2.rendering.awt.drawing.stateful.AWTStatefulDrawer;


public class AWTDrawManager extends DrawManager
{
    private AWTViewPort vp;

    public AWTDrawManager()
    {
        super(new AWTStatelessDrawer(), new AWTStatefulDrawer(), 
              new AWTImageCache());
    }

    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
            ((AWTStatelessDrawer)stateless).withViewPort(vp);
        }
    }


}