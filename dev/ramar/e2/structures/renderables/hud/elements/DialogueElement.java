package dev.ramar.e2.structures.renderables.hud.elements;

import dev.ramar.e2.structures.renderables.hud.HUDElement;
import dev.ramar.e2.structures.renderables.hud.OverlayerRendering;

import dev.ramar.e2.structures.Colour;


import java.awt.Font;

public class DialogueElement extends HUDElement
{

    private String text;
    private Font font;
    private Colour colour;


    public DialogueElement(String s, double x, double y, double dimX, double dimY)
    {
        super(x, y, dimX, dimY);
        text = s;
    }


    public DialogueElement(String s, double x, double y, double dimX, double dimY, Colour c)
    {
        super(x, y, dimX, dimY, c);
        text = s;
    }


    public void setFont(Font f)
    {
        font = f;
    }


    public void setColour(Colour c)
    {
        colour = c;
    }



    @Override
    public void drawOffset(double xO, double yO, OverlayerRendering o)
    {

        double x = pos.getX() + xO,
               y = pos.getY() + yO;

        // x, y represent the center position of where we need to render


        // modify x, y based on our Alignment

        // x = convertWithHoriAlign(x);
        // y = convertWithVertAlign(y);

        Colour oldC = o.getColour();

        o.setColour(new Colour(0, 0, 0, 255));

        o.drawRect(x, y, (int)dimX, (int)dimY);

        o.setColour(colour);

        o.drawText(x + 5, y + dimY/2 + 15, text);

        o.setColour(oldC);

    }



}