package dev.ramar.e2.rendering.console;

public interface Command
{
    public Object run(ConsoleParser cp, Object[] args);

    public ObjectParser getParser();
}

