package dev.ramar.e2.rendering.console.commands;

import dev.ramar.e2.rendering.awt.AWTImage;
import dev.ramar.e2.EngineR2;
import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.console.*;
import dev.ramar.e2.rendering.console.parsers.StringSplitter;

import dev.ramar.e2.structures.Vec2;

import dev.ramar.e2.rendering.ViewPort.ViewPortFrameListener;

import dev.ramar.utils.PairedValues;

import java.util.Arrays;

import java.util.List;
import java.util.LinkedList;

public class FPS implements Command
{
    public static final ObjectParser PARSER = new StringSplitter(" "); 

    /*
    SubCommand: list
     - lists all commands FPS has
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
            cp.ps.println("<..> show mspf || shows the mspf graph");
            cp.ps.println("<..> show fps  || shows the fps graph");
        };

        /*
        SubCommand: show mspf
         - shows the mspf Drawable
        */
        SubCommand mspf = (ConsoleParser cp, Object... args) -> 
        {
            FPS.this.er.viewport.draw.stateless.top.add(FPS.this.mspf);
        };

        /*
        SubCommand: show fps
         - Shows the fps Drawable
        */
        SubCommand fps = (ConsoleParser cp, Object... args) -> 
        {
            FPS.this.er.viewport.draw.stateless.top.add(FPS.this.fps);
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
            cp.ps.println("<..> hide mspf || hides the mspf graph");
            cp.ps.println("<..> hide fps  || hides the fps graph");
        };

        /*
        SubCommand: hide mspf
         -  Removes the mspf Drawable from vp.draw.stateless.top
        */
        SubCommand mspf = (ConsoleParser cp, Object... args) -> 
        {   
            FPS.this.er.viewport.draw.stateless.top.remove(FPS.this.mspf);
        };

        /*
        SubCommand: hide fps
         -  Removes the fps Drawable from vp.draw.stateless.top
        */
        SubCommand fps = (ConsoleParser cp, Object... args) ->
        {
            FPS.this.er.viewport.draw.stateless.top.remove(FPS.this.fps);
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
        private double mspf = 0.0;
        private double fps = 0.0;

        private double[] mspf_average = new double[5];
        private int mspf_average_pointer = 0;

        List<Long> frameStartBuff = new LinkedList<>();
        List<Double> mspfBuffer = new LinkedList<>();

        private long lastFrameStart = 0;

        public void frameStarted(long ns)
        {   
            this.frameStartBuff.add(ns);
            this.lastFrameStart = ns;
        }

        private double calcAverage(double[] buff)
        {
            double out = 0.0;
            for( double d : buff )
                out += d;

            return out / (double)buff.length;
        }

        public void frameEnded(long ns)
        {
            this.mspf = (ns - this.lastFrameStart) / 1000000.0;
            this.mspf_average[mspf_average_pointer] = mspf;
            mspf_average_pointer++;
            if( mspf_average_pointer >= mspf_average.length )
            {
                double average = calcAverage(mspf_average);
                synchronized(mspfBuffer)
                {
                    mspfBuffer.add(average);
                }
                mspf_average_pointer = 0;
            }

            if( this.mspfBuffer.size() > 50 )
                this.mspfBuffer.remove(0);
        }
    }

    EngineR2 er;

    private Vec2 pos = new Vec2(0, 30);

    public FPS(EngineR2 er)
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


    Drawable mspf = (double x, double y, ViewPort vp) ->
    {
        vp.draw.stateless.rect.withTempMod()
            .withColour(50, 50, 50, 255)
            .withOffset(x, y)
            .withOffset(pos.getX(), pos.getY())
            .withFill()
        ;
        vp.draw.stateless.rect.poslen(0, 0, 100, 30);

        vp.draw.stateless.rect.activeMod()
            .withColour(255, 255, 255, 255)
            .withoutFill()
        ;

        vp.draw.stateless.rect.poslen(0, 0, 100, 30);

        vp.draw.stateless.rect.activeMod()
            .withColour(0, 255, 0, 255)
        ;

        // map our mspfs and draw them
        double maxDist = -26;
        double floor = 0;
        double ceil = 1;

        synchronized(this.data.mspfBuffer)
        {
            double offX = 98;
            for( double mspf : this.data.mspfBuffer )
            {
                double dist = (mspf - floor) / ceil;
                vp.draw.stateless.rect.poslen(offX, 28, -4, dist * maxDist);
                offX -= 4;
            }
        }

        vp.draw.stateless.rect.clearTempMod();

        vp.draw.stateless.text.withMod()
            .withOffset(x, y)
            .withOffset(pos.getX() + 50, pos.getY() + 15)
            .withAlignment(0, 0)
            .withColour(255, 255, 255, 255)
            .withSize(12)
        ;

        vp.draw.stateless.text.pos_c(0, 0, "mspf");
    };

    Drawable fps = (double x, double y, ViewPort vp) ->
    {
        vp.draw.stateless.rect.withTempMod()
            .withColour(50, 50, 50, 255)
            .withOffset(x, y)
            .withOffset(pos.getX(), pos.getY())
            .withFill()
        ;
        vp.draw.stateless.rect.poslen(1, 1, 98, 28);

        vp.draw.stateless.rect.activeMod()
            .withColour(255, 255, 255, 255)
            .withoutFill()
        ;

        vp.draw.stateless.rect.poslen(0, 0, 100, 30);

        vp.draw.stateless.rect.clearTempMod();

        vp.draw.stateless.text.withMod()
            .withOffset(x, y)
            .withOffset(pos.getX(), pos.getY())
        ;   

        vp.draw.stateless.text.pos_c(0, 0, "fps: " + this.data.fps);
    };
    

    public ObjectParser getParser()
    {   return this.PARSER; }

    public String describeCommand()
    {   return "returns information about framerate and performance";   }
}