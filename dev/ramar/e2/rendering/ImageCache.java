package dev.ramar.e2.rendering;

import java.util.Map;
import java.util.HashMap;

import java.io.*;

public abstract class ImageCache
{
    private Map<String, Image> cache = new HashMap<>();

    public abstract Image load(Class c, String resPath) throws IOException;


    public String loadToCache(Class c, String resPath, String name) throws IOException
    {
        Image i = load(c, resPath);
        return cache(i, name);
    }


    public String cache(Image im, String name)
    {
        synchronized(cache)
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
    }

    public Image uncache(String name)
    {
        synchronized(cache)
        {
            return cache.remove(name);
        }
    }

    public boolean isCached(String name)
    {
        synchronized(cache)
        {
            return cache.containsKey(name);
        }
    }


    public Image get(String name)
    {
        return cache.get(name);
    }

    public String getNameOf(Image i)
    {
        synchronized(cache)
        {
            for( String s : cache.keySet() )
            {
                if( cache.get(s).equals(i) )
                    return s;
            }
        }

        return null;
    }

    public Map<String, Image> getCacheCopy()
    {
        Map<String, Image> exp = new HashMap<>();
        for( String s : cache.keySet() )
            exp.put(s, cache.get(s));
        
        return exp;
    }

}