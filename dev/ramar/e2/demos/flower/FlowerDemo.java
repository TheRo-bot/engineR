package dev.ramar.e2.demos.flower;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import java.util.List;
import java.util.ArrayList;

public class FlowerDemo
{


    public FlowerDemo()
    {

    }

    public void setup(EngineR2... ers)
    {
        if( perm.isEmpty() && top.isEmpty() )
            init();

        for( EngineR2 er : ers )
        {
            for( Drawable d : perm )
                er.viewport.draw.stateless.perm.add(d);
            for( Drawable d :  top )
                er.viewport.draw.stateless. top.add(d);
            initialised.add(er);
        }

    }

    public void setdown(EngineR2... ers)
    {   
        if( !perm.isEmpty() || !top.isEmpty())
        {
            for( EngineR2 er : ers )
            {
                for( Drawable d : perm )
                    er.viewport.draw.stateless.perm.remove(d);
                for( Drawable d :  top )
                    er.viewport.draw.stateless. top.remove(d);
                
                initialised.remove(er);
            }
        }
    }

    List<EngineR2> initialised = new ArrayList<>();
    List<Drawable> perm = new ArrayList<>();
    List<Drawable>  top = new ArrayList<>();

    public void init()
    {
        Drawable one = new Drawable()
        {
            public void drawAt(double x, double y, ViewPort vp)
            {
                vp.draw.stateless.rect.withMod()
                    .withColour(255, 255, 255, 255)
                    .withFill()
                    .withOffset(x, y)
                ;
                vp.draw.stateless.rect.poslen(-5, -5, 10, 10);

            }
        };

        perm.add(one);
    }

    
    public void uninit()
    {
        
    }
}