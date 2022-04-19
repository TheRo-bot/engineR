package dev.ramar.e2.rendering.console;


import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.control.KeyController;
import dev.ramar.e2.rendering.control.Stealer;
import dev.ramar.e2.rendering.control.Stealable;

import dev.ramar.e2.rendering.drawing.rect.Rect;
import dev.ramar.e2.rendering.drawing.stateful.Shape;

import dev.ramar.e2.rendering.ui.*;
import dev.ramar.e2.rendering.animations.*;

import dev.ramar.utils.ValuePair;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

import java.io.PrintStream;

public class Console implements Drawable
{
    private double x = 0, y = 0;
    private double windowW = -1/*400*/,
                   windowH = -1/*600*/;
    private boolean visible = false;

    private ViewPort vpRef = null;
    public final ConsoleParser parser;

    public final PrintStream out;

    public ViewPort getViewPortRef()
    {   return vpRef;   }

    private final Stealer<Character> charStealer = new Stealer<Character>()
    {

        public void onSteal(Stealable<Character> s, Character c)
        {
            TextField in = null;
            if( in != null )
            {
                String inputText = in.getInputText();
                int intCode = (int)(char)c;
                switch(intCode)
                {
                    // all codes we want to ignore:
                    /*
                        alt / cntrl / shift: 65535
                    */
                    case 65535:
                        break;

                    // '`'
                    case 8:
                        if( inputText != null )
                        {
                            String newString = inputText.substring(0, Math.max(0,inputText.length() - 1));
                            in.withInput(newString);
                        }

                        break;
                    // return character ('\n')
                    case 10:
                        parser.parseCommand(inputText);
                        break;

                    case (int)'`':
                        animation_SwapVisibility();
                        break;
                    default:
                        in.withInput(inputText == null ? "" + c : inputText + c);
                }
            }
        }

        public boolean allowSimultaneousThievery(Stealer<Character> c)
        {
            return true;
        }
    };

    private Map<String, Animation> animations = new HashMap<>();

    public Console()
    {
        parser = ConsoleParser.createParser(this);
        out = System.out;
        animationsSetup();
    }


    public Console withVisibility(boolean b)
    {
        visible = b;
        return this;
    }

    public Console withOffset(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }


    public boolean getVisibility()
    {   return visible;   }


    public Console withViewPort(ViewPort vp)
    {
        vpRef = vp;
        return this;
    }


    public Console withPos(double x, double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Console withSize(double w, double h)
    {
        this.windowW = w;
        this.windowH = h;
        return this;
    }


    public void drawAt(double x, double y, ViewPort vp) {}


    /* Commands related section
    -===--------------------------
    */




    /* Animations 
    -===------------
    */

    private void animationsSetup()
    {
        // x = -windowW;
        // // Opening animation, the anonymous class allows for <percentage>
        // // and <dist> to be nicely hidden away without being too annoying
        // animations.put("onOpen", new RepeatableTaskAnimation()
        // {
        //     private double percentage = 1.0;
        //     private double dist = windowW;

        //     private boolean first = true;
        //     private long startTime;
        //     // TODO: rename this
        //     protected void constructorOverrideable()
        //     {
        //         // these "anytime builder" methods i've been addicted with
        //         // allows for super easy modification of an animation
        //         withDelay(2);
        //         withExecTime(250);

        //         // based runnable lambda for cheeky listeners
        //         // (i've been addicted to messing around with these things
        //         //  and scope recently)
        //         withTask(() -> 
        //         {
        //             x += (dist / ((double)getRepeatCount() / 2.0)) * percentage;
        //             percentage -= ((double)1 / (double)getRepeatCount());

        //         });
        //         whenStart(() ->
        //         {
        //             startTime = System.currentTimeMillis();
        //             percentage = 1.0;
        //             x = -dist;
        //             visible = true;
        //         });
        //         whenFinished(() ->
        //         {
        //             onVisible();   
        //         });
        //     }
        // });


        // animations.put("onClose", new RepeatableTaskAnimation()
        // {
        //     private double percentage = 1.0;
        //     private double dist = windowW;
        //     protected void constructorOverrideable()
        //     {
        //         withDelay(3);
        //         withExecTime(250);
        //         withTask(() ->
        //         {
        //             x -= (dist / ((double)getRepeatCount() / 2.0)) * percentage;
        //             percentage -= ((double)1 / (double)getRepeatCount());
        //         });
        //         whenStart(() -> 
        //         {
        //             percentage = 1.0;
        //         });
        //         whenFinished(() ->
        //         {
        //             onInvisible();
        //             visible = false;
        //         });
        //     }
        // });
    }

    public Console animation_SwapVisibility()
    {
        // String toGet = visible ? "onClose" : "onOpen";
        // String other = visible ? "onOpen"  : "onClose";

        // Animation gottenAnim = animations.get(toGet);
        // Animation otherAnim  = animations.get(other);

        // if( gottenAnim.isIdle() && otherAnim.isIdle() )
        //     gottenAnim.start();

        return this;
    }

    private void onVisible()
    {
        // if( vpRef != null )
        //     vpRef.window.keys.thieves.startStealing(charStealer);
    }

    private void onInvisible()
    {
        // if( vpRef != null)
        //     vpRef.window.keys.thieves.stopStealing(charStealer);
    }

}