package dev.ramar.e2.rendering.control;

import dev.ramar.e2.rendering.control.KeyController.KeyCombo.Modifiers;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

public abstract class KeyController
{

    public interface KeyListener extends KeyPressListener, KeyReleaseListener
    {
    }

    public interface KeyPressListener
    {
        public void onPress(KeyCombo kc);
    }

    public interface KeyReleaseListener
    {
        public void onRelease(KeyCombo kc);
    }

    protected static class CharStealable implements Stealable<Character>
    {
        public List<Stealer<Character>> thieves = new ArrayList<>();
        public CharStealable()
        {

        }

        public void startStealing(Stealer<Character> s)
        {
            synchronized(thieves)
            {
                thieves.add(s);
            }
        }

        public void stopStealing(Stealer<Character> s)
        {
            synchronized(thieves)
            {
                thieves.remove(s);
            }
        }

    } 
    public final Stealable<Character> stealChars = new CharStealable();

    protected CharStealable getCharStealer() 
    {   return (CharStealable)stealChars;   }

    public KeyController()
    {

    }

    protected final Map<KeyCombo, List<KeyPressListener>> pressMapping = new HashMap<>();
    protected final Map<KeyCombo, List<KeyReleaseListener>> relMapping   = new HashMap<>();

    protected final Set<Modifiers> activeMods = new HashSet<>();


    public void bindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( !pressMapping.containsKey(kc) )
            pressMapping.put(kc, new ArrayList<>());

        pressMapping.get(kc).add(kpl);
    }

    public void bindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        if( !relMapping.containsKey(kc) )
            relMapping.put(kc, new ArrayList<>());

        relMapping.get(kc).add(krl);
    }


    public void bind(KeyCombo kc, KeyListener kl)
    {
        bindPress(kc, kl);
        bindRel(kc, kl);
    }

    public void bind(KeyCombo kc, KeyPressListener kpl, KeyReleaseListener krl)
    {
        bindPress(kc, kpl);
        bindRel(kc, krl);
    }

    public void unbindPress(KeyCombo kc, KeyPressListener kpl)
    {
        if( pressMapping.containsKey(kc) )
            pressMapping.get(kc).remove(kpl);
    }

    public void unbindRel(KeyCombo kc, KeyReleaseListener krl)
    {
        if( relMapping.containsKey(kc) )
            relMapping.get(kc).remove(krl);
    }

    public void unbind(KeyCombo kc, KeyListener kl)
    {
        unbindPress(kc, kl);
        unbindRel(kc, kl);
    }


    public abstract boolean isPressed(KeyCombo kc);


    public static class KeyCombo
    {
        public static enum Modifiers
        {
            LSHIFT, RSHIFT, LCNTRL, RCNTRL, LALT, RALT
        }

        public Set<Modifiers> modifiers = new HashSet<>();
        public Set<Integer> chars = new HashSet<>();
        private String name;

        public KeyCombo(String name)
        {   
            this.name = name;
        }

        public String toString()
        {
            return "KeyCombo: " + getName() + " with chars " + chars + "(" + modifiers +  ")";
        }

        public String getName()
        {
            return name;
        }


        public KeyCombo withChar(char c)
        {
            chars.add((int)Character.toLowerCase(c));
            return this;
        }

        public KeyCombo withKeyCode(int code)
        {
            chars.add(code);
            return this;
        }

        public KeyCombo withShift(boolean left)
        {
            modifiers.add(left ? Modifiers.LSHIFT : Modifiers.RSHIFT);
            return this;
        }

        public KeyCombo withCntrl(boolean left)
        {
            modifiers.add(left ? Modifiers.LCNTRL : Modifiers.RCNTRL);
            return this;
        }

        public KeyCombo withAlt(boolean left)
        {
            modifiers.add(left ? Modifiers.LALT : Modifiers.RALT);
            return this;
        }

        public boolean equals(Object o)
        {
            boolean equal = false;
            KeyCombo kc;
            if( o instanceof KeyCombo)
            {
                kc = (KeyCombo)o;
                if( kc.name.equals(name))
                    // if( kc.modifiers.equals(modifiers) )
                    //     if( kc.chars.equals(chars))
                            equal = true;
            }
            return equal;
        }

        public boolean isTriggered(Set<Integer> pressed, Set<Modifiers> activeMods)
        {
            boolean isPressed = true;
            for( Modifiers mod : modifiers )
            {
                if( !activeMods.contains(mod))
                {
                    isPressed = false;
                    break;
                }
            }

            if( isPressed )
            {
                for( Integer c : chars )
                {
                    if( !pressed.contains(c) )
                    {
                        isPressed = false;
                        break;
                    }
                }
            }

            return isPressed;
        }

    }

}