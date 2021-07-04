package dev.ramar.e2.rendering;


import dev.ramar.e2.rendering.control.*;

import java.util.*;

public abstract class Window
{

    public KeyController keys;
    public MouseController mouse;

    public CloseListeners onClose = new CloseListeners();

    public abstract int width();

    public abstract int height();



    /* Listener Callbacks
    -==---------------------
    */  

    /* CloseListener
    -===----------------
     Listener for when the 
    */

    public static class CloseListeners
    {
        private final List<CloseListener> listeners = new ArrayList<>();

        public CloseListeners()
        {
            }

        public interface CloseListener
        {
            public void onClose();
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