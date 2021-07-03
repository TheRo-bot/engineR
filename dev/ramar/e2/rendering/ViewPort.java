package dev.ramar.e2.rendering;


import dev.ramar.e2.structures.WindowSettings;

public abstract class ViewPort
{
    public final DrawManager draw;
    public final Window window;

    protected enum State
    {
        DEAD, INIT, START, INTERRUPT
    }

    protected State currState = State.DEAD;



    protected ViewPort(DrawManager dm, Window w)
    {
        draw = dm;
        window = w;
    }

    public abstract void waitForClose();

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

}