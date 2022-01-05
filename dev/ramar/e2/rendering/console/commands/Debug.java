package dev.ramar.e2.rendering.console.commands;

import java.util.Map;
import java.util.HashMap;

import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ConsoleParser;
import dev.ramar.e2.rendering.console.ObjectParser;

import dev.ramar.e2.rendering.console.parsers.*;

/*
Command: "debug"
 - Usage:
    "debug x [y]" 
      x = <runtime variable> 
      x = "list" || shows possible options for x
      y: optional arguments to send to x
 - Example:
    "debug console test1"
 - Parser: 
    StringSpaceSplitter
*/
public class Debug implements Command
{
    public static final ObjectParser PARSER = new StringSplitter(" "); 

    private Map<String, Command> subCommands = new HashMap<>();

    public Debug()
    {
        registerCommand("list", new Command()
        {
            public Object run(ConsoleParser cp, Object[] in)
            {
                String msg = "Available debuggers:";
                for( String s : subCommands.keySet() )
                {   
                    msg += "\n" + s;
                }
                cp.ps.println(msg);
                
                return null;
            }

            public ObjectParser getParser() { return null; }

            public String describeCommand() { return "lists available debug commands"; }
        });
    }


    public void registerCommand(String s, Command c) throws IllegalArgumentException
    {
        if( subCommands.containsKey(s) )
            throw new IllegalArgumentException("'" + s + "' already is registered");

        subCommands.put(s, c);
    }

    public Object run(ConsoleParser cp, Object[] args)
    {
        Object out = null;
        if( args.length > 1)
        {
            Command c = subCommands.get(args[1]);

            if( c != null )
                out = c.run(cp, args);
            else
            {
                cp.ps.println("unknown arguments to 'debug' command!\nuse 'debug list' to show all arguments");                
            }
        }

        return out;
    }


    public ObjectParser getParser()
    {
        return PARSER;
    }

    public String describeCommand()
    {    return "A hub command for accessing debugging related utilities";   }


}