package dev.ramar.e2.rendering;


public abstract class GUI
{
    protected ViewPort viewport;

    public GUI()
    {

    }


    public abstract boolean requestAccess(GUI g);


    public abstract void prepSwapTo(GUI g);

    public abstract void startDrawing();

    public final void initiateGUI(ViewPort vp)
    {
        viewport = vp;
    }


    public final void stopDrawing()
    {
        viewport = null;
    }


    public ViewPort getViewPort()
    {
        return viewport;
    }

}