package dev.ramar.e2.rendering.awt;


import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ImageCache;

import javax.imageio.ImageIO;
import java.io.File;

public class AWTImageCache extends ImageCache
{



    @Override
    public Image load(File file)
    {
        return new AWTImage(file);
    }
}