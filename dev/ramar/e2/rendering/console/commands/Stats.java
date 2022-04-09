package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.rendering.awt.AWTImage;
import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.console.*;
import dev.ramar.e2.rendering.console.parsers.StringSplitter;


import dev.ramar.e2.rendering.drawing.polygon.Polygon;
import dev.ramar.e2.rendering.drawing.text.Text;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.ViewPort.ViewPortFrameListener;

import dev.ramar.utils.PairedValues;

import java.util.Arrays;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Stats implements Command
{
    public static final ObjectParser PARSER = new StringSplitter(" "); 

    /*
    SubCommand: list
     - lists all commands Stats has
    */
    SubCommand list = (ConsoleParser cp, Object... args) ->
    {
        String name = "<..>";
        if( args.length > 0 && args[0] instanceof String)
            name = (String)args[0];

        cp.ps.println(name +" show <..> || shows a given counter on the screen");
        cp.ps.println(name +" hide <..> || hides a given counter on the screen");
        cp.ps.println(name +" edit <..> || edits a given counter");
    };


    /*
    SubCommand: show
     - shows certain graphs
    */
    SubCommand show = new SubCommand()
    {
        /*
        SubCommand: show list
         - Lists options
        */
        SubCommand list = (ConsoleParser cp, Object... args) ->
        {
            cp.ps.println("<..> show mspf    || shows the mspf graph");
            cp.ps.println("<..> show fps     || shows the fps graph");
            cp.ps.println("<..> show memory  || shows the memory graph");
        };

        /*
        SubCommand: show mspf
         - shows the mspf Drawable
        */
        SubCommand mspf = (ConsoleParser cp, Object... args) -> 
        {
            Stats.this.er.viewport.draw.layered.layers.top.add(Stats.this.mspf);
        };

        /*
        SubCommand: show fps
         - Shows the fps Drawable
        */
        SubCommand fps = (ConsoleParser cp, Object... args) -> 
        {
            Stats.this.er.viewport.draw.layered.layers.top.add(Stats.this.fps);
        };

        SubCommand memory = (ConsoleParser cp, Object... args) ->
        {
            Stats.this.er.viewport.draw.layered.layers.top.add(Stats.this.memory);
        };

        public void run(ConsoleParser cp, Object... args)
        {
            if( args.length > 1 )
            {
                String message = (String)args[1];
                Object[] next = Arrays.copyOfRange(args, 1, args.length - 1);

                switch(message)
                {
                    case "list":
                        list.run(cp, next);
                        break;
                    case "mspf":
                        mspf.run(cp, next);
                        break;
                    case "fps":
                        fps.run(cp, next);
                        break;
                    case "memory":
                        memory.run(cp, next);
                }
            }
        }
    };

    SubCommand hide = new SubCommand()
    {
        /*
        SubCommand: hide list
         - Lists options 
        */
        SubCommand list = (ConsoleParser cp, Object... args) ->
        {
            cp.ps.println("<..> hide mspf    || hides the mspf graph");
            cp.ps.println("<..> hide fps     || hides the fps graph");
            cp.ps.println("<..> hide memory  || hides the memory graph");
        };

        /*
        SubCommand: hide mspf
         -  Removes the mspf Drawable from vp.draw.layered.layers.top
        */
        SubCommand mspf = (ConsoleParser cp, Object... args) -> 
        {   
            Stats.this.er.viewport.draw.layered.layers.top.remove(Stats.this.mspf);
        };

        /*
        SubCommand: hide fps
         -  Removes the fps Drawable from vp.draw.layered.top
        */
        SubCommand fps = (ConsoleParser cp, Object... args) ->
        {
            Stats.this.er.viewport.draw.layered.layers.top.remove(Stats.this.fps);
        };

        SubCommand memory = (ConsoleParser cp, Object... args) ->
        {
            Stats.this.er.viewport.draw.layered.layers.top.remove(Stats.this.memory);
        };

        public void run(ConsoleParser cp, Object... args)
        {
            if( args.length > 1 && args[0] instanceof String )
            {
                String message = (String)args[1];
                switch(message)
                {
                    case "list":
                        this.list.run(cp, args);
                        break;
                    case "mspf":
                        this.mspf.run(cp, args);
                        break;
                    case "fps":
                        this.fps.run(cp, args);
                        break;
                    case "memory":
                        this.memory.run(cp, args);
                        break;
                }
            }
        }
    };


    /*
    SubCommand: edit
     - Allows modification of graph parameters
    */
    SubCommand edit = new SubCommand()
    {
        /*
        SubCommand: edit list
         - Lists options
        */
        SubCommand list = (ConsoleParser cp, Object... args) ->
        {
            cp.ps.println("<..> edit mspf <..> || edits parameters of the mspf graph");
            cp.ps.println("<..> edit fps  <..> || edits parameters of the fps graph");
        };

        /*
        SubCommand: edit mspf
         - Edits the mspf graph
        */
        SubCommand mspf = (ConsoleParser cp, Object... args) ->
        {
            cp.ps.println("can't edit shit rn!");
        };

        /*
        SubCommand: edit fps
         - Edits the fps graph
        */
        SubCommand fps = (ConsoleParser cp, Object... args) ->
        {
            cp.ps.println("can't edit shit rn!");
        };

        public void run(ConsoleParser cp, Object... args)
        {
            if( args.length > 1 && args[0] instanceof String )
            {
                String message = (String)args[1];
                Object[] next = Arrays.copyOfRange(args, 1, args.length);
                switch(message)
                {
                    case "list":
                        list.run(cp, next);
                        break;
                    case "mspf":
                        mspf.run(cp, next);
                        break;
                    case "fps":
                        fps.run(cp, next);
                        break;
                }
            }
        }
    };

    public Data data = new Data();

    public class Data implements ViewPortFrameListener
    {
        double nspfs[] = new double[100];
        int start = 0;

        private long lastFrameStart = 0;    

        public void frameStarted(long ns)
        {   
            // this.frameStartBuff.add(ns);
            this.lastFrameStart = ns;
        }


        public void frameEnded(long ns)
        {
            nspfs[start] = ns - lastFrameStart;
        }
    }

    EngineR2 er;

    private Vec2 pos = new Vec2(0, 30);

    public Stats(EngineR2 er)
    {
        this.er = er;
        this.er.viewport.frameListeners.add(this.data);
    }


    public Object run(ConsoleParser cp, Object[] args)
    {
        if( args.length > 1 )
        {
            String message = (String)args[1];
            Object[] next = Arrays.copyOfRange(args, 1, args.length);

            switch(message)
            {
                case "list":
                    this.list.run(cp, next);
                    break;
                case "show":
                    this.show.run(cp, next);
                    break;
                case "hide":
                    this.hide.run(cp, next);
                    break;
                case "edit":
                    this.edit.run(cp, next);
            }
        }

        return null;
    }


    Drawable mspf = new Drawable()
    {
        private double floor = 0.0,
                       ceil = 30.0;


        Polygon bg = new Polygon();
        Text title = new Text("mspf");
        Polygon graph = new Polygon();

        Text floorText = new Text("0.0");
        Text ceilText = new Text("100.0");

        public void drawAt(double x, double y, ViewPort vp)
        {
            bg.drawAt(x, y, vp);
            title.drawAt(x, y, vp);
            graph.drawAt(x, y, vp);
            floorText.drawAt(x, y, vp);
            ceilText.drawAt(x, y, vp);
        }
    };

    Drawable fps = (double x, double y, ViewPort vp) ->
    {
        vp.draw.layered.rect.withMod()
            .colour.with(50, 50, 50, 255)
            .offset.with(x, y)
            .offset.with(pos.getX(), pos.getY())
            .fill.with()
        ;
        vp.draw.layered.rect.poslen(1, 1, 98, 28);

        vp.draw.layered.rect.getMod()
            .colour.with(255, 255, 255, 255)
            .fill.without()
        ;

        vp.draw.layered.rect.poslen(0, 0, 100, 30);

        vp.draw.layered.rect.clearMod();
    };
    

    Drawable memory = new Drawable()
    {

        private double timer = 0.01;
        private double delta = 0.0;
        private long lastTime = System.currentTimeMillis();

        private List<Long> heaps = new ArrayList<>();

        private double x = 200, 
                       y = 40;

        private double min = 1.0;
        private double max = 1.0;

        public void drawAt(double x, double y, ViewPort vp)
        {
            vp.draw.layered.rect.withMod()
                .colour.with(255, 255, 255, 255)
                .fill.with()
                .offset.with(this.x, this.y)
                .offset.with(x, y)
            ;
            int  width = 100,
                height = 40;
            int border = 1;

            vp.draw.layered.rect.poslen(-width/2 - border, -height/2 - border, width + border * 2, height + border * 2);

            vp.draw.layered.rect.getMod()
                .colour.with(50, 50, 50, 255)
            ;
            vp.draw.layered.rect.poslen(-width/2, -height/2, width, height);

            vp.draw.layered.rect.clearMod();

            vp.draw.layered.text.withMod()
                .colour.with(255, 255, 255, 255)
                .offset.with(this.x, this.y)
                .offset.with(x, y - 5)
            ;
            vp.draw.layered.text.at(0, 0,  "memory");
            vp.draw.layered.text.at(0, 12, "" + this.getHeap() / (1000000));

            vp.draw.layered.text.clearMod();
        }

        private long getHeap()
        {
            return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }

        private void update()
        {
            long currHeap = this.getHeap();
            heaps.add(currHeap);
            if( heaps.size() > 100 )
                heaps.remove(0);
        }
    };

    public ObjectParser getParser()
    {   return this.PARSER; }

    public String describeCommand()
    {   return "returns information about framerate and performance";   }
}