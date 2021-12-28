package dev.ramar.e2.rendering.awt.control;

import dev.ramar.e2.rendering.control.KeyController;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo;
import dev.ramar.e2.rendering.control.KeyController.KeyCombo.Modifiers;
import dev.ramar.e2.rendering.control.Stealer;

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

// pressMapping
// relMapping

    private Set<Integer> pressed = new HashSet<>();



    protected List<KeyCombo> active = new ArrayList<>();

    protected void onKeyPress()
    {
        System.out.println("onKeyPress: " + active + " :: " + pressed);
        if( getCharStealer().thieves.isEmpty() )
        {

            for( KeyCombo kc : pressMapping.keySet() )
            {
                if( !active.contains(kc) )
                {
                    synchronized(pressed)
                    {
                        synchronized(activeMods)
                        {
                            if( kc.isTriggered(pressed, activeMods))
                            {
                                if( !active.contains(kc) )
                                    active.add(kc);
                                for( KeyPressListener kl : pressMapping.get(kc) )
                                    kl.onPress(kc);
                            }
                        }
                    }
                }
            } 
        }


    }

    protected void onKeyRel()
    {
        System.out.println("onKeyRel (" + active + ")");
        List<KeyCombo> toRemove = new ArrayList<>();
        for( KeyCombo kc : active )
        {
            synchronized(pressed)
            {
                synchronized(activeMods)
                {
                    System.out.println("if (! " + kc.isTriggered(pressed, activeMods) + " )");
                    if( !kc.isTriggered(pressed, activeMods) )
                    {
                        // List<KeyReleaseListener> krls = relMapping.get(kc);
                        // System.out.println("foreach: " + krls);
                        for( KeyReleaseListener krl : relMapping.get(kc))
                        // if( krls != null )
                        // {
                        //     for( int ii = 0; ii < krls.size(); ii++ )
                            {
                                // KeyReleaseListener krl = krls.get(ii);
                                krl.onRelease(kc);
                            }
                            toRemove.add(kc);
                        // }
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
            System.out.println(e.getKeyCode());
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

            List<Stealer<Character>> thieves = getCharStealer().thieves;
            boolean stole = false;

            /*
            list of actions:
            - check if we have any thieves
                - if so, permit them to steal the chars
            - if nothing was stolen, send keyPress signal
            */
            for( int ii = 0; ii < thieves.size(); ii++ )
            {
                Stealer thisThief = thieves.get(ii);
                boolean permittedToSteal = true;
                for( int jj = ii - 1; jj > 0; jj-- )
                {
                    Stealer toCheck = thieves.get(jj);
                    permittedToSteal = toCheck.allowSimultaneousThievery(thisThief);
                    if( !permittedToSteal )
                        break;
                }

                if( permittedToSteal )
                {
                    stole = true;
                    thisThief.onSteal(stealChars, (char)((int)keyChar));
                }

            }

            if( !stole && !pressed.contains(keyChar) )
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