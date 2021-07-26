package dev.ramar.e2.rendering.control;


import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

public abstract class KeyController
{

    public interface KeyListener
    {
        public void onPress(KeyCombo kc);

        public void onRelease(KeyCombo kc);
    }


    protected final Map<KeyCombo, List<KeyListener>> keyMapping = new HashMap<>();

    public void bind(KeyCombo kc, KeyListener kl)
    {
        if( !keyMapping.containsKey(kc) )
            keyMapping.put(kc, new ArrayList<>());

        // System.out.println("keyMapping: " + keyMapping);
        keyMapping.get(kc).add(kl);
    }


    public void unbind(KeyCombo kc, KeyListener kl)
    {
        if( keyMapping.containsKey(kc) )
            keyMapping.get(kc).remove(kl);
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

        public String getName()
        {
            return name;
        }


        public KeyCombo withChar(char c)
        {
            System.out.println("adding " + ((int)c) + " to " + chars);
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