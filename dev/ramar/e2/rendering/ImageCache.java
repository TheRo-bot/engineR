package dev.ramar.e2.rendering;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

public abstract class ImageCache
{
    private Map<String, Image> cache = new HashMap<>();

    public abstract Image load(File uri);

    public String cache(Image im, String name)
    {
        String failReason = null;

        if( cache.containsKey(name) )
            failReason = "'" + name + "' already cached";
        else if( im == null )
            failReason = "provided image is null";
        else
            cache.put(name, im);

        return failReason;
    }

    public Image uncache(String name)
    {
        return cache.remove(name);
    }


    public Image get(String name)
    {
        return cache.get(name);
    }

}