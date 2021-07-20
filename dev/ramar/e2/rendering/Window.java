package dev.ramar.e2.rendering;


import dev.ramar.e2.rendering.control.*;

import java.util.*;

public abstract class Window
{

    public final KeyController keys;
    public final MouseController mouse;

    public final CloseListeners onClose = new CloseListeners();

    public abstract int width();

    public abstract int height();

    protected Window()
    {
        keys = null;
        mouse = null;
        System.out.println("WARNING: Window running control-less!");
    }

    protected Window(KeyController keys, MouseController mouse)
    {
        this.keys = keys;
        this.mouse = mouse;
    }


    /* Listener Callbacks
    -==---------------------
    */  

    /* CloseListener
    -===----------------
     Listener for when the window closes.
     This is a one-off event, the provided
     method hasHappened() lets you check if
     a close has already been initiated 
    */

    public static class CloseListeners
    {
        private final List<CloseListener> listeners = new ArrayList<>();
        private boolean happened = false;

        public CloseListeners() 
        {
            add(() ->
            {
                happened = true;
            });
        }

        public interface CloseListener
        {
            public void onClose();
        }

        public boolean hasHappened()
        {
            return happened;
        }



        public void add(CloseListener cl)
        {
            listeners.add(cl);
        }

        public void remove(CloseListener cl)
        {
            listeners.remove(cl);
        }


        private void onClose()
        {
            for( CloseListener cl : listeners )
                cl.onClose();
        }
    }


    protected void onClose()
    {
        onClose.onClose();
    }


}