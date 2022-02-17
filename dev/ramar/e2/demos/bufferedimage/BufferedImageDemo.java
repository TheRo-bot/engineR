package dev.ramar.e2.demos.bufferedimage;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.TestDemos.Demo;

import java.util.List;
import java.util.ArrayList;

public class BufferedImageDemo implements Demo
{
    BufferDemo demo;

    public BufferedImageDemo()
    {
        demo = new BufferDemo();
    }


    List<EngineR2> initialised = new ArrayList<>();


    
    public void start(List<EngineR2> instances)
    {
        for( EngineR2 er : instances )
        {
            demo.attach(er);
            initialised.add(er);
        }
    }

    public void stop(List<EngineR2> instances)
    {
        for( EngineR2 er : instances ) 
        {
            demo.release(er);
            initialised.remove(er);
        }
    }


}