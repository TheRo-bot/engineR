package dev.ramar.e2.console;


import dev.ramar.e2.rendering.ViewPort;
import dev.ramar.e2.rendering.Drawable;

import dev.ramar.e2.rendering.control.KeyReleaseListener;
import dev.ramar.e2.rendering.control.KeyCombo;

import dev.ramar.e2.rendering.drawing.rect.Rect;
import dev.ramar.e2.rendering.drawing.enums.*;
/* CapStyle, FontStyle, JoinStyle */

import dev.ramar.e2.demos.combat.DeltaUpdater;
import dev.ramar.e2.demos.combat.DeltaUpdater.Updatable;

import java.util.List;
import java.util.ArrayList;

// captures input and sends characters to ConsoleInput
// listens to ConsoleOutput's lines, and displays them on the screen

public class Console
{

    public final ConsoleInput in = new ConsoleInput();
    public final ConsoleOutput out = new ConsoleOutput();

    public final ConsoleParser parser = new ConsoleParser(this);

    public Console()
    {
        this.out.setLineCount(30);
    }


    private final KeyReleaseListener consoleToggle = (KeyCombo kc) ->
    {
        Console.this.animations_toggleOpen();
    };


    public void attach(ViewPort vp)
    {
        this.input.attach(vp);
        this.output.attach(vp);
        vp.window.keys.bindRel(ConsoleBinds.Events.consoleToggle, consoleToggle);
    }


    public void release(ViewPort vp)
    {
        this.input.release(vp);
        this.output.release(vp);
        vp.window.keys.unbindRel(ConsoleBinds.Events.consoleToggle, consoleToggle);
    }



    private boolean open = false;

    public void animations_toggleOpen()
    {
        open = !open;
        if( open )
        {
            this.input.animations_open();
            this.output.animations_open();
        }
        else
        {
            this.input.animations_close();
            this.output.animations_close();   
        }
    }


    private InputDrawable   input = new InputDrawable();
    private OutputDrawable output = new OutputDrawable();


    private class InputDrawable
    {
        public InputDrawable()
        {

        }
        private Rect bound = null;
        private Rect fill = null;
        private ViewPort vp = null;

        private void setup(ViewPort vp)
        {
            this.vp = vp;

            this.bound = new Rect();
            fill = new Rect(this.bound);
            this.redraw();
        }

        public void redraw()
        {
            double w = this.vp.window.width();
            double h = this.vp.window.height();


            if( this.bound != null )
            {
                this.bound.len.set(w * 0.8, h * 0.05);
                this.bound.pos.set(w * 0.1, h * 0.8);

                this.bound.getMod()
                    .colour.with(125, 71, 163, 255)
                    .fill.with()
                    .visible.without()
                    .stroke
                        .width.with(10)
                        .join.with(JoinStyle.Round)
                ;
            }
            if( this.fill != null )
            {
                this.fill.len.set(w * 0.8, h * 0.05);
                this.fill.pos.set(w * 0.1, h * 0.8);

                this.fill.getMod()
                    .colour.with(69, 31, 96, 255)
                    .fill.with()
                    .visible.without()
                    .stroke
                        .width.with(5)
                        .join.with(JoinStyle.Round)
                ;
            }
        }


        public void attach(ViewPort vp)
        {
            if( this.bound == null )
                this.setup(vp);
            vp.layers.console.add(this.bound);
            vp.layers.console.add(this.fill);
        }

        public void release(ViewPort vp)
        {
            vp.layers.console.remove(this.bound);
            vp.layers.console.remove(this.fill);
        }


        public void animations_open()
        {
            this.bound.getMod()
                .visible.with(true)
            ;
            this.fill.getMod()
                .visible.with(true)
            ;
            // double totalTime = 1.0;
            // double ahh = this.vp.window.height() - this.bound.pos.getY();
            // double startY = this.vp.window.height() + ahh;
            // double dist = ahh * -2;

            // double thing = 1.0;

            // this.bound.pos.setY(startY);
            // DeltaUpdater.getInstance().toUpdate.queueAdd(new Updatable()
            // {
            //     double endTime = totalTime;
            //     public boolean update(double d)
            //     {
            //         endTime -= d;
            //         boolean close = endTime < 0;
            //         if( !close )
            //         {
            //             InputDrawable.this.bound.pos.addY(dist / totalTime * d);
            //         }

            //         return close;
            //     }
            // });
        }
        public void animations_close()
        {
            this.bound.getMod()
                .visible.with(false)
            ;
            this.fill.getMod()
                .visible.with(false)
            ;
        }

    }

    private class OutputDrawable
    {
        public OutputDrawable()
        {

        }

        private ViewPort vp = null;
        private Rect bound = null;
        private Rect fill = null;

        private void setup(ViewPort vp)
        {
            this.vp = vp;
            double w = vp.window.width();
            double h = vp.window.height();

            this.bound = new Rect();
            this.bound.pos.set(w * 0.1, h * 0.1);
            this.bound.len.set(w * 0.8, h * 0.6);

            this.bound.getMod()
                .colour.with(125, 71, 163, 255)
                .fill.with()
                .visible.without()
                .stroke
                    .width.with(12.5f)
                    .join.with(JoinStyle.Round)
            ;

            this.fill = new Rect();

            this.fill.pos.set(w * 0.1, h * 0.1);
            this.fill.len.set(w * 0.8, h * 0.6);

            this.fill.getMod()
                .colour.with(69, 31, 96, 255)
                .fill.with()
                .visible.without()
                .stroke
                    .width.with(6.25f)
                    .join.with(JoinStyle.Round)
            ;
        }

        public void attach(ViewPort vp)
        {
            if( bound == null )
                this.setup(vp);

            vp.layers.console.add(this.bound);
            vp.layers.console.add(this.fill);
        }   

        public void release(ViewPort vp)
        {
            vp.layers.console.remove(this.bound);
            vp.layers.console.remove(this.fill);
        }


        public void animations_open()
        {
            this.bound.mod.visible.with();
            this.fill.mod.visible.with();
        }
        public void animations_close()
        {
            this.bound.mod.visible.without();
            this.fill.mod.visible.without();
        }

    }
}