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

import java.util.List;
import java.util.LinkedList;

public class FPS implements Command
{
    public static final ObjectParser PARSER = new StringSplitter(" "); 

    public Data data = new Data();

    public class Data implements ViewPortFrameListener
    {
        private double mspf = 0.0;
        private double fps = 0.0;

        private double[] mspf_average = new double[50];
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
                    mspfBuffer.add(mspf);
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

    /*
    frames show mspf
    frames show fps
    frames list
    */
    public Object run(ConsoleParser cp, Object[] args)
    {
        if( args.length > 1 )
        {
            String message = (String)args[1];
            if( message.equals("list") )
                list(cp);
            else if( message.equals("show"))
                show(cp, args);
        }

        return null;
    }


    private void list(ConsoleParser cp)
    {
        cp.ps.println("frames show mspf || puts the mspf counter on the screen");
        cp.ps.println("frames show fps  || puts the fps counter on the screen");
    }


    private void show(ConsoleParser cp, Object[] args)
    {
        // args = [frames, show, ...]
        if( args.length > 2 )
        {
            String message = (String)args[2];
            if( message.equals("mspf") )
                show_mspf(cp, args);
            else if( message.equals("fps"))
                show_fps(cp, args);
        }
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

    private void show_mspf(ConsoleParser cp, Object[] args)
    {
        er.viewport.draw.stateless.top.remove(fps);
        er.viewport.draw.stateless.top.add(mspf);
    }

    private void show_fps(ConsoleParser cp, Object[] args)
    {
        er.viewport.draw.stateless.top.add(fps);
        er.viewport.draw.stateless.top.remove(mspf);
    }

    public ObjectParser getParser()
    {   return this.PARSER; }

    public String describeCommand()
    {   return "returns information about framerate and performance";   }
}