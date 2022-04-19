package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.drawing.StatelessDrawer;
import dev.ramar.e2.rendering.drawing.StatefulDrawer;

import dev.ramar.e2.rendering.drawing.rect.RectDrawer;

/*
Abstract Class: DrawManager
 - Owned by a ViewPort
 - The handler for drawing, mainly here for phrasing draw calls
   (so instead of going myViewport.stateless... you go myViewport.draw.stateless)
   which essentially allows further methods to be less word-y 
*/
public abstract class DrawManager
{

    public static DrawManagerBuilder init()
    {
        return new DrawManagerBuilder();
    }

    public final RectDrawer rect;

    protected DrawManager(DrawManagerBuilder dmb)
    {
        this.rect = dmb.rect;
    }



    protected static class DrawManagerBuilder
    {
        protected DrawManagerBuilder()
        {

        }

        public RectDrawer rect = null;
        public DrawManagerBuilder withRect(RectDrawer rd)
        {
            this.rect = rd;
            return this;
        }


    }

}





