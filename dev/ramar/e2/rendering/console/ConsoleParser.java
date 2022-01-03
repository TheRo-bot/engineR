package dev.ramar.e2.rendering.console;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import java.io.PrintStream;

import java.util.Random;

import dev.ramar.e2.rendering.console.commands.*;
import dev.ramar.e2.rendering.console.parsers.*;


public class ConsoleParser
{


    

    public static ConsoleParser createParser(Console c)
    {
        ConsoleParser cp = new ConsoleParser(c);

 
        cp.addCommand("debug", new Debug());


        Command closeCommand = new Command()
        {
            private final ObjectParser PARSER = new SingleEntryParser();

            public Object[] run(ConsoleParser cp, Object[] args)
            {
                cp.console.getViewPortRef().window.close();
                return null;  
            }

            public ObjectParser getParser()
            {   return PARSER; }
        };
        cp.addCommand("quit", closeCommand);
        cp.addCommand("exit", closeCommand);

        return cp;
    }



    private final Map<String, Command> commands = new HashMap<>();
    private List<ObjectParser> objectParsers = new ArrayList<>();
    public final PrintStream ps;
    private Console console;


    public ConsoleParser()
    {
        this.ps = System.out;
    }

    public ConsoleParser(PrintStream ps)
    {  
        this.ps = ps == null ? System.out : ps;
    }

    public ConsoleParser(Console c)
    {
        this(c.out);
        this.console = c;
    }  

    public void addCommand(String s, Command c)
    {
        commands.put(s, c);
    }

    public Command getCommand(String s)
    {
        return commands.get(s);
    }

    public void addParser(ObjectParser op)
    {
        objectParsers.add(op);
    }


    public void parseCommand(String s)
    {
        if( s != null && !s.equals("") )
        {

            String command = s.split(" ")[0];


            Command c = getCommand(command);
            if( c != null )
                c.run(this, c.getParser().parse(s));
            else
                ps.println("Unrecognised command! Try again");
        }
    }
}