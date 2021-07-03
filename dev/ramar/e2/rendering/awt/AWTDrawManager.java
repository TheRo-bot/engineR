package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.awt.*;
import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;

public class AWTDrawManager extends DrawManager
{
    private AWTViewPort vp;

    public AWTDrawManager()
    {
        super(new AWTStatelessDrawer());
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