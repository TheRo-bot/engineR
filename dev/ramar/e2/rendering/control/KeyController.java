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

    private boolean[]    currModifiers = new boolean[6];
    private Set<Character> currPressed = new HashSet<>();


    public KeyController()
    {
        for( int ii = 0; ii < currModifiers.length; ii++ )
            currModifiers[ii] = false;
    }


    public void bindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( kc != null )
        {   

            if( !combos.contains(kc) )
            {
                pressers.put(kc, new ArrayList<>());
                combos.add(kc);
            }

            List<KeyPressListener> kpls = pressers.get(kc);
            kpls.add(kpl);

        }
    }



    public void bindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        System.out.println("bindRel " + kc);
        if( kc != null )
        {

            if( !combos.contains(kc) )
            {
                releasers.put(kc, new ArrayList<>());
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
    }



    public void unbindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( kc != null )
        {

            pressers.get(kc).remove(kpl);

            if( pressers.get(kc).isEmpty() && releasers.get(kc).isEmpty() )
            {
                pressers.remove(kc);
                combos.remove(kc);
            }
        }
    }


    public void unbindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        if( kc != null && combos.contains(kc) ) 
        {
            pressers.get(kc).remove(krl);

            if( pressers.get(kc).isEmpty() && releasers.get(kc).isEmpty() )
            {
                pressers.remove(kc);
                combos.remove(kc);
            }
        }
    }


    private List<KeyCombo> active = new ArrayList<>();


    protected void onKeyIn(char c)
    {
        // if this char wasn't stolen, 
        // check if we've pressed anything

        if( !currPressed.contains(c) )
        {
            if(! ((KCStealer)thieves).onKeyIn(c) )
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


    static
    {
        KeyController kc = new KeyController();


        kc.bindPress(new KeyCombo("bruh").withChar('w'), 
                (KeyCombo pressed) -> 
                {
                    System.out.println(pressed + " pressed !");
                }
        );
        kc.bindRel(new KeyCombo("bruh").withChar('w'), 
                (KeyCombo rel) ->
                {
                    System.out.println(rel + " released !");
                }
        );

        System.out.println("------");
        // System.out.println(kc.pressListeners);
        System.out.println(kc.releasers);
        System.out.println();
        System.out.println("------");
    }




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
            boolean wasStolen = false;
            synchronized(charStealers)
            {
                int ii = 0;
                for( Stealer<Character> stealer : charStealers )
                {
                    // check with all previous stealers are ok with curr stealer
                    boolean canSteal = true;
                    for( int jj = ii; canSteal && jj >= 0; jj-- )
                        canSteal = canSteal && 
                          charStealers.get(jj).allowSimultaneousThievery(stealer);

                    if( canSteal )
                    {
                        stealer.onSteal(this, c);
                        wasStolen = true;
                    }
                    ii++;
                }
            }
            return wasStolen;
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