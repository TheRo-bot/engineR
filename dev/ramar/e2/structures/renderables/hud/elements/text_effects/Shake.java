package dev.ramar.e2.structures.renderables.hud.elements.text_effects;

import dev.ramar.e2.structures.renderables.hud.elements.TextElement;


import java.util.Random;

public class Shake extends TextEffect
{
    private double timeCounter = 0;
    private double toQuarter = 0;

    private Random rd = new Random();

    public Shake()
    {
        super();
    }

    public Shake(TextElement el)
    {
        super(el);
    }
    

    public Shake(TextElement el, TextEffect te)
    {
        super(el, te);
    }


    private boolean shooketh = false;

    private double offX, offY;


    @Override
    public void modify(double delta)
    {
        timeCounter += delta;
        toQuarter += delta;

        if( timeCounter % 1.0 < 0.25 && timeCounter % 1.0 > 0.22 )
        {
            if( element != null )
            {
                if(! shooketh )
                {
                    int off = 50;
                    offX = rd.nextInt(off) - off/2;
                    offY = rd.nextInt(off) - off/2;

                    element.getPos().add(offX, offY);
                    shooketh = true;
                }
                else
                {
                    element.getPos().take(offX, offY);
                    shooketh = false;
                }
            }

            toQuarter = 0;
        }


        // goes to next if next isn't null
        super.modify(delta);
    }
}