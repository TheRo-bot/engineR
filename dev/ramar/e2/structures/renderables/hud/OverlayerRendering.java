package dev.ramar.e2.structures.renderables.hud;

import dev.ramar.e2.structures.Colour;

import dev.ramar.e2.interfaces.rendering.Sprite;


import java.awt.Font;

public interface OverlayerRendering
{

    public OverlayerRendering setColour(Colour c);

    public Colour getColour();

    public void setFont(Font f);

    public Font getFont();

    public OverlayerRendering drawRect(double x, double y, int width, int height);


    public OverlayerRendering drawRect(double x1, double y1, double x2, double y2);
    
    public OverlayerRendering outlineRect(double x, double y, int width, int height);

    public OverlayerRendering drawSprite(double x, double y, Sprite s);

    public OverlayerRendering drawText(double x, double y, String s);


}