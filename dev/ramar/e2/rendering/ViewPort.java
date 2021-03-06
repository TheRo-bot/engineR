package dev.ramar.e2.rendering;


import dev.ramar.e2.rendering.Window.FullscreenState;

import dev.ramar.utils.HiddenList;

/*
Class: ViewPort
 - Base template for any specifically implemented viewport to match
 - Current implementations:
    - AWTViewPort (Using Java's AWT library to do things)
*/
public abstract class ViewPort
{
    public final DrawManager draw;
    public final Window window;
    public final GUIManager guis;

    public interface ViewPortFrameListener 
    {
        public void frameStarted(long ms);

        public void frameEnded(long ms);        
    }

    public final ListenerList frameListeners = new ListenerList();

    public class ListenerList extends HiddenList<ViewPortFrameListener>
    {
        // add, remove, etc.. public methods, but, 
        // we add private methods here so we can hook ViewPort event
        // firing methods
        private void frameStarted(long ms)
        {
            for( ViewPortFrameListener vpfl : this.list )
                vpfl.frameStarted(ms);
        }

        private void frameEnded(long ms)
        {
            for( ViewPortFrameListener vpfl : this.list )
                vpfl.frameEnded(ms);
        }
    }   

    // and the best part is that viewport can't even access the list! :)
    protected void onFrameStart()
    {
        this.frameListeners.frameStarted((long)(System.nanoTime()));
    }

    protected void onFrameEnd()
    {
        this.frameListeners.frameEnded((long)(System.nanoTime()));
    }

    /* 
    Enum: State
     - A protected enum for implementations to keep state
       a lot simpler.
     ** make sure to "super.myMethod(...)" for any method you override!  
    */
    protected enum State
    {
        DEAD, INIT, START, INTERRUPT;


        public String getNameOf()
        {
            switch(this)
            {
                case      DEAD: return "Dead";
                case      INIT: return "Initialising";
                case     START: return "Start";
                case INTERRUPT: return "INTERRUPTED";
            };
            return null;
        }
    }

    protected State currState = State.DEAD;

    public boolean isRunning()
    {
        return currState == State.START;
    }


    protected ViewPort(DrawManager dm, Window w)
    {
        draw = dm;
        window = w;
        guis = new GUIManager();
        guis.withViewPort(this);
    }


    protected ViewPort(DrawManager dm, Window w, GUIManager gui)
    {
        draw = dm;
        window = w;
        guis = gui;
        guis.withViewPort(this);
    }


    public void waitForClose()
    {
        if( window != null )
        {
            try
            {
                while(! window.onClose.hasHappened())
                {
                    Thread.sleep(100);
                }
            }
            catch(InterruptedException e) {}
        }
    }

    public void init()
    {
        currState = State.INIT;
    }

    public void init(int screenW, int screenH, String title, FullscreenState fs)
    {
        currState = State.INIT;
    }

    public void start()
    {
        currState = State.START;
    }

    public void stop()
    {
        currState = State.DEAD;
    }

    public void interrupt()
    {
        currState = State.INTERRUPT;
    }


    public abstract double getCenterX();
    public abstract double getCenterY();

    public abstract void moveCenterX(double x);
    public abstract void moveCenterY(double y);

    public abstract void setCenterX(double x);
    public abstract void setCenterY(double y);


    public abstract int getLogicalWidth();
    public abstract int getLogicalHeight();

    public abstract void setLogicalWidth(int w);
    public abstract void setLogicalHeight(int w);

}