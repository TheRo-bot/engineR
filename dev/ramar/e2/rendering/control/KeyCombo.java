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
    private boolean[] flags = new boolean[]
    {
        // 0 - lshift
        false,
        // 1 - rshift
        false,
        // 2 - lalt
        false,
        // 3 - ralt
        false,
        // 4 - lcntrl
        false,
        // 5 - rcntrl
        false
    };

    public KeyCombo(String name)
    {
        this.name = name;
    }


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


    public boolean isShift()
    {
        return isLShift() || isRShift();
    }

    public boolean isLShift()
    {
        return flags[0];
    }

    public boolean isRShift()
    {
        return flags[1];
    }


    public boolean isAlt()
    {
        return isLAlt() || isRAlt();
    }

    public boolean isLAlt()
    {
        return flags[2];
    }

    public boolean isRAlt()
    {
        return flags[3];
    }


    public boolean isCntrl()
    {
        return isLCntrl() || isRCntrl();
    }

    public boolean isLCntrl()
    {
        return flags[4];
    }

    public boolean isRCntrl()
    {
        return flags[5];
    }

    /*static
    {
        KeyCombo kc1 = new KeyCombo("one")
            .withChar('a')
            .withTShift(Directionality.LEFT)
        ;
        KeyCombo kc2 = new KeyCombo("two")
            .withChar('a')
            .withTShift(Directionality.RIGHT)
        ;
        System.out.println("-----===-----");

        Set<Character> testSet = new HashSet<>();
        boolean[] testFlags = new boolean[]
            {true, false, false, false, false, false};

        testSet.add('a');


        System.out.println("    " + kc1);
        System.out.println("    " + kc2);
        System.out.println("    active test: " + testSet);

        System.out.println("    " + kc1.isActive(testSet, testFlags));
        System.out.println("    " + kc2.isActive(testSet, testFlags));
        System.out.println("-----===-----");
    }*/

    public boolean isActive(Set<Character> pressed, boolean[] flags)
    {
        boolean allPressed = true;
        for( Character c : toActivate )
        {
            if(! pressed.contains(c) )
            {
                allPressed = true;
                break; 
            }
        }
        // System.out.println("isActive: " + allPressed + ", " + flagsMatch(this.flags, flags));
        // System.out.println(Arrays.toString(flags) + "\nvs\n" + Arrays.toString(this.flags));

        boolean modifiersMatch = flags.length == this.flags.length;

        if( modifiersMatch )
            for( int ii = 0; modifiersMatch && ii < flags.length; ii++ )
                modifiersMatch = modifiersMatch && (this.flags[ii] == flags[ii]);

        return allPressed && modifiersMatch;
    }


    public static boolean flagsMatch(boolean[] a, boolean[] b)
    {
        boolean match = a.length == b.length;
        if( match )
        {
            for( int ii = 0; match && ii < a.length; ii++ )
            {
                match = match && a[ii] ? b[ii] : false;
            }
        }

        return match;    
    }






}