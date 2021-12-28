package dev.ramar.e2.rendering.console;

public interface ObjectParser
{
    public Object[] parse(String s);

    public String unparse(Object[] arr);
}