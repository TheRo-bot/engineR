package dev.ramar.e2.rendering.awt.control;

import dev.ramar.e2.rendering.control.KeyController;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo.Modifiers;

import dev.ramar.e2.rendering.awt.AWTViewPort;
import dev.ramar.e2.rendering.awt.AWTWindow;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class AWTKeyController extends KeyController
{
    private static final int SHIFT = 16,
                             CNTRL = 17,
                             ALT = 18;

    private AWTViewPort vp = null;

    public AWTKeyController()
    {

    }

    public AWTKeyController withViewPort(AWTViewPort vp)
    {
        this.vp = vp;

        ((AWTWindow)vp.window).getCanvas().addKeyListener(adapter);
        return this;
    }


    private Set<Integer> pressed = new HashSet<>();

    private Set<Modifiers> activeMods = new HashSet<>();


    protected List<KeyCombo> active = new ArrayList<>();

    protected void onKeyPress()
    {
        for( KeyCombo kc : keyMapping.keySet() )
        {
            if( !active.contains(kc) )
            {
                synchronized(pressed)
                {
                    synchronized(activeMods)
                    {
                        if( kc.isTriggered(pressed, activeMods) )
                        {
                            if( !active.contains(kc))
                                active.add(kc);
                            for( KeyListener kl : keyMapping.get(kc) )
                                kl.onPress(kc);
                        }
                    }
                }
            }
        }
    }

    protected void onKeyRel()
    {
        List<KeyCombo> toRemove = new ArrayList<>();
        for( KeyCombo kc : active )
        {
            synchronized(pressed)
            {
                synchronized(activeMods)
                {
                    if( !kc.isTriggered(pressed, activeMods) )
                    {
                        for( KeyListener kl : keyMapping.get(kc) )
                            kl.onRelease(kc);
                        toRemove.add(kc);
                    }
                }
            }
        }

        active.removeAll(toRemove);
    }


    @Override
    public boolean isPressed(KeyCombo kc)
    {
        synchronized(pressed)
        {
            return kc.isTriggered(pressed, activeMods);
        }
    }

    private KeyAdapter adapter = new KeyAdapter()
    {

        /*@Override
        public void keyPressed(KeyEvent e)
        {
            // do things for modifiers
            if( e.getKeyCode() == SHIFT )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LSHIFT : Modifiers.RSHIFT
                    );
                }
            }
            else if( e.getKeyCode() == ALT )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LALT : Modifiers.RALT
                    );   
                }
            }
            else if( e.getKeyCode() == CNTRL )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LCNTRL : Modifiers.RCNTRL
                    );
                }
            }


            // if it's not a modifier, then it's a keycode
            synchronized(pressed)
            {
                if( !pressed.contains((int)e.getKeyChar()) )
                {
                    pressed.add((int)e.getKeyChar());
                    onKeyUp();
                }
            }

        }*/




        @Override
        public void keyPressed(KeyEvent e)
        {
            if( e.getKeyCode() == SHIFT )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LSHIFT : Modifiers.RSHIFT
                    );
                }
            }
            else if( e.getKeyCode() == ALT )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LALT : Modifiers.RALT
                    );   
                }
            }
            else if( e.getKeyCode() == CNTRL )
            {
                synchronized(activeMods)
                {
                    activeMods.add(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LCNTRL : Modifiers.RCNTRL
                    );
                }
            }
            int keyChar = (int)Character.toLowerCase(e.getKeyChar());

            if( !pressed.contains(keyChar) )
            {
                pressed.add(keyChar);
                onKeyPress();
            }
        }


        @Override
        public void keyReleased(KeyEvent e)
        {
            if( e.getKeyCode() == SHIFT )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LSHIFT : Modifiers.RSHIFT
                    );
                }
            }
            else if( e.getKeyCode() == ALT )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LALT : Modifiers.RALT
                    );   
                }
            }
            else if( e.getKeyCode() == CNTRL )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LCNTRL : Modifiers.RCNTRL
                    );
                }
            }
            int keyChar = (int)Character.toLowerCase(e.getKeyChar());
            if( pressed.contains(keyChar) )
            {
                pressed.remove(keyChar);
                onKeyRel();
            }
        }






  /*      @Override
        public void keyReleased(KeyEvent e)
        {
            if( e.getKeyCode() == SHIFT )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LSHIFT : Modifiers.RSHIFT
                    );
                }
            }
            else if( e.getKeyCode() == ALT )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LALT : Modifiers.RALT
                    );   
                }
            }
            else if( e.getKeyCode() == CNTRL )
            {
                synchronized(activeMods)
                {
                    activeMods.remove(
                        e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT ? 
                        Modifiers.LCNTRL : Modifiers.RCNTRL
                    );
                }
            }

            synchronized(pressed)
            {
                pressed.remove((int)e.getKeyChar());
                onKeyDown();
            }
        }
*/
    };
}