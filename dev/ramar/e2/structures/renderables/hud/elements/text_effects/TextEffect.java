package dev.ramar.e2.structures.renderables.hud.elements.text_effects;

import dev.ramar.e2.structures.renderables.hud.elements.TextElement;

public abstract class TextEffect
{
    private TextEffect next;

    protected TextElement element;

    public TextEffect()
    {

    }


    public TextEffect(TextElement el)
    {
        element = el;
    }


    public TextEffect(TextElement el, TextEffect te)
    {
        this(el);
        next = te;

        if( next != null )
            next.setElement(el);
    }


    public void setElement(TextElement te)
    {
        element = te;
        if( next != null )
            next.setElement(te);

    }



    public void addEffect(TextEffect eff)
    {
        if( next == null )
            next = eff;
        else
            next.addEffect(eff);
    }

    
    public void modify(double delta)
    {
        if( next != null )
            next.modify(delta);

    }
}