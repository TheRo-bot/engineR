package dev.ramar.e2.core.drawing.image;

import dev.ramar.e2.core.drawing.Drawer;


public abstract class ImageDrawer extends Drawer<ImageMods>
{
    public ImageMods withMod()
    {  return this.withMod(new ImageMods()); }
}
