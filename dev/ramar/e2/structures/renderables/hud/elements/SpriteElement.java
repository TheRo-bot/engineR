package dev.ramar.e2.structures.renderables.hud.elements;

import dev.ramar.e2.structures.renderables.hud.HUDElement;
import dev.ramar.e2.structures.renderables.hud.OverlayerRendering;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.interfaces.rendering.Sprite;

public class SpriteElement extends HUDElement
{
    private Sprite sprite; 

    public SpriteElement(double x, double y, double dimX, double dimY, Sprite s)
    {
        super(x, y, dimX, dimY);
        sprite = s;
    }


    @Override
    public void drawOffset(double xO, double yO, OverlayerRendering o)
    {
        double x = pos.getX() + xO,
               y = pos.getY() + yO;

        x = convertWithHoriAlign(x);
        y = convertWithVertAlign(y);

        // o.drawSprite()
    }



}