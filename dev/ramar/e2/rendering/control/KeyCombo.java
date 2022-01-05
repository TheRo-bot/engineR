package dev.ramar.e2.rendering.control;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

public class KeyCombo
{
    public enum Directionality
    {
        LEFT, RIGHT;
    }


    private String name;

    private Set<Character> toActivate = new HashSet<>();


    // this is a bit un-readable, but no one's
    // meant to know about this variable so it's
    // fine >:)
    // basically, just stores which modifier keys
    // should be pressed to activate this combo    
    private Boolean[] flags = new Boolean[]
    {
        // 0 - lshift
        null,
        // 1 - rshift
        null,
        // 2 - lalt
        null,
        // 3 - ralt
        null,
        // 4 - lcntrl
        null,
        // 5 - rcntrl
        null
    };

    public KeyCombo(String name)
    {
        this.name = name;
    }

    public String getName()
    {  return name;  }


    public String toString()
    {
        String out = "KeyCombo || ";
        if( isShift() )
        {
            out += "Shift";
            if(isLShift() && !isRShift())
                out += "[R]";
            else
                out += "[L]";
            out += " || ";
        }

        if( isAlt() )
        {
            out += "Alt ";
            if( isLAlt() && !isRAlt() )
                out += "[L]";
            else
                out += "[R]";
            out += " || ";
        }

        if( isCntrl() )
        {
            out += "Cntrl ";
            if( isLCntrl() && !isRAlt() )
                out += "[L]";
            else
                out += "[R]";
            out += " || ";
        }

        return out + "Keys" + toActivate;
    }



    public KeyCombo withChar(char b)
    {
        toActivate.add(b);
        return this;
    }

    public void clearChars()
    {
        toActivate.clear();
    }


    /*
    Anytime Builder: withTShift
     - Toggles a directional shift, pass null for both.
    */
    public KeyCombo withTShift(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 0 : 1;
            flags[pointer] = !flags[pointer];
        }
        else
        {
            flags[0] = !flags[0];
            flags[1] = !flags[1];
        }

        return this;
    }

    public KeyCombo withNoShift(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 0 : 1;
            flags[pointer] = null;
        }
        else
            flags[0] = flags[1] = null;

        return this;
    }



    /*
    Anytime Builder: withTAlt
     - Toggles a directional alt, pass null for both.
    */
    public KeyCombo withTAlt(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 2 : 3;
            flags[pointer] = !flags[pointer];
        }
        else
        {
            flags[2] = !flags[2];
            flags[3] = !flags[3];
        }
        return this;
    }


    public KeyCombo withNoAlt(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 2 : 3;
            flags[pointer] = null;
        }
        else
            flags[2] = flags[3] = null;
        
        return this;
    }


    /*
    Anytime Builder: withTCntrl
     - Toggles a directional cntrl, pass null for both.
    */
    public KeyCombo withTCntrl(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 4 : 5;
            flags[pointer] = !flags[pointer];
        }
        else
        {
            flags[4] = !flags[4];
            flags[5] = !flags[5];
        }

        return this;
    }


    public KeyCombo withNoCntrl(Directionality dir)
    {
        if( dir != null )
        {
            int pointer = dir == Directionality.LEFT ? 4 : 5;
            flags[pointer] = null;
        }
        else
            flags[4] = flags[5] = null;
        
        return this;
    }


    /*
    Query: isShift
     - If either shift key should be held down to activate
    */
    public boolean isShift()
    {
        return isLShift() || isRShift();
    }

    /*
    Query: isLShift
     - If the left shift key should be held down to activate
    */
    public boolean isLShift()
    {
        return flags[0] != null && flags[0];
    }


    /*
    Query: isRShift
     - If the right shift key should be held down to activate
    */
    public boolean isRShift()
    {
        return flags[1] != null && flags[1];
    }


    /*
    Query: isAlt
     - If either alt key should be held down to activate
    */
    public boolean isAlt()
    {
        return isLAlt() || isRAlt();
    }


    /*
    Query: isLAlt
     - If the left alt key should be held down to activate
    */
    public boolean isLAlt()
    {
        return flags[2] != null && flags[2];
    }

    /*
    Query: isRAlt
     - If the right alt key should be held down to activate
    */
    public boolean isRAlt()
    {
        return flags[3] != null && flags[3];
    }

    /*
    Query: isCntrl
     - If either cntrl key should be held down to activate
    */
    public boolean isCntrl()
    {
        return isLCntrl() || isRCntrl();
    }


    /*
    Query: isLCntrl
     - If the left cntrl key should be held down to activate
    */
    public boolean isLCntrl()
    {
        return flags[4] != null && flags[4];
    }

    /*
    Query: isRCntrl
     - If the right cntrl key should be held down to activate
    */
    public boolean isRCntrl()
    {
        return flags[5] != null && flags[5];
    }


    /*
    Query: isActive
     - Checks if this KeyCombo is active, based off of the set of
       pressed characters, and a given flags array, which directly
       corresponds to this KeyCombo's flags array
    */
    public boolean isActive(Set<Character> pressed, Boolean[] flags)
    {
        return pressed.containsAll(toActivate) && 
               flagsMatch(this.flags, flags);
    }


    /* Static Methods
    -==-----------------
    */


    /*
    Method: flagsMatch
     - Checks if two boolean arrays are equal
    */
    public static boolean flagsMatch(Boolean[] a, Boolean[] b)
    {
        boolean match = a.length == b.length;

        for( int ii = 0; match && ii < a.length; ii++ )
        {
            match = match && a[ii] == null || a[ii] == b[ii];
        }

        return match;    
    }






}