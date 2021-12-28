package dev.ramar.e2.rendering;


import dev.ramar.e2.rendering.control.*;

import java.util.*;

public abstract class Window
{
    public enum FullscreenState
    {
        WINDOWED,
        FULLSCREEN,
        WINDOWED_BORDERLESS
    }


    public final KeyController keys;
    public final MouseController mouse;

    public final CloseListeners  onClose = new CloseListeners();
    public final ResizeListeners onResize = new ResizeListeners();

    protected FullscreenState fullscreenState = FullscreenState.WINDOWED;

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


    public abstract void init();

    public abstract boolean isRenderable();


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
            if( happened )
                cl.onClose();
            else
            {
                synchronized(listeners)
                {
                    listeners.add(cl);
                }
            }
        }

        public void remove(CloseListener cl)
        {
            synchronized(listeners)
            {
                listeners.remove(cl);
            }
        }

        private void clear()
        {
            synchronized( listeners )
            {
                listeners.clear();
            }
        }

        private void onClose()
        {
            synchronized(listeners)
            {
                // for( CloseListener cl : listeners )
                for( int ii = 0; ii < listeners.size(); ii++ )
                {
                    CloseListener cl = listeners.get(ii);
                    cl.onClose();
                }
            }
        }
    }


    protected void onClose()
    {
        onClose.onClose();
        //// revoked for more freedom on calling listeners and performing actions
        // close();
    }


    protected void clearListeners()
    {
        onClose.clear();
        onResize.clear();
    }

    // make sure to call onResize(w, h) once you've resized!
    public abstract void resize(int w, int h);

    public abstract void setTitle(String s);

    public abstract void setFullscreenState(FullscreenState fss);

    /* ResizeListener
    -===----------------
     Listener for when the window changes size.
    */

    public static class ResizeListeners
    {
        private final List<ResizeListener> listeners = new ArrayList<>();

        public ResizeListeners() {}


        public interface ResizeListener
        {
            public void onResize(int w, int h);
        }

        public void add(ResizeListener cl)
        {
            listeners.add(cl);
        }

        public void remove(ResizeListener cl)
        {
            listeners.remove(cl);
        }


        private void onResize(int w, int h)
        {
            for( ResizeListener cl : listeners )
                cl.onResize(w, h);
        }

        private void clear()
        {
            synchronized( listeners )
            {
                listeners.clear();
            }
        }
    }

    protected void onResize(int w, int h)
    {
        onResize.onResize(w, h);
    }

    public abstract void close();


}