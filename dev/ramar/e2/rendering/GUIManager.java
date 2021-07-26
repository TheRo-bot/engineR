package dev.ramar.e2.rendering;


public class GUIManager
{
    private ViewPort vp;

    private GUI currentGUI;

    public GUIManager()
    {

    }


    public GUIManager withViewPort(ViewPort vp)
    {
        this.vp = vp;
        return this;
    }


    public boolean requestGUI(GUI g)
    {
        boolean successful = false;
        System.out.println("requestGUI " + g);

        if( currentGUI == null )
            successful = true;
        else
        {
            successful = currentGUI.requestAccess(g);
            if( successful )
                currentGUI.prepSwapTo(g);
        }

        if( successful )
        {
            g.initiateGUI(vp);
            g.startDrawing();
        }

        return successful;
    }
}