package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ConsoleParser;
import dev.ramar.e2.rendering.console.ObjectParser;

import dev.ramar.e2.rendering.console.parsers.*;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Window.FullscreenState;

public class Screen implements Command
{
    private static final ObjectParser PARSER = new StringSplitter(" ");
    private EngineR2 er;

    public Screen(EngineR2 er)
    {
        this.er = er;
    }


    public Object run(ConsoleParser cp, Object[] args)
    {
        if( args.length > 1 )
        {
            switch((String)args[1])
            {
                case "list":
                    cp.ps.println("list                                 | lists all commands");
                    cp.ps.println("resolution <w> <h>                   | changes the viewport's resolution to <w> pixels by <h> pixels");
                    cp.ps.println("size <w> <h>                         | changes the window's size to <w> units by <h> units");
                    cp.ps.println("set (fullscreen|borderless|windowed) | sets the screen type. ");
                    break;
                case "resolution":

                    break;

                case "size":

                    break;

                case "set":
                    set(cp, args);
                    break;
            }
        }
        return null;
    }  

    private void set(ConsoleParser cp, Object[] args)
    {
        if( args.length > 2 )
        {
            String s = (String)args[2];
            FullscreenState selected = null;
            switch(s)
            {
                case "fullscreen":
                    selected = FullscreenState.FULLSCREEN;
                    break;
                case "borderless":
                    selected = FullscreenState.WINDOWED_BORDERLESS;
                    break;

                case "windowed":
                    selected = FullscreenState.WINDOWED;
                    break;

                default:
                    cp.ps.println("invalid argument for set command. valid arguments: 'fullscreen' 'borderless' 'windowed'");
            }

            if( selected != null )
                er.viewport.window.setFullscreenState(selected);
        } 
    }

    public ObjectParser getParser()
    {
        return PARSER;
    }

    public String describeCommand()
    {
        return "Allows control of the screen. use the 'list' command to show actions";
    }
}