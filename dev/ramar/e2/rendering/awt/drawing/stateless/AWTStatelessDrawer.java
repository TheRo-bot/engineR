package dev.ramar.e2.rendering.awt.drawing.stateless;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;

import dev.ramar.e2.rendering.awt.drawing.stateless.AWTRectDrawer;

import dev.ramar.e2.rendering.awt.AWTViewPort;

import java.util.*;

public class AWTStatelessDrawer extends StatelessDrawer
{

    private AWTViewPort vp;

    public AWTStatelessDrawer()
    {
        super(new AWTRectDrawer());
    }


    public void withViewPort(AWTViewPort avp)
    {
        if( vp == null )
        {
            vp = avp;
            ((AWTRectDrawer)rect).withViewPort(vp);
        }
    }
}