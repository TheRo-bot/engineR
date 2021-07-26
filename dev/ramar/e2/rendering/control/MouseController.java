
package dev.ramar.e2.rendering.control;


import dev.ramar.e2.structures.Vec2;

import java.util.List;
import java.util.ArrayList;

/*

NOTE:
 x and y should be in coordinate space of the game window
*/
public abstract class MouseController
{   

    public final PressedListeners onPress;
    public final ReleasedListeners onRelease;

    protected MouseController()
    {
        onPress = new PressedListeners();
        onRelease = new ReleasedListeners();
    }

    protected void onPress(int button, double x, double y)
    {
        // System.out.println("onPress " + button + ": " + x + ", " + y );
        onPress.mousePressed(button, x, y);
    }

    protected void onRelease(int button, double x, double y)
    {
        // System.out.println("onRelease " + button + ": " + x + ", " + y );
        onRelease.mouseReleased(button, x, y);
    }

    public abstract double getMouseX();

    public abstract double getMouseY();


    public static class PressedListeners
    {
        private List<PressedListener> listeners = new ArrayList<>();

        public PressedListeners()
        {

        }

        public interface PressedListener
        {
            public void mousePressed(int bID, double x, double y);
        }


        public void add(PressedListener pl)
        {
            listeners.add(pl);
        }

        public void remove(PressedListener pl)
        {
            listeners.remove(pl);
        }

        private void mousePressed(int button, double x, double y)
        {
            for( PressedListener pl : listeners )
                pl.mousePressed(button, x, y);
        }

    }

    public static class ReleasedListeners
    {
        private List<ReleasedListener> listeners = new ArrayList<>();

        public ReleasedListeners()
        {

        }

        public interface ReleasedListener
        {
            public void mouseReleased(int bID, double x, double y);
        }


        public void add(ReleasedListener rl)
        {
            listeners.add(rl);
        }


        public void remove(ReleasedListener rl)
        {
            listeners.remove(rl);
        }


        private void mouseReleased(int button, double x, double y)
        {
            for( ReleasedListener rl : listeners )
                rl.mouseReleased(button, x, y);
        }
    }


}