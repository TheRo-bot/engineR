package dev.ramar.e2.rendering;

import dev.ramar.e2.rendering.Drawable;
import dev.ramar.e2.rendering.ViewPort;

import dev.ramar.e2.rendering.drawing.stateful.Rect;
import dev.ramar.e2.rendering.drawing.stateful.Shape;

import dev.ramar.e2.rendering.ui.*;
import dev.ramar.e2.rendering.animations.*;

import dev.ramar.utils.ValuePair;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

public class Console implements Drawable
{
    private double x = 0, y = 0;
    private double windowW = 400, windowH = 600;
    private boolean visible = false;

    private ViewPort vpRef;


    private Map<String, Animation> animations = new HashMap<>();

    public Console()
    {
        setup();
        animationsSetup();
    }

    public Console withVisibility(boolean b)
    {
        visible = b;
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

    private List<ValuePair<String, Shape>> shapes = new ArrayList<>();

    private void setup()
    {
        Rect consoleBox = new Rect(x, y)
            .withSize(windowW, windowH)
        ;
        consoleBox.getMod()
            .withColour(150, 150, 150, 255)
            .withFill()
        ;

        shapes.add(new ValuePair<String, Shape>("consoleBox", consoleBox));


        // 20 for size, 10 for gap
        Rect textBox = new Rect(x + 5, y + windowH - 25)
            .withSize(windowW - 10, 20)
        ;

        textBox.getMod()
            .withColour(255, 255, 255, 150)
            .withFill()
        ;

        shapes.add(new ValuePair<String, Shape>("textBox", textBox));

        Rect outputBG = new Rect(x + 5, y + 5)
            .withSize(windowW - 11, windowH - 34)
        ;

        outputBG.getMod()
            .withColour(255, 255, 255, 30)
            .withFill()
        ;


        Rect outputBox = new Rect(x + 5, y + 5)
            .withSize(windowW - 10, windowH - 35)
        ;

        outputBox.getMod()
            .withColour(255, 255, 255, 125)
            .withFill()
        ;

        shapes.add(new ValuePair<String, Shape>("outputBG",  outputBG));
        shapes.add(new ValuePair<String, Shape>("outputBox", outputBox));

        TextField tf = new TextField().withHint("<Console>");

        tf
            .withAlignment(1, 0)
            .withPos(x + 10, y + windowH - 12.5)
        ;

        shapes.add(new ValuePair<String, Shape>("consoleInput", tf));

        // shapes.put("consoleInput", tf);
        // shapes.put("textBox", textBox);
        // shapes.put("consoleBox", consoleBox);

    }


    public void drawAt(double x, double y, ViewPort vp)
    {
        if( visible )
        {
            for( ValuePair<String, Shape> pair : shapes )
            {
                Shape sh = pair.getTwoVal();
                sh.drawAt(this.x, this.y, vp);
            }
        }
    }

    /* Animations 
    -===------------
    */

    private void animationsSetup()
    {
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
                withExecTime(4000);

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
                    System.out.println("uh: " + getRepeatCount());
                    startTime = System.currentTimeMillis();
                    percentage = 1.0;
                    x = -windowW;
                    visible = true;
                });
                whenFinished(() ->
                {
                    onVisible();   
                    System.out.println("took " + (System.currentTimeMillis() - startTime) + "ms");
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
                withExecTime(250);
                withTask(() ->
                {
                    x -= (dist / ((double)getRepeatCount() / 2.0)) * percentage;
                    percentage -= ((double)1 / (double)getRepeatCount());
                });
                whenStart(() -> 
                {
                    percentage = 1.0;
                });
                whenFinished(() ->
                {
                    onInvisible();
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
        System.out.println("onVisible");
    }

    private void onInvisible()
    {
        System.out.println("onInvisible");
    }

}