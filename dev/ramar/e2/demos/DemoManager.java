package dev.ramar.e2.demos;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.demos.combat.CombatDemo;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.LinkedList;

public class DemoManager
{

    private DemoManager(Map<String, Demo> demos)
    {
        this.demos = demos;
    }

    String currKey = null;
    private Map<String, Demo> demos;

    private List<EngineR2> instances = new LinkedList<>();


    public boolean hasDemo(String key)
    {
        return this.demos.containsKey(key);
    }

    public Demo getDemo(String key)
    {
        return this.demos.get(key);
    }


    public Demo getCurr()
    {
        return this.demos.get(this.currKey);
    }


    public synchronized void swapToDemo(String key)
    {
        if( this.hasDemo(key) )
        {
            Demo ol = this.getCurr();
            Demo ne = this.demos.get(key);

            System.out.println(ol + ", " + ne);
            if( !ne.isInitialised() )
                ne.initialise();

            for( EngineR2 instance : this.instances )
            {
                if( ol != null )
                    ol.disconnect(instance);

                ne.connect(instance);
            }
            
            this.currKey = key;
        }
    }



    public synchronized void bind(EngineR2 instance)
    {
        this.instances.add(instance);
        Demo curr = this.getCurr();

        if( curr != null )
            curr.connect(instance);
    }


    public interface Demo
    {
        public boolean isInitialised();
        public void initialise();
        public void connect(EngineR2 er);
        public void disconnect(EngineR2 er);
    }


    public static DemoManager build()
    {
        Map<String, Demo> demos = new HashMap<>();

        CombatDemo cd = new CombatDemo();
        cd.initialise();
        demos.put("combat", cd);

        return new DemoManager(demos);
    }
}


