package dev.ramar.e2.rendering.control;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

import java.util.Iterator;

import dev.ramar.utils.ValuePair;

public class KeyController
{
    public final Stealable<Character> thieves = new KCStealer(); 

    private List<KeyCombo> combos = new ArrayList<>();

    private Map<KeyCombo, List<KeyPressListener>>    pressers = new HashMap<>();
    private Map<KeyCombo, List<KeyReleaseListener>> releasers = new HashMap<>();


    // protected Map<KeyCombo, List<KeyPressListener>>     pressListeners = new HashMap<>();
    // protected Map<KeyCombo, List<KeyReleaseListener>> releaseListeners = new HashMap<>();

    private Boolean[]    currModifiers = new Boolean[6];
    private Set<Character> currPressed = new HashSet<>();


    public KeyController()
    {
        for( int ii = 0; ii < currModifiers.length; ii++ )
            currModifiers[ii] = false;
    }

    public boolean isPressed(char c)
    { return currPressed.contains(c); }

    public void copyPressed(Set<Character> in)
    {
        in.clear();
        in.addAll(currPressed);
    }

    public void bindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( kc != null && kpl != null)
        {   

            if( !combos.contains(kc) )
            {
                pressers.put(kc, new ArrayList<>());
                combos.add(kc);
            }

            List<KeyPressListener> kpls = pressers.get(kc);
            kpls.add(kpl);

        }
                else
            throw new NullPointerException();
    }



    public void bindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        if( kc != null && krl != null )
        {

            if( !releasers.containsKey(kc) )
            {
                releasers.put(kc, new ArrayList<>());
                if( !combos.contains(kc) )
                    combos.add(kc);
            }

            List<KeyReleaseListener> krls = releasers.get(kc);
            krls.add(krl);


            ////
            // List<KeyReleaseListener> kcListeners = releaseListeners.get(kc);
            // if( kcListeners == null )
            // {
            //     releaseListeners.put(kc, new ArrayList<KeyReleaseListener>());
            //     kcListeners = releaseListeners.get(kc);
            // }

            // kcListeners.add(krl);
            // System.out.println(releaseListeners.get(kc));
        }
        else
            throw new NullPointerException();
    }



    public void unbindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( kc != null && kpl != null)
        {

            pressers.get(kc).remove(kpl);

            if( pressers.get(kc).isEmpty() && releasers.get(kc).isEmpty() )
            {
                pressers.remove(kc);
                combos.remove(kc);
            }
        }
        else
            throw new NullPointerException();
    }


    public void unbindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        if( kc != null && krl != null && combos.contains(kc) ) 
        {
            pressers.get(kc).remove(krl);

            if( pressers.get(kc).isEmpty() && releasers.get(kc).isEmpty() )
            {
                pressers.remove(kc);
                combos.remove(kc);
            }
        }
        else
            throw new NullPointerException();
    }


    private List<KeyCombo> active = new ArrayList<>();

    // represents when a character comes in
    // (defined as any character. i.e. '|' (shift + '\'))
    protected void onCharIn(char c)
    {
        if( ((KCStealer)thieves).onKeyIn(c) )
            // this isn't flawless, ':' wouldn't be ';',
            // but that's not the end of the world since
            // this edge case is very unlikely to be reached
            onKeyIn(Character.toLowerCase(c));
    }

    protected boolean isThieveryOccurring()
    {
        return !((KCStealer)thieves).charStealers.isEmpty();
    }

    // represents when a key stroke comes in,
    // defined as its abstract representation of the key
    // ('w' for the 'w' key on a keyboard, ';' for the semicolon
    //  ';' would still be inputted even if shift was held and ':'
    //  wouldn't been generated as a character)
    protected void onKeyIn(char c)
    {
        // don't bother stealing since the key could be
        // misrepresentative of the character input, and
        // loss of information could occur
        if( !currPressed.contains(c) )
        {
            currPressed.add(c);

            for( KeyCombo kc : combos )
                if( kc.isActive(currPressed, currModifiers) )
                {
                    synchronized(active)
                    {
                        active.add(kc);
                    }

                    List<KeyPressListener> kpls = pressers.get(kc);
                    if( kpls != null )
                        for( KeyPressListener kpl : kpls )
                            kpl.onPress(kc);
                }
        }
    }


    protected void onKeyOut(char c)
    {
        currPressed.remove(c);
        synchronized(active)
        {
            Iterator<KeyCombo> iter = active.iterator();
            while(iter.hasNext())
            {
                KeyCombo kc = iter.next();
                if( !kc.isActive(currPressed, currModifiers) )
                {
                    iter.remove();

                    List<KeyReleaseListener> krls = releasers.get(kc);
                    if( krls != null )
                        for( KeyReleaseListener krl : krls )
                            krl.onRelease(kc);
                }
            }
        }
    }



    /*
    Modifiers to: currModifiers
     - Abstractly modifies <currModifiers> so you can change
       whatever boolean means what here, and not everywhere else

     * these indeces are ripped from KeyCombo.flags
    */

    protected void setAllModifiers(boolean p)
    {
        for( int ii = 0; ii < currModifiers.length; ii++ )
            currModifiers[ii] = p;
    }

    protected void setLShift(boolean p)
    {   currModifiers[0] = p;   }


    protected void setRShift(boolean p)
    {   currModifiers[1] = p;   }


    protected void setLAlt(boolean p)
    {   currModifiers[2] = p;   }


    protected void setRAlt(boolean p)
    {   currModifiers[3] = p;   }


    protected void setLCntrl(boolean p)
    {   currModifiers[4] = p;   }


    protected void setRCntrl(boolean p)
    {   currModifiers[5] = p;   }



    /*
    Accessors to: currModifiers
     - A more static way of accessing what modifier keys are being pressed
       compared to KeyCombos
    */

    public boolean isLShift()
    {   return currModifiers[0];   }

    public boolean isRShift()
    {   return currModifiers[1];   }

    public boolean isLAlt()
    {   return currModifiers[2];   }

    public boolean isRAlt()
    {   return currModifiers[3];   }

    public boolean isLCntrl()
    {   return currModifiers[4];   }

    public boolean isRCntrl()
    {   return currModifiers[5];   }



    /* Inner Classes
    -===---------------
    */

    /*
    Helper: KCStealer (Stealable<Character>)
     - Handles the char thievery that this class allows
     - Is done through the new KeyController().thieves classfield
    */
    public static class KCStealer implements Stealable<Character>
    {
        private List<Stealer<Character>> charStealers = new ArrayList<>();

        private boolean onKeyIn(char c)
        {
            synchronized(charStealers)
            {
                for( int ii = 0; ii < charStealers.size(); ii++ )
                {
                    Stealer<Character> stealer = charStealers.get(ii);
                    // check with all previous stealers are ok with curr stealer
                    boolean canSteal = true;
                    for( int jj = ii; canSteal && jj >= 0; jj-- )
                        canSteal = canSteal && 
                          charStealers.get(jj).allowSimultaneousThievery(stealer);

                    if( canSteal )
                    {
                        stealer.onSteal(this, c);
                    }
                }

                boolean permitted = true;
                for( Stealer<Character> stealer : charStealers )
                    permitted = permitted &&
                                stealer.allowSimultaneousThievery(null);

                return permitted;
            }
        }


        public void startStealing(Stealer<Character> s)
        {
            synchronized(charStealers)
            {
                charStealers.add(s);
            }
        }   

        public void stopStealing(Stealer<Character> s)
        {
            synchronized(charStealers)
            {
                charStealers.remove(s);
            }
        }
    }
}