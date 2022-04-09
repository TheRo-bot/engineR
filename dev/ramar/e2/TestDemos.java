package dev.ramar.e2;


import dev.ramar.e2.rendering.ViewPort;

import java.util.List;

import java.util.Map;
import java.util.HashMap;

import java.util.Random;

import dev.ramar.e2.rendering.console.parsers.StringSplitter;
import dev.ramar.e2.rendering.console.Command;
import dev.ramar.e2.rendering.console.ObjectParser;
import dev.ramar.e2.rendering.console.ConsoleParser;

import dev.ramar.e2.rendering.Drawable;

public class TestDemos
{

    public interface Demo
    {
        public void start(List<EngineR2> instance);
        public void stop(List<EngineR2> instance);
    }

    List<EngineR2> instances;

    String currDemo = null;

    private Map<String, Demo> demos = new HashMap<>();

    public TestDemos(List<EngineR2> instances)
    {
        this.instances = instances;

        for( EngineR2 instance : instances )
        {

            instance.console.parser.addCommand("demo", new Command()
            {
                private final ObjectParser PARSER = new StringSplitter(" ");

                public Object run(ConsoleParser cp, Object[] args)
                {
                    if( args.length > 1 )
                    {
                        switch((String)args[1])
                        {
                            case "stop":
                                if( currDemo != null )
                                    cp.ps.println("Stopping the " + currDemo + " demo!");   
                                stopDemo();
                                break;
                            case "list":
                                String out = "Available demos:";
                                for( String s : demos.keySet() )
                                {
                                    out += "\n" + s;
                                }
                                cp.ps.println(out);
                                break;
                            default:
                                try
                                {
                                    if( demos.containsKey((String)args[1]) )
                                        cp.ps.println("Starting the " + args[1] + " demo!");
                                    startDemo((String)args[1]);
                                }
                                catch(IllegalArgumentException e)
                                {
                                    cp.ps.println(e.getMessage());
                                }
                        }
                    }
                    return null;
                }

                public ObjectParser getParser()
                {
                    return PARSER;
                }

                public String describeCommand()
                { return "runs demos. available demos: 'combat', 'explore', 'cutscene'. 'stop' to stop the current demo"; }
            });
        }
    }

    public void stopDemo()
    {
        if( demos.get(currDemo) != null )
            demos.get(currDemo).stop(instances);
        currDemo = null;
    }


    public void startDemo(String s)
    {
        Demo d = demos.get(s);
        if( d != null )
        {
            currDemo = s;
            d.start(instances);
        }
        else
            throw new IllegalArgumentException("'" + s + "' is an unknown demo!");
    }
}