package dev.ramar.e2.rendering.console.parsers;

import dev.ramar.e2.rendering.console.ObjectParser;


public class StringSplitter implements ObjectParser
{
    private String regex = null;

    public StringSplitter(String regex)
    {
        if( regex == null ) 
            throw new NullPointerException();
        
        this.regex = regex; 
    }

    public Object[] parse(String s)
    {
        return s.split(regex);
    }

}