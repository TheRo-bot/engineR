package dev.ramar.e2.rendering.awt;


import dev.ramar.e2.rendering.Image;
import dev.ramar.e2.rendering.ImageCache;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;


public class AWTImageCache extends ImageCache
{
    @Override
    public Image load(Class c, String resPath) throws IOException
    {
        if( c == null || resPath == null )
            throw new NullPointerException();

        InputStream is = c.getResourceAsStream(resPath);
        if( is == null )
            throw new IOException("Cannot access '" + resPath + "' from class '" + c.getResource("") + "'");

        BufferedImage bi = ImageIO.read(is);
        return new AWTImage(bi);
    }


}