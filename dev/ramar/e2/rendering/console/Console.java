package dev.ramar.e2.rendering.console;

import dev.ramar.e2.EngineR2;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.control.KeyController;
import dev.ramar.e2.rendering.control.Stealer;
import dev.ramar.e2.rendering.control.Stealable;

import dev.ramar.e2.rendering.drawing.rect.Rect;

// import dev.ramar.e2.rendering.ui.*;
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

    public EngineR2 engine;


    public ViewPort getViewPortRef()
    {   return vpRef;   }



    private final Stealer<Character> charStealer = new Stealer<Character>()
    {
        public void onSteal(Stealable<Character> s, Character c)
        {
            // TextField in = null;
            // for( ValuePair<String, Drawable> pair : shapes )
            // {
            //     if( pair.getOneVal().equals("consoleInput"))
            //     {
            //         in = (TextField)pair.getTwoVal();
            //         break;
            //     }
            // }

            // if( in != null )
            // {
            //     String inputText = in.getInputText();
            //     int intCode = (int)(char)c;
            //     switch(intCode)
            //     {
            //         // all codes we want to ignore:
            //         /*
            //             alt / cntrl / shift: 65535
            //         */
            //         case 65535:
            //             break;

            //         // '`'
            //         case 8:
            //             if( inputText != null )
            //             {
            //                 String newString = inputText.substring(0, Math.max(0,inputText.length() - 1));
            //                 in.withInput(newString);
            //             }

            //             break;
            //         // return character ('\n')
            //         case 10:
            //             parser.parseCommand(inputText.trim().toLowerCase());
            //             break;

            //         case (int)'`':
            //             animation_SwapVisibility();
            //             break;
            //         default:
            //             in.withInput(inputText == null ? "" + c : inputText + c);
            //     }
            // }
        }

        public boolean allowSimultaneousThievery(Stealer<Character> c)
        {
            return c != null;
        }
    };

    private Map<String, Animation> animations = new HashMap<>();

    public Console()
    {
        parser = ConsoleParser.createParser(this);
        out = System.out;
        setup();
        animationsSetup();
    }

    public Console(EngineR2 engine)
    {
        this.engine = engine;
        parser = ConsoleParser.createParser(this);
        out = System.out;
        setup();
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

    private List<ValuePair<String, Drawable>> shapes = new ArrayList<>();

    Rect getTextBox()
    {
        Rect exp = null;
        for( ValuePair vp : shapes )
        {
            if( vp.getOneVal().equals("textBox") )
            {
                exp = (Rect)vp.getTwoVal();
                break;
            }
        }
        return exp;
    }

    private void setup()
    {

        double maxWidth  = windowW == -1 ? (vpRef != null ?  vpRef.getLogicalWidth() : 400) : windowW,
               maxHeight = windowH == -1 ? (vpRef != null ? vpRef.getLogicalHeight() : 600) : windowH;
        windowW = maxWidth;
        windowH = maxHeight;
        Rect consoleBox = new Rect(x, y, maxWidth, maxHeight);

        consoleBox.getMod()
            .colour.with(150, 150, 150, 255)
            .fill.with()
        ;

        shapes.add(new ValuePair<String, Drawable>("consoleBox", consoleBox));


        // 20 for size, 10 for gap
        Rect textBox = new Rect(x + 5, y + maxHeight - 27, maxWidth - 10, 23);

        textBox.getMod()
            .colour.with(255, 255, 255, 150)
            .fill.with()
        ;

        shapes.add(new ValuePair<String, Drawable>("textBox", textBox));

        Rect outputBG = new Rect(x + 5, y + 5, maxWidth - 11, maxHeight - 34);

        outputBG.getMod()
            .colour.with(255, 255, 255, 30)
            .fill.with()
        ;


        Rect outputBox = new Rect(x + 5, y + 5, maxWidth - 10, maxHeight - 36);

        outputBox.getMod()
            .colour.with(255, 255, 255, 125)
            .fill.with()
        ;


        shapes.add(new ValuePair<String, Drawable>("outputBG",  outputBG));
        shapes.add(new ValuePair<String, Drawable>("outputBox", outputBox));

        // TextField tf = new TextField().withHint("Console");

        // tf.withPos(x + 10, y + maxHeight - 16);
        
        // tf.getMod()
        //     .alignment.with(1, 0)
        // ;

        // tf.getInput().getMod()
        //     .withColour(0, 0, 0, 255)
        // ;

      
        // shapes.add(new ValuePair<String, Drawable>("consoleInput", tf));


    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        vpRef = vp;
        if( visible )
        {
            for( ValuePair<String, Drawable> pair : shapes )
            {
                Drawable sh = pair.getTwoVal();
                sh.drawAt(this.x, this.y, vp);
            }
        }
    }


    /* Commands related section
    -===--------------------------
    */




    /* Animations 
    -===------------
    */

    private void animationsSetup()
    {
        x = -windowW;
        // Opening animation, the anonymous class allows for <percentage>
        // and <dist> to be nicely hidden away without being too annoying
        animations.put("onOpen", new RepeatableTaskAnimation()
        {
            private double percentage = 1.0;
            private double dist = windowW;

            private boolean first = true;
            private long startTime;
            // TODO: rename this
            protected void constructorOverrideable()
            {
                // these "anytime builder" methods i've been addicted with
                // allows for super easy modification of an animation
                withDelay(2);
                withExecTime(250);

                // based runnable lambda for cheeky listeners
                // (i've been addicted to messing around with these things
                //  and scope recently)
                withTask(() -> 
                {
                    x += (dist / ((double)getRepeatCount() / 2.0)) * percentage;
                    percentage -= ((double)1 / (double)getRepeatCount());

                });
                whenStart(() ->
                {
                    startTime = System.currentTimeMillis();
                    percentage = 1.0;
                    x = -dist;
                    visible = true;
                    onVisible();
                });
            }
        });


        animations.put("onClose", new RepeatableTaskAnimation()
        {
            private double percentage = 1.0;
            private double dist = windowW;
            protected void constructorOverrideable()
            {
                withDelay(3);
                withExecTime(75);
                withTask(() ->
                {
                    x -= (dist / ((double)getRepeatCount() / 2.0)) * percentage;
                    percentage -= ((double)1 / (double)getRepeatCount());
                });
                whenStart(() -> 
                {
                    percentage = 1.0;
                    onInvisible();
                });
                whenFinished(() ->
                {
                    visible = false;
                });
            }
        });
    }

    public Console animation_SwapVisibility()
    {
        String toGet = visible ? "onClose" : "onOpen";
        String other = visible ? "onOpen"  : "onClose";

        Animation gottenAnim = animations.get(toGet);
        Animation otherAnim  = animations.get(other);

        if( gottenAnim.isIdle() && otherAnim.isIdle() )
            gottenAnim.start();
        return this;
    }

    private void onVisible()
    {
        if( vpRef != null )
            vpRef.window.keys.thieves.startStealing(charStealer);
    }

    private void onInvisible()
    {
        if( vpRef != null)
            vpRef.window.keys.thieves.stopStealing(charStealer);
    }

}