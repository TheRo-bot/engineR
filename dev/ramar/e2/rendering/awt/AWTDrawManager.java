package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.awt.drawing.AWTLayeredDrawer;

import dev.ramar.e2.rendering.DrawManager;

public class AWTDrawManager extends DrawManager
{
    private AWTViewPort vp;

    public AWTDrawManager()
    {
        super(new AWTLayeredDrawer(), new AWTImageCache());
    }

    public void withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
            ((AWTLayeredDrawer)layered).withViewPort(vp);
        }
    }


}