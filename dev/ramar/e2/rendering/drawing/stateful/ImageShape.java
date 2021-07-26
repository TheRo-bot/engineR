package dev.ramar.e2.rendering.drawing.stateful;

import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.stateless.ImageDrawer.ImageMods;



public class ImageShape extends Shape
{
    private Image image;
    private ImageMods mod = new ImageMods().withDelete(false);

    public ImageShape(Image i)
    {
        setImage(i);
    }


    public void setImage(Image i)
    {
        image = i;
    }

    public Image getImage()
    {
        return image;
    }


    public ImageMods getMod()
    {
        return mod;
    }

    public void setMod(ImageMods im)
    {
        mod = im;
    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        vp.draw.stateless.image.withTempMod(mod);
        
        vp.draw.stateless.image.pos_c(x, y, image);

        vp.draw.stateless.image.clearTempMod();
    }

}