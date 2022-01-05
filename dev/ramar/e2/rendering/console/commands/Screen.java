package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ConsoleParser;
import dev.ramar.e2.rendering.console.ObjectParser;

import dev.ramar.e2.rendering.console.parsers.*;


import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Window.FullscreenState;

public class Screen implements Command
{
    private static final ObjectParser PARSER = new StringSplitter(" ");



    public Object run(ConsoleParser cp, Object[] args)
    {
        if( args.length > 1 )
        {
            switch((String)args[1])
            {
                case "list":
                    cp.ps.println("list                                 | lists all commands");
                    cp.ps.println("res <w> <h>                          | changes the viewport's resolution to <w> pixels by <h> pixels");
                    cp.ps.println("size <w> <h>                         | changes the window's size to <w> units by <h> units");
                    cp.ps.println("set (fullscreen|borderless|windowed) | sets the screen type. ");
                    cp.ps.println("** NOTE: use 'show' to get current res/size/set");
                    break;
                case "res":
                    resolution(cp, args);
                    break;

                case "size":
                    size(cp, args);
                    break; 

                case "set":
                    set(cp, args);
                    break;
            }
        }
        return null;
    }  

    private void resolution(ConsoleParser cp, Object[] args)
    {
        if( args.length > 2 )
        {
            if( ((String)args[2]).equals("show") )
            {
                cp.ps.println("Current screen resolution: " + 
                    cp.console.engine.viewport.getLogicalWidth() + "x" +
                    cp.console.engine.viewport.getLogicalHeight());
            }
            else if( args.length > 3 )
            {
                String sW = (String)args[2];
                String sH = (String)args[3];
                cp.ps.println("coverting '" + sW + "' and '" + sH + "'");
                try
                {
                    int w = Integer.parseInt(sW);
                    try
                    {
                        int h = Integer.parseInt(sH);
                        cp.console.engine.viewport. setLogicalWidth(w);
                        cp.console.engine.viewport.setLogicalHeight(h);
                    }
                    catch(NumberFormatException e)
                    {
                        cp.ps.println("height value is not an integer.");
                    }
                }
                catch(NumberFormatException e) 
                {
                    cp.ps.println("width value is not an integer");
                }

            }
            else
                cp.ps.println("invalid arguments for 'res'. correct arguments: two integers for width and height, or 'show'");

        }
        else
            cp.ps.println("invalid arguments for 'res'. correct arguments: two integers for width and height, or 'show'");
    }


    private void size(ConsoleParser cp, Object[] args)
    {
        if( args.length > 2 )
        {
            if( ((String)args[2]).equals("show") )
            {
                cp.ps.println("Current screen size: " + 
                    cp.console.engine.viewport.window. width() + "x" +
                    cp.console.engine.viewport.window.height());
            }
            else if( args.length > 3 )
            {
                String sW = (String)args[2];
                String sH = (String)args[3];
                cp.ps.println("coverting '" + sW + "' and '" + sH + "'");
                try
                {
                    int w = Integer.parseInt(sW);
                    try
                    {
                        int h = Integer.parseInt(sH);
                        cp.console.engine.viewport.window.resize(w, h);
                    }
                    catch(NumberFormatException e)
                    {
                        cp.ps.println("height value is not an integer.");
                    }
                }
                catch(NumberFormatException e) 
                {
                    cp.ps.println("width value is not an integer");
                }
            }
            else
                cp.ps.println("invalid arguments for 'size'. correct arguments: two integers for width and height, or 'show'");
        }
        else
            cp.ps.println("invalid arguments for 'size'. correct arguments: two integers for width and height, or 'show'");
    }


    private void set(ConsoleParser cp, Object[] args)
    {
        if( args.length > 2 )
        {
            String s = (String)args[2];
            FullscreenState selected = null;
            switch(s)
            {
                case "show":
                    cp.ps.println("Current screen type: " + cp.console.engine.viewport.window.getFullScreenState().getName());
                    break;
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
            {
                EngineR2 er = cp.console.engine;
                System.out.println("viewport: " + er.viewport);
                er.viewport.window.setFullscreenState(selected);
            }
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