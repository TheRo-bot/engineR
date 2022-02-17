package dev.ramar.e2.demos.bufferedimage;

import java.util.List;
import java.util.ArrayList;

import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.awt.AWTViewPort;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Random;


public class BufferDemo
{
    int w = 1280,
        h =  700;

    List<Drawable> perm = new ArrayList<>();
    List<Drawable>  top = new ArrayList<>();
    double offX = w/2, offY = h/2;

    Random rd = new Random();

    public BufferDemo()
    {
        double desW = w * 0.9, desH = h * 0.9;
        BufferDrawable bd = new BufferDrawable(-desW/2, -desH/2, desW, desH)
        {
            public void draw()
            {
                Graphics2D g2d = getGraphics();
                g2d.setColor(new Color(255, 0, 0, 40));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.getTransform().rotate(90);
                g2d.setColor(new Color(255, 255, 255, 255));
                g2d.fillRect((int)(offX - 5), (int)(offY - 5), 10, 10);
            }
        };

        perm.add(bd);
    }

    public void attach(EngineR2 instance)
    {
        for( Drawable d : perm )
            instance.viewport.draw.stateless.perm.add(d);

        for( Drawable d : top )
            instance.viewport.draw.stateless.top.add(d);
    }

    public void release(EngineR2 instance)
    {
        for( Drawable d : perm )
            instance.viewport.draw.stateless.perm.remove(d);

        for( Drawable d : top )
            instance.viewport.draw.stateless.top.remove(d);
    }


}