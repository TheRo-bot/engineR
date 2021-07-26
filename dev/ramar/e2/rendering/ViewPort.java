package dev.ramar.e2.rendering;


import dev.ramar.e2.structures.WindowSettings;


public abstract class ViewPort
{
    public final DrawManager draw;
    public final Window window;
    public final GUIManager guis;

    protected enum State
    {
        DEAD, INIT, START, INTERRUPT
    }

    protected State currState = State.DEAD;



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


    public void init(WindowSettings ws)
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