package dev.ramar.e2.rendering.console.parsers;

import dev.ramar.e2.rendering.console.ObjectParser;


public class SingleEntryParser implements ObjectParser
{
    public Object[] parse(String s)
    {
        return new Object[]{s};
    }
}