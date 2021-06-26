package dev.ramar.e2.structures.renderables.hud.elements;

import dev.ramar.e2.structures.renderables.hud.HUDElement;
import dev.ramar.e2.structures.renderables.hud.OverlayerRendering;

import dev.ramar.e2.structures.renderables.hud.elements.text_effects.*;

import dev.ramar.e2.structures.Colour;


import java.awt.Font;
import java.awt.FontMetrics;


public class TextElement extends HUDElement
{

    private String text;
    private Font font;
    private FontMetrics metrics;
    private Colour colour;

    private TextEffect effects;

    public TextElement(double x, double y, double dimX, double dimY, String s, Font f)
    {
        super(x, y, dimX, dimY);
        text = s;
        font = f;
    }


    public TextElement(double x, double y, double dimX, double dimY, Colour c, String s, Font f)
    {
        this(x, y, dimX, dimY, s, f);
        colour = c;
    }


    /* Accessors / Mutators
    --------------------------
    */

    public void setMetrics(FontMetrics fm)
    {
        metrics = fm;
    }

    public void addTextEffect(TextEffect eff)
    {
        if( effects == null )
            effects = eff;
        else
            effects.addEffect(eff);

        effects.setElement(this);
    }



    public String getText()
    {
        return text;
    }


    public void setText(String s)
    {
        text = s;
    }


    public Font getFont()
    {
        return font;
    }


    public void setFont(Font f)
    {
        font = f;
    }


    public Colour getColour()
    {
        return colour;
    }


    public void setColour(Colour c)
    {
        colour = c;
    }


    /* Drawing methods
    ---------------------
    */


    private double lastTime = System.currentTimeMillis();
    private double delta = 0;

    @Override
    public void drawOffset(double xO, double yO, OverlayerRendering o)
    {
        double nowTime = System.currentTimeMillis();
        delta += (nowTime - lastTime) / 1000;
        lastTime = nowTime;

        if( effects != null )
            effects.modify(delta);


        if( text != null )
        {
            double x = pos.getX() + xO,
                   y = pos.getY() + yO;

            x = convertWithHoriAlign(x);
            y = convertWithVertAlign(y);

            Colour origC = o.getColour();
            o.setColour(colour);

            if( font != null )
                o.setFont(font);


            // double thisX = pos.getX() + xO,
            //        thisY = pos.getY() + yO;

            // o.outlineRect(thisX - dimX / 2, thisY - dimY / 2, (int)dimX, (int)dimY);

            if( metrics != null )
                y -= metrics.getHeight() /2 ;

            o.drawText(x, y, text);

            o.setColour(origC);
        }
    }


}