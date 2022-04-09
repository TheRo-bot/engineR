package dev.ramar.e2.rendering.drawing.image;

import dev.ramar.e2.rendering.drawing.Drawer;

import dev.ramar.e2.rendering.Image;

public abstract class ImageDrawer extends Drawer<ImageMods>
{
    public abstract void at(double x, double y, Image i);
}