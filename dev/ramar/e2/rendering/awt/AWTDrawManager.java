package dev.ramar.e2.rendering.awt;

import dev.ramar.e2.rendering.*;
import dev.ramar.e2.rendering.awt.*;
import dev.ramar.e2.rendering.awt.drawing.stateless.AWTStatelessDrawer;
import dev.ramar.e2.rendering.awt.drawing.stateful.AWTStatefulDrawer;

import dev.ramar.e2.rendering.awt.drawing.rect.AWTRectDrawer;
public class AWTDrawManager extends DrawManager
{
    private AWTViewPort vp;

    public AWTDrawManager()
    {
        super(DrawManager.init()
            .withRect(new AWTRectDrawer())
        );
    }

    public AWTDrawManager withViewPort(AWTViewPort vp)
    {
        if( this.vp == null )
        {
            this.vp = vp;
            ((AWTRectDrawer)this.rect).withViewPort(vp);
        }

        return this;
    }


}