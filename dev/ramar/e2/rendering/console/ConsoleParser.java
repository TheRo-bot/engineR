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

        Debug dbg = new Debug();
        cp.addCommand("debug", dbg);
        dbg.registerCommand("keylog", new Keys(c.engine));

        cp.addCommand("screen", new Screen());

        cp.addCommand("list", new Command()
        {
            private final ObjectParser PARSER = new SingleEntryParser();

            public Object[] run(ConsoleParser cp, Object[] args)
            {
                cp.ps.println(cp.commands.keySet());

                for( String s : cp.commands.keySet() )
                {
                    Command c = cp.commands.get(s);
                    System.out.println(s + " || " + c.describeCommand());
                }
                return null;
            }

            public ObjectParser getParser()
            {   return PARSER;   }

            public String describeCommand()
            {
                return "Shows all available commands";
            }
        });

        Command closeCommand = new Command()
        {
            private final ObjectParser PARSER = new SingleEntryParser();

            public Object[] run(ConsoleParser cp, Object[] args)
            {
                System.out.println("AHHHHH");
                cp.console.getViewPortRef().window.close();
                return null;  
            }

            public ObjectParser getParser()
            {   
                return PARSER; }

            public String describeCommand()
            {   return "closes this instance of EngineR2";   }
        };
        cp.addCommand("quit", closeCommand);
        cp.addCommand("exit", closeCommand);

        return cp;
    }



    private final Map<String, Command> commands = new HashMap<>();
    private List<ObjectParser> objectParsers = new ArrayList<>();
    public final PrintStream ps;
    public final Console console;


    public ConsoleParser(Console c)
    {
        this.console = c;
        this.ps = System.out;
    }  


    public ConsoleParser(Console c, PrintStream ps)
    {  
        this.console = c;
        this.ps = ps == null ? System.out : ps;
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
            String command = s;
            if( command.contains(" ") )
                command = s.split(" ")[0];


            Command c = getCommand(command);
            if( c != null )
                c.run(this, c.getParser().parse(s));
            else
                ps.println("Unrecognised command! Try again");
        }
    }
}